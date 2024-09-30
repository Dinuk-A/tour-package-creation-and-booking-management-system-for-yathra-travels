package lk.yathra.transport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehiStatusController {
    
    @Autowired
    private VehicleStatusDao vStatusDao;

    @GetMapping(value = "/vstatus/alldata" , produces = "application/json")
    public List<VehiStatus> getVStatusData(){
        return vStatusDao.findAll();
    }
}
