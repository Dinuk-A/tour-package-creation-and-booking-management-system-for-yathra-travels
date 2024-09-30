package lk.yathra.inquiry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.transaction.Transactional;
import lk.yathra.booking.Booking;
import lk.yathra.booking.BookingDao;
import lk.yathra.booking.BookingStatus;
import lk.yathra.booking.BookingStatusDao;
import lk.yathra.client.Client;
import lk.yathra.client.ClientDao;
import lk.yathra.client.ClientStatus;
import lk.yathra.client.ClientsSatusDao;
import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.tourpackage.TourPackage;
import lk.yathra.tourpackage.TourPackageDao;
import lk.yathra.user.UserDao;

@RestController
public class InquiryResponseController {

    @Autowired
    private InquiryResponseDao inqResDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private BookingStatusDao bookingStatusDao;

    @Autowired
    private InquiryDao inqDao;

    @Autowired
    private InquiryStatusDao inqStatusDao;

    @Autowired
    private InquiryResponseStatusDao inqResStatusDao;

    @Autowired
    private UserDao uDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ClientsSatusDao clientSttsDao;

    @Autowired
    private TourPackageDao tPkgDao;

    @Autowired
    private BookingStatusDao bkSttsDao;

    @RequestMapping(value = "/inquiryresponse", method = RequestMethod.GET)
    public ModelAndView inquiryResponseUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView inquiryResponseView = new ModelAndView();
        inquiryResponseView.setViewName("inquiryresponse.html");
        inquiryResponseView.addObject("loggedusername", auth.getName());
        inquiryResponseView.addObject("title", "Inquiry Responses");

        return inquiryResponseView;
    }

    // @GetMapping(value = "/inquiryresponse/alldata", produces =
    // "application/json")
    // public List<InquiryResponse> getAllInqResponseData() {
    // return inqResDao.findAll(Sort.by(Direction.DESC, "id"));
    // }

    // get inquiry responses by this inquiry id
    @GetMapping(value = "/inquiryresponse/byinquiry/{inquid}", produces = "application/json")
    public List<InquiryResponse> getResponseByInq(@PathVariable("inquid") int inquid) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY_RESPONSE");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<InquiryResponse>();
        }

        return inqResDao.byinquiry(inquid);
    }

    @PostMapping(value = "/inquiryresponse")
    @Transactional
    public String saveInqResponse(@RequestBody InquiryResponse inqResp) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY_RESPONSE");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {

            inqResp.setAddeddatetime(LocalDateTime.now());
            inqResp.setAddeduserid(uDao.getByUName(auth.getName()).getId());

            List<InquiryResponse> inqResList = inqResDao.byinquiry(inqResp.getInquiry_id().getId());
            if (inqResList.size() == 4) {
                if (inqResp.getInqResponseStatus().getName().equals("Client Not Responded")
                        || inqResp.getInqResponseStatus().getName().equals("Waiting")
                        || inqResp.getInqResponseStatus().getName().equals("Cancelled Inquiry")) {
                    inqResp.setInqResponseStatus(inqResStatusDao.getReferenceById(4));
                }
            }

            InquiryResponse savedinqRes = inqResDao.save(inqResp);

            Inquiry resInq = inqDao.getReferenceById(savedinqRes.getInquiry_id().getId());
            resInq.setIsonworking(false);
            resInq.setCurrentworkinguser(null);

            Client savedClient = null;

            if (savedinqRes.getInqResponseStatus().getName().equals("Waiting")) {
                resInq.setInquirystatus(inqStatusDao.getReferenceById(2));
            }

            if (savedinqRes.getInqResponseStatus().getName().equals("Confirmed")) {
                resInq.setInquirystatus(inqStatusDao.getReferenceById(4));
            }

            if (savedinqRes.getInqResponseStatus().getName().equals("Cancelled Inquiry")) {
                resInq.setInquirystatus(inqStatusDao.getReferenceById(3));
            }

            if (savedinqRes.getInqResponseStatus().getName().equals("Confirmed")) {

                //first check if this client already exists 
                Client existClient = clientDao.getClientByPassportOrNIC(resInq.getPassportnumornic());
                if (existClient == null) {

                    // auto create a client then a booking record
                    Client newClient = new Client();
                    newClient.setClienttitle(inqResp.getInquiry_id().getClienttitle());
                    newClient.setClientname(inqResp.getInquiry_id().getClientname());
                    newClient.setContactnum(inqResp.getInquiry_id().getContactnum());
                    newClient.setNationality(inqResp.getInquiry_id().getNationality());
                    newClient.setPassportnumornic(inqResp.getInquiry_id().getPassportnumornic());
                    newClient.setClientstatus(clientSttsDao.getReferenceById(1));
                    newClient.setAddeddatetime(LocalDateTime.now());
                    newClient.setAddeduserid(uDao.getByUName(auth.getName()).getId());
                    newClient.setEmail(inqResp.getInquiry_id().getEmail());
                    newClient.setNationality(inqResp.getInquiry_id().getNationality());

                    String clientNextRegNum = clientDao.getNextClientRegNo();

                    if (clientNextRegNum == null || clientNextRegNum.equals(null)) {
                        newClient.setClientregno("CL00001");
                    } else {
                        newClient.setClientregno(clientNextRegNum);
                    }

                    savedClient = clientDao.save(newClient);

                } else {
                    savedClient = existClient;
                }

                // create a booking obj concurrently
                Booking newBookingObj = new Booking();
                newBookingObj.setClient_id(savedClient);
                newBookingObj.setInquiry_id(savedinqRes.getInquiry_id());
                newBookingObj.setBookingstatus_id(bookingStatusDao.getReferenceById(1));
                newBookingObj.setAddeddatetime(LocalDateTime.now());
                newBookingObj.setAddeduserid(uDao.getByUName(auth.getName()).getId());
                newBookingObj.setTourpackage_id(inqResp.getInquiry_id().getCus_tpkg_id());
                newBookingObj.setTotalamount(inqResp.getInquiry_id().getCus_tpkg_id().getPkgprice());
                newBookingObj.setAdvance(inqResp.getInquiry_id().getCus_tpkg_id().getAdvanceamount()); 
                
                newBookingObj.setStartdate(inqResp.getInquiry_id().getCus_tpkg_id().getTourstartdate());
                newBookingObj.setEnddate(inqResp.getInquiry_id().getCus_tpkg_id().getTourenddate());           

                String nextBookingCode = bookingDao.getNextBookingCode();
                if (nextBookingCode == null || nextBookingCode.equals("")) {
                    newBookingObj.setBookingcode("BK000001");
                } else {
                    newBookingObj.setBookingcode(bookingDao.getNextBookingCode());
                }

                BookingStatus bkSttsPending = bkSttsDao.getReferenceById(1);
                newBookingObj.setBookingstatus_id(bkSttsPending);

                System.out.println("Booking Status"+ bkSttsPending);

                /* EmployeeStatus deleteStatus = empStatusDao.getReferenceById(3);
            isEmpExist.setEmployeestatus_id(deleteStatus);
            dao.save(isEmpExist); */

                bookingDao.save(newBookingObj);

            }

            inqDao.save(resInq);

            return "OK";
        } catch (

        Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    // get custom pkgs
    @GetMapping(value = "/tourpackagecustom/alldata", produces = "application/json")
    public List<TourPackage> getAllTourPackageData() {

        return tPkgDao.getCustomPkgs();
    }

}
