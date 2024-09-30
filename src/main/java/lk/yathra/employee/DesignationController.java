package lk.yathra.employee;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class DesignationController {
    
    @Autowired
    private DesignationDao desDao;

    @GetMapping(value ="/des/alldata" , produces = "application/json")
    public List<Designation> getDesignations (){
        
        return desDao.findAll();
    }
    
}
