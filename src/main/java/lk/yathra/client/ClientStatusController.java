package lk.yathra.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientStatusController {

    @Autowired
    private ClientsSatusDao cliSttsDao;

    @GetMapping(value = "/clientstatus/alldata", produces = "application/json")
    public List<ClientStatus> getClientStatusAllData() {
        return cliSttsDao.findAll();
    }
}
