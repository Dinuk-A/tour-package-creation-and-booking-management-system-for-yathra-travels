package lk.yathra.privilege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuleController {

    @Autowired
    private ModuleDao dao;
    
    //mapping for get all module data
    @GetMapping(value = "/module/alldata" , produces = "application/json")
   public List<Module> getModuleAllData(){
    return dao.findAll();
   }

  // get mapping for get module data by given roleid 

   @GetMapping(value = "/module/listbyrole" , params = {"roleid"})
   public List<Module> getByrole(@RequestParam("roleid") Integer roleid){
    return dao.getModulesByRole(roleid);
   }


   
    }
