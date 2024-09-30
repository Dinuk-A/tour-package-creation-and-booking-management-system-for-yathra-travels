package lk.yathra.stay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StayTypeController {

    @Autowired
    private StayTypeDao sTypeDao;

    @GetMapping(value = "/staytype/alldata", produces = "application/json")
    public List<StayType> getStayStatusesAllData() {
        return sTypeDao.findAll();
    }

}
