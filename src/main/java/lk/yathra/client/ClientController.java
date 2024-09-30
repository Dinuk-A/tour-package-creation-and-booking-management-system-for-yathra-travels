package lk.yathra.client;

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
public class ClientController {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ClientsSatusDao cliSttsDao;

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ModelAndView clientUi() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView clientView = new ModelAndView();
        clientView.setViewName("client.html");
        clientView.addObject("loggedusername", auth.getName());
        clientView.addObject("title", "Yathra Client");

        return clientView;
    }

    @GetMapping(value = "/client/alldata", produces = "application/JSON")
    public List<Client> getEmployeeAllData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "CLIENT");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Client>();
        }

        return clientDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/client/byclietnicorppt/{nicorppt}", produces = "application/JSON")
    public Client getByNicOrPPt(@PathVariable("nicorppt")String nicorppt) {
        return clientDao.getClientByPassportOrNIC(nicorppt);
    }

    @PostMapping(value = "/client")
    public String saveClient(@RequestBody Client clnt) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "CLIENT");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {
            String clientRegNum = clientDao.getNextClientRegNo();

            if (clientRegNum == null || clientRegNum.equals("")) {
                clnt.setClientregno("0000001");
            } else {
                clnt.setClientregno(clientDao.getNextClientRegNo());
            }

            clnt.setAddeddatetime(LocalDateTime.now());
            clnt.setAddeduserid(userDao.getByUName(auth.getName()).getId());
            clientDao.save(clnt);
            return "OK";
        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }

    }

    @PutMapping(value = "/client")
    public String updateClient(@RequestBody Client client) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "CLIENT");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed You Dont Have Permission";
        }

        try {
            client.setLastmodifieddatetime(LocalDateTime.now());
            client.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            clientDao.save(client);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed ; " + e.getMessage();
        }
    }

    @DeleteMapping(value = "/client")
    public String deleteClient(@RequestBody Client client) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "CLIENT");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed You Dont Have Permission";
        }

        Client existClient = clientDao.getReferenceById(client.getId());
        if (existClient == null) {
            return "Record Not Found";
        }

        try {
            existClient.setDeleteddatetime(LocalDateTime.now());
            existClient.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            ClientStatus deletedStatus = cliSttsDao.getReferenceById(2);
            existClient.setClientstatus(deletedStatus);

            clientDao.save(existClient);
            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed : " + e.getMessage();
        }
    }

}
