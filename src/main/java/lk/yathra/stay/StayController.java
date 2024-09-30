package lk.yathra.stay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.UserDao;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class StayController {

    @Autowired
    private StayDao stayDao;

    @Autowired
    private StayStatusDao staySttsDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/stay", method = RequestMethod.GET)
    public ModelAndView stayUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView stayView = new ModelAndView();
        stayView.setViewName("stay.html");
        stayView.addObject("loggedusername", auth.getName());
        stayView.addObject("title", "Yathra Accomadation");

        return stayView;

    }

    @GetMapping(value = "/stay/alldata", produces = "application/JSON")
    public List<Stay> getStayAllData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "STAY");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Stay>();
        }

        return stayDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/stay/bydistrict/{givenDistrict}", produces = "application/json")
    public List<Stay> getStaysByDist(@PathVariable Integer givenDistrict) {
        return stayDao.getStayListByDistrict(givenDistrict);
    }

    @PostMapping(value = "/stay")
    public String saveStayinfo(@RequestBody Stay stay) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "STAY");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {
            stay.setAddeddatetime(LocalDateTime.now());
            stay.setAddeduserid(userDao.getByUName(auth.getName()).getId());
            stayDao.save(stay);
            return "OK";
        } catch (Exception e) {
            return "save not completed : " + e.getMessage();

        }

    }

    @PutMapping(value = "/stay")
    public String updateStay(@RequestBody Stay stay) {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "STAY");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Update Not Completed You Dont Have Permission";
        }

        // check existence

        // duplications

        try {
            stay.setLastmodifieddatetime(LocalDateTime.now());
            stay.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            stayDao.save(stay);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed ; " + e.getMessage();
        }
    }

    @DeleteMapping(value = "/stay")
    public String deleteStay(@RequestBody Stay stay) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "STAY");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        // check existence
        Stay existStay = stayDao.getReferenceById(stay.getId());
        if (existStay == null) {
            return "Record Not Found";
        }

        try {
            existStay.setDeleteddatetime(LocalDateTime.now());
            existStay.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            StayStatus deleteStatus = staySttsDao.getReferenceById(4);
            existStay.setStaystatus_id(deleteStatus);
            stayDao.save(existStay);

            return "OK";
        } catch (Exception e) {
            return "Delete Not Completed : " + e.getMessage();
        }

    }

}
