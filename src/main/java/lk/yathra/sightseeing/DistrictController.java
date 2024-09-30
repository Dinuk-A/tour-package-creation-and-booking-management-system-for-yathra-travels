package lk.yathra.sightseeing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistrictController {

    @Autowired
    private DistrictDao dDao;

    // mapping for get all district data
    @GetMapping(value = "district/alldata", produces = "application/json")
    public List<District> getDistrictAllData() {
        return dDao.findAll();
    }

    @GetMapping(value = "/district/getdistrictbyprovince/{provinceid}")
    public List <District> getDistByprovince (@PathVariable Integer provinceid){
        return dDao.getDistrictByProvince(provinceid);
    }


}
