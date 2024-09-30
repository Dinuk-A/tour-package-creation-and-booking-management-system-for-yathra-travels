package lk.yathra.dayplans;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LunchHotelStatusController {

   @Autowired
   private LunchHotelStatusDao lhSttsDao;

   @GetMapping(value = "/lhstts/alldata" , produces = "application/json")
   public List<LunchHotelStatus> getHotelStatusAllData(){
    return lhSttsDao.findAll();
   }
}
