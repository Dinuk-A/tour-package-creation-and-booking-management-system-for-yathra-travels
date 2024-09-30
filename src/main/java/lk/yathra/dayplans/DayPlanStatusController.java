package lk.yathra.dayplans;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DayPlanStatusController {

    @Autowired
    private DayPlanStatusDao dpSttsDao;

    @GetMapping(value = "/dpstatus/alldata", produces = "application/json")
    public List<DayPlanStatus> getDpStatusAllData() {
        return dpSttsDao.findAll();
    }
}
