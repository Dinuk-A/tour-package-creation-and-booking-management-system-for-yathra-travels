package lk.yathra.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeStatusController {

@Autowired
private EmployeeStatusDao empSttsDao;

@GetMapping(value = "/status/alldata" , produces="application/json")
public List<EmployeeStatus> getEmpStatusAllData(){
    return empSttsDao.findAll();
}
    
}
