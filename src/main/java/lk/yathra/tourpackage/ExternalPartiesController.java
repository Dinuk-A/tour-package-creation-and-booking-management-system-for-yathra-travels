package lk.yathra.tourpackage;

import java.time.LocalDate;
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
public class ExternalPartiesController {

    @Autowired
    private ExternalPartiesDao externalPartiesDao;

    @Autowired
    private EPSttsDao epSttsDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao uDao;

    // load UI
    @RequestMapping(value = "/externalparties", method = RequestMethod.GET)
    public ModelAndView externalPartiesUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView externalPartiesView = new ModelAndView();
        externalPartiesView.setViewName("externalparty.html");
        externalPartiesView.addObject("loggedusername", auth.getName());
        externalPartiesView.addObject("title", "Yathra External Parties");

        return externalPartiesView;
    }

    // get all data
    @GetMapping(value = "/externalparties/alldata", produces = "application/JSON")
    public List<ExternalParties> getExternalPartyllData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EXTERNAL_EMPLOYEE");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<ExternalParties>();
        }

        return externalPartiesDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    // get available driver list
    @GetMapping(value = "exp/drivers", produces = "application/JSON")
    public List<ExternalParties> getExternalDrivers() {
        return externalPartiesDao.getExternalDriversList();
    }

    // get only guides
    @GetMapping(value = "exp/guides", produces = "application/JSON")
    public List<ExternalParties> getExternalGuides() {
        return externalPartiesDao.getExternalDriversList();
    }

    // save records
    @PostMapping(value = "/externalparties")
    public String saveExtEmpInfo(@RequestBody ExternalParties externalPartyRecordObj) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EXTERNAL_EMPLOYEE");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        ExternalParties existingExp = externalPartiesDao.getExpsByNIC(externalPartyRecordObj.getNic());
        if (existingExp != null) {
            return "\n An External Employee With This NIC Already Exists";
        }

        try {
            externalPartyRecordObj.setAddeddatetime(LocalDateTime.now());
            externalPartyRecordObj.setAddeduserid(uDao.getByUName(auth.getName()).getId());
            externalPartiesDao.save(externalPartyRecordObj);
            return "OK";
        } catch (Exception e) {
            return "Save Not Completed: " + e.getMessage();
        }
    }

    // update records
    @PutMapping(value = "/externalparties")
    public String updateExternalParty(@RequestBody ExternalParties externalPartyRecordObj) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EXTERNAL_EMPLOYEE");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed : You Dont Have Permission";
        }

        ExternalParties existingExp = externalPartiesDao.getExpsByNIC(externalPartyRecordObj.getNic());

        if (existingExp != null && existingExp.getId() != externalPartyRecordObj.getId()) {
            return "Update Failed : An External Employee With This NIC Already Exists";
        }

        try {
            externalPartyRecordObj.setLastmodifieddatetime(LocalDateTime.now());
            externalPartyRecordObj.setLastmodifieduserid(uDao.getByUName(auth.getName()).getId());
            externalPartiesDao.save(externalPartyRecordObj);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }

    }

    // delete records
    @DeleteMapping(value = "/externalparties")
    public String deleteExternalParty(@RequestBody ExternalParties externalPartyRecordObj) {
        // authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EXTERNAL_EMPLOYEE");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        ExternalParties existingRecord = externalPartiesDao.getReferenceById(externalPartyRecordObj.getId());
        if (existingRecord == null) {
            return "Delete Not Completed , Employee Not Exist";
        }

        try {
            existingRecord.setDeleteddatetime(LocalDateTime.now());
            existingRecord.setDeleteduserid(uDao.getByUName(auth.getName()).getId());

            // soft delete
            EPStatus deleteStatus = epSttsDao.getReferenceById(2);
            existingRecord.setExtstatus_id(deleteStatus);
            externalPartiesDao.save(existingRecord);
            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed " + e.getMessage();
        }

    }
}
