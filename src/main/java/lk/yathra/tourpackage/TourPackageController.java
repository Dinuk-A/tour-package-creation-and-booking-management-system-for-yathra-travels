package lk.yathra.tourpackage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.transaction.Transactional;
import lk.yathra.inquiry.Inquiry;
import lk.yathra.inquiry.InquiryDao;
import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.User;
import lk.yathra.user.UserDao;

@RestController
public class TourPackageController {

    @Autowired
    private TourPackageDao tPkgDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private TourPackageStatusDao tpkgStatusDao;

    @Autowired
    private InquiryDao daoInquery;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/tourpackage", method = RequestMethod.GET)
    public ModelAndView tPkgUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userDao.getByUName(auth.getName());
        ModelAndView tPkgView = new ModelAndView();
        tPkgView.setViewName("tourpackage.html");
        tPkgView.addObject("loggedusername", auth.getName());
        // tPkgView.addObject("loggedUserPhoto", loggedUser.getUser_photo());
        tPkgView.addObject("loggeduserdesignation", loggedUser.getEmployee_id().getDesignation_id().getName());
        // tPkgView.addObject("userrole",
        // loggedUser.getRoles().iterator().next().getName());
        // tPkgView.addObject("title", "Yathra Tour Package");

        return tPkgView;
    }

    @GetMapping(value = "/tourpackage/alldata", produces = "application/json")
    public List<TourPackage> getAllTourPackageData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TOUR_PACKAGE");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<TourPackage>();
        }
        return tPkgDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    // TO SAVE TOUR PACKAGE RECORDS IN DB
    @Transactional
    @PostMapping(value = "/tourpackage")
    public String saveTourPackage(@RequestBody TourPackage tpkg) {

        // TO GET LOGGED USER'S INFORMATION
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TOUR_PACKAGE");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {
            // SET THE DATE AND TIME OF SAVING RECORD
            tpkg.setAddeddatetime(LocalDateTime.now());

            // SAVE THE USER ID OF THE USER WHO SAVING THIS RECORD
            tpkg.setAddeduserid(userDao.getByUName(auth.getName()).getId());

            //
            for (TourPackageHasDayPlan tphdp : tpkg.getTourpkgHasDaypPlanList()) {
                tphdp.setTourpackage_id(tpkg);
            }

            // SET TOUR PACKAGE'S UNIQUE CODE
            String tpCode = tPkgDao.nextTPCode();

            // IF NO TOUR PACKAGES RECORDS ARE FOUND BEFORE THIS; MAKE THE CODE AS "TP00001"
            if (tpCode == null || tpCode.equals("")) {
                tpkg.setPackagecode("TP00001");
                // IF MORE RECORDS ARE THERE ALREADY; GET THE NEXT CODE BY DAO
            } else {
                tpkg.setPackagecode(tPkgDao.nextTPCode());
            }

            // SAVE THE CHANGES DONE BY BACKEND
            TourPackage savedPackage = tPkgDao.save(tpkg);

            if(savedPackage.getBasedinquiry() != null){
                Inquiry inquiry = daoInquery.getByCode(savedPackage.getBasedinquiry());
                inquiry.setCus_tpkg_id(savedPackage);
                daoInquery.save(inquiry);
            }

            return tpCode;

            // ALERT THE USER IF AN ERROR OCCURED
        } catch (Exception e) {
            return "Save Not Completed: " + e.getMessage();
        }
    }

    @PutMapping(value = "/tourpackage")
    public String updateTourPkg(@RequestBody TourPackage tpkg) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TOUR_PACKAGE");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed : You Dont Have Permission";
        }

        try {
            for (TourPackageHasDayPlan tphdp : tpkg.getTourpkgHasDaypPlanList()) {
                tphdp.setTourpackage_id(tpkg);
            }
            tpkg.setLastmodifieddatetime(LocalDateTime.now());
            tpkg.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            tPkgDao.save(tpkg);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }
    }

    @DeleteMapping(value = "/tourpackage")
    public String deleteTourPkg(@RequestBody TourPackage tpkg) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TOUR_PACKAGE");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        TourPackage isExistPackage = tPkgDao.getReferenceById(tpkg.getId());
        if (isExistPackage == null) {
            return "Delete Not Completed , Package Not Found";
        }
        try {

            for (TourPackageHasDayPlan tphdp : tpkg.getTourpkgHasDaypPlanList()) {
                tphdp.setTourpackage_id(tpkg);
            }
            isExistPackage.setDeleteddatetime(LocalDateTime.now());
            isExistPackage.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            TourPackageStatus dltStatus = tpkgStatusDao.getReferenceById(2);
            isExistPackage.setTourpackagestatus_id(dltStatus);
            tPkgDao.save(isExistPackage);

            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed " + e.getMessage();
        }

    }
}
