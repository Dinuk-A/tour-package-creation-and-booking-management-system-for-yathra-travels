package lk.yathra.privilege;

import java.time.LocalDateTime;
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
//import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PrivilegeController {

    @Autowired
    private PrivilegeDao daoPrivilege;

    @GetMapping(value = "/privilege/bymodule/{modulename}")
    public Privilege getPrvByModules(@PathVariable("modulename") String modulename) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return getPrivilegesByUserAndModule(auth.getName(), modulename);
    }

    public Privilege getPrivilegesByUserAndModule(String username, String modulename) {

        if (username.equals("Admin")) {

            Boolean select = true;
            Boolean insert = true;
            Boolean update = true;
            Boolean delete = true;

            Privilege adminPrivi = new Privilege(select, insert, update, delete);
            return adminPrivi;
        }

        else {
            String privi = daoPrivilege.getPrivilegesByUserAndModuleUsingDaoQuery(username, modulename);

            String[] priviArray = privi.split(",");
            Boolean select = priviArray[0].equals("1");
            Boolean insert = priviArray[1].equals("1");
            Boolean update = priviArray[2].equals("1");
            Boolean delete = priviArray[3].equals("1");

            Privilege userPrivi = new Privilege(select, insert, update, delete);

            return userPrivi;
        }
    }

    @GetMapping(value = "/privilege/alldata", produces = "application/json")
    public List<Privilege> getPrvAllData() {
        return daoPrivilege.findAll(Sort.by(Direction.DESC, "id"));
    }

    @RequestMapping(value = "/privilege", method = RequestMethod.GET)
    public ModelAndView privilegeUi() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView privilegeView = new ModelAndView();
        privilegeView.setViewName("privilege.html");
        privilegeView.addObject("loggedusername", auth.getName());
        privilegeView.addObject("title", "Yathra Privilege");
        return privilegeView;
    }

    @PutMapping(value = "/privilege")
    public String editPrivilege(@RequestBody Privilege privilege) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // check existing
        Privilege existPrivilege = daoPrivilege.getReferenceById(privilege.getId());

        if (existPrivilege == null) {
            return "Update Not Completed : Record Not Found";
        }

        try {
            privilege.setLastmodifieddatetime(LocalDateTime.now());
            daoPrivilege.save(privilege);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed " + e.getMessage();
        }
    }

    @DeleteMapping(value = "/privilege")
    public String deletePrivilege(@RequestBody Privilege privilege) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            privilege.setDeleteddatetime(LocalDateTime.now());
            // prvDao.delete(privilege);
            daoPrivilege.delete(daoPrivilege.getReferenceById(privilege.getId()));
            return "OK";
        } catch (Exception e) {
            return "Delete Not Completed " + e.getMessage();
        }
    }

    @PostMapping(value = "/privilege")
    public String SavePrivilege(@RequestBody Privilege privilege) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // duplicates
        Privilege existPrivilege = daoPrivilege.getByRoleModule(privilege.getRole().getId(),
                privilege.getModule().getId());
        if (existPrivilege != null) {
            return "Save failed : This privilege is already granted for this Role & Module";
        }
        try {
            privilege.setAddeddatetime(LocalDateTime.now());
        
            daoPrivilege.save(privilege);
            return "OK";
        } catch (Exception e) {
            return ("Save Not Competeded " + e.getMessage());
        }
    }

}
