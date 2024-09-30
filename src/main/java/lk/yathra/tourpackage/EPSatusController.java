package lk.yathra.tourpackage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EPSatusController {

    @Autowired
    private EPSttsDao epsttsDao;

    @GetMapping(value = "/epstatus/alldata", produces = "application/json")
    public List<EPStatus> getAttrStatusAllData() {
        return epsttsDao.findAll();
    }
    
}
