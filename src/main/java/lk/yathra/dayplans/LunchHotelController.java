package lk.yathra.dayplans;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.UserDao;

@RestController
public class LunchHotelController {

    @Autowired
    private LunchHotelDao lunchDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LunchHotelStatusDao lHotelSttsDao;

    @RequestMapping(value = "/lunchhotel", method = RequestMethod.GET)
    public ModelAndView lunchMgtUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView lunchMgtView = new ModelAndView();
        lunchMgtView.setViewName("lunchhotel.html");
        lunchMgtView.addObject("loggedusername", auth.getName());
        lunchMgtView.addObject("title", "Yathra Lunch");

        return lunchMgtView;

    }

    // get all data
    @GetMapping(value = "/lunchhotel/alldata", produces = "application/JSON")
    public List<LunchHotel> getLunchHotelAllData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "LUNCH_HOTEL");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<LunchHotel>();
        }

        return lunchDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    // get by given district
    @GetMapping(value = "/lunchhotel/bydistrict/{givenDistrict}", produces = "application/json")
    public List<LunchHotel> getLHotelsByDist(@PathVariable Integer givenDistrict) {
        return lunchDao.getLunchHotelsByGivenDistrict(givenDistrict);
    }

    @PostMapping(value = "/lunchhotel")
    public String saveLunchPlace(@RequestBody LunchHotel lh) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "LUNCH_HOTEL");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {
            lh.setAddeddatetime(LocalDateTime.now());
            lunchDao.save(lh);
            return "OK";
        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    @PutMapping(value = "/lunchhotel")
    public String updateLHotel(@RequestBody LunchHotel lh) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "LUNCH_HOTEL");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed : You Dont Have Permission";
        }

        try {
            lh.setLastmodifieddatetime(LocalDateTime.now());
            lh.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            lunchDao.save(lh);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed ; " + e.getMessage();
        }
    }

    @DeleteMapping(value = "/lunchhotel")
    public String deleteLhotelRecord(@RequestBody LunchHotel lh) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "LUNCH_HOTEL");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        LunchHotel existLH = lunchDao.getReferenceById(lh.getId());
        if (existLH == null) {
            return "Delete Not Completed, Record Not Found";
        }
        try {
            lh.setDeleteddatetime(LocalDateTime.now());
            lh.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            LunchHotelStatus deleteStatus = lHotelSttsDao.getReferenceById(2);
            existLH.setLhotelstts_id(deleteStatus);
            lunchDao.save(existLH);
            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed : " + e.getMessage();
        }

    }
}
