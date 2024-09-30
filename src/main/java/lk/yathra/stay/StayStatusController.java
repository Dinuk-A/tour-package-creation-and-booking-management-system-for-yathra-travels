package lk.yathra.stay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StayStatusController {
    
    @Autowired
    private StayStatusDao sSttsDao;

    @GetMapping(value = "/staystatus/alldata" , produces="application/json")
    public List<StayStatus> getStayStatusAllData(){
        return sSttsDao.findAll();
    }
}
