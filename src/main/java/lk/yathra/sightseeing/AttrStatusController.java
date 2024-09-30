package lk.yathra.sightseeing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttrStatusController {

    @Autowired
    private AttrStatusDao attrStatusDao;

    @GetMapping(value = "/attrstatus/alldata", produces = "application/json")
    public List<AttrStatus> getAttrStatusAllData() {
        return attrStatusDao.findAll();
    }

}
