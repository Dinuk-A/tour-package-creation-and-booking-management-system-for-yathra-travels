package lk.yathra.sightseeing;

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
public class AttractionController {

    @Autowired
    private AttractionDao attrDao;

    @Autowired
    private AttrStatusDao atSttsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @RequestMapping(value = "/attraction", method = RequestMethod.GET)
    public ModelAndView attractionUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView attrView = new ModelAndView();
        attrView.setViewName("attraction.html");
        attrView.addObject("loggedusername", auth.getName());
        attrView.addObject("title", "Yathra Attraction");

        return attrView;

    }

    @GetMapping(value = "/attraction/alldata")
    public List<Attraction> getAttrAllDList() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "ATTRACTION");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Attraction>();
        }

        return attrDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    // for get attractions based on district >> to day plan creation
    @GetMapping(value = "/attraction/bydistrict/{selectedDistrict}", produces = "application/json")
    public List<Attraction> getAttrListByDist(@PathVariable Integer selectedDistrict) {
        return attrDao.attrListByDistrict(selectedDistrict);
    }

    @PostMapping(value = "/attraction")
    public String saveVPlace(@RequestBody Attraction vplace) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "ATTRACTION");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {
            vplace.setAddeddatetime(LocalDateTime.now());
            vplace.setAddeduserid(userDao.getByUName(auth.getName()).getId());

            attrDao.save(vplace);

            return "OK";
        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    @PutMapping(value = "/attraction")
    public String updateVPlace(@RequestBody Attraction vplace) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "ATTRACTION");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed You Dont Have Permission";
        }

        // check existence

        // duplications

        try {
            vplace.setLastmodifieddatetime(LocalDateTime.now());
            vplace.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());

            attrDao.save(vplace);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed ; " + e.getMessage();
        }
    }

    @DeleteMapping(value = "/attraction")
    public String deleteVplace(@RequestBody Attraction vplace) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "ATTRACTION");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        // check existence
        Attraction existVPlace = attrDao.getReferenceById(vplace.getId());
        if (existVPlace == null) {
            return "Record Not Found";
        }
        try {
            existVPlace.setDeleteddatetime(LocalDateTime.now());
            existVPlace.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            AttrStatus deletedStatus = atSttsDao.getReferenceById(4);
            existVPlace.setAttrstatus_id(deletedStatus);
            attrDao.save(existVPlace);

            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed : " + e.getMessage();
        }
    }
}
