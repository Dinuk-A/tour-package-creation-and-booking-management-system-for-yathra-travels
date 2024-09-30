package lk.yathra.sightseeing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttrActivityController {

    @Autowired
    private AttrActivityDao attrActivityDao;

    @GetMapping(value = "/attractivity/alldata", produces = "application/json")
    public List<AttrActivity> getAttrActivities() {
        return attrActivityDao.findAll();
    }

}
