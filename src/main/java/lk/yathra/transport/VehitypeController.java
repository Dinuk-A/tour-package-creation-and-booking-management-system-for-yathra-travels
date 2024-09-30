package lk.yathra.transport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehitypeController {

    @Autowired
    private VehicleTypeDao vTypeDao;

    @GetMapping(value = "/vtypes/alldata" ,produces = "application/json")
    public List <VehiType> getVTypeAllData(){
        return vTypeDao.findAll();
    }

    // to add a new option to select tag
    @PostMapping(value = "/vtypes")
     public String saveVTypes(@RequestBody VehiType vtype){

        //auth one na vehicle eka athule inna nisa
        //duplicate nam aniwa one

        try {
            vTypeDao.save(vtype);
            return "OK";
        } catch (Exception e) {
            return "save not complete";
        }
     }
    
}
