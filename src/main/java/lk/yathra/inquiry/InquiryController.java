package lk.yathra.inquiry;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.UserDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class InquiryController {

    @Autowired
    private InquiryDao inqDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao userDao;

    @Autowired
    private InquiryStatusDao inqStatusDao;

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public ModelAndView inquiryUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView inquiryView = new ModelAndView();
        inquiryView.setViewName("inquiry.html");
        inquiryView.addObject("loggedusername", auth.getName());
        inquiryView.addObject("title", "Yathra Inquiry");

        return inquiryView;
    }

    @GetMapping(value = "/inquiry/alldata", produces = "application/json")
    public List<Inquiry> getAllInquiryData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Inquiry>();
        }

        return inqDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/inquiry/inqsexceptnewandinprogress", produces = "application/json")
    public List<Inquiry> getAllInquiryDataByStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Inquiry>();
        }

        return inqDao.getAllInqsExceptNewAndInProgress();
    }

    @GetMapping(value = "/inquiry/newandinprogressonly", produces = "application/json")
    public List<Inquiry> getoOnlyNewandInProgressInqs() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY_RESPONSE");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Inquiry>();
        }

        return inqDao.getNewAndInprogressInqList();
    }

    // inqs from website
    @PostMapping(value = "/inquiry")
    public String saveGeneralInqFromWeb(@RequestBody Inquiry inq) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {

            String inqCode = inqDao.getNextInquiryCode();
            if (inqCode == null || inqCode.equals("")) {
                inq.setInqcode("INQ00001");
            } else {
                inq.setInqcode(inqCode);
            }

            if (inq.getRecievedmethod() == null) {
                inq.setRecievedmethod("Website");

                // set recieved date and time within this for inqs sent by clients via website
                inq.setRecieveddate(LocalDate.now());
                inq.setRecievedtime(LocalTime.now());
                InquiryStatus newStatus = inqStatusDao.getReferenceById(1);
                inq.setInquirystatus(newStatus);
            }

            if (inq.getInqtype() == null) {
                inq.setInqtype("General");

                inq.setRecieveddate(LocalDate.now());
                inq.setRecievedtime(LocalTime.now());

            }

            // if(inq.getInqtype()!="General" || inq.getInqtype()!="Package-Related"){
            // inq.setInqtype("Manual Entry");
            // }

            // if an employee manually added this which recieved from email or call
            inq.setAddeddatetime(LocalDateTime.now());
            inq.setAddeduserid(userDao.getByUName(auth.getName()).getId());

            inqDao.save(inq);
            return "OK";

        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    // inqs from website
    @PostMapping(value = "/inquirybycustomer")
    public String saveGeneralInqByCustomer(@RequestBody Inquiry inq) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {

            String inqCode = inqDao.getNextInquiryCode();
            if (inqCode == null || inqCode.equals("")) {
                inq.setInqcode("INQ00001");
            } else {
                inq.setInqcode(inqCode);
            }

            if (inq.getRecievedmethod() == null) {
                inq.setRecievedmethod("Website");

                InquiryStatus newStatus = inqStatusDao.getReferenceById(1);
                inq.setInquirystatus(newStatus);
            }

            if (inq.getInqtype() == null) {
                inq.setInqtype("General");
            }

            inq.setAddeddatetime(LocalDateTime.now());

            inqDao.save(inq);
            return "OK";

        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    // create get mapping for get only the new inquiries
    @GetMapping(value = "/inquiry/onlynew", produces = "application/json")
    public List<Inquiry> getNewInqsList() {
        return inqDao.getOnlyNewInquiries();
    }

    // create get mapping for get only inquiries that currently in progress
    @GetMapping(value = "/inquiry/inprogress", produces = "application/json")
    public List<Inquiry> getInProgressInqsList() {
        return inqDao.getOnlyInProgressInquiries();
    }

    @PutMapping("/inquiry")
    public String updateInquiry(@RequestBody Inquiry inq) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed : You Dont Have Permission";
        }

        Inquiry existingInq = inqDao.getReferenceById(inq.getId());

        try {

            existingInq.setIsonworking(false);
            existingInq.setCurrentworkinguser(null);

            // inq detail changes
            existingInq.setInqforeignadultcount(inq.getInqforeignadultcount());
            existingInq.setInqforeignchildcount(inq.getInqforeignchildcount());
            existingInq.setInqlocaladultcount(inq.getInqlocaladultcount());
            existingInq.setInqlocalchildcount(inq.getInqlocalchildcount());
            existingInq.setPickuplocation(inq.getPickuplocation());
            existingInq.setDropofflocation(inq.getDropofflocation());
            existingInq.setArrivaldate(inq.getArrivaldate());
            existingInq.setIsguideinclude(inq.getIsguideinclude());

            // customer deail changes
            existingInq.setClientname(inq.getClientname());
            existingInq.setNationality(inq.getNationality());
            existingInq.setPassportnumornic(inq.getPassportnumornic());
            existingInq.setContactnum(inq.getContactnum());
            existingInq.setContactnumtwo(inq.getContactnumtwo());
            existingInq.setEmail(inq.getEmail());

            // other
            existingInq.setNote(inq.getNote());

            // auto
            existingInq.setLastmodifieddatetime(LocalDateTime.now());
            existingInq.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            inqDao.save(existingInq);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }

    }

    // DELETE MAPPING EPA

    @PutMapping("/inquiryonworking")
    public String updateInquiryOnWorking(@RequestBody Inquiry inq) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "INQUIRY");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        try {

            inq.setCurrentworkinguser(userDao.getByUName(auth.getName()).getId());
            inqDao.save(inq);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }

    }

}
