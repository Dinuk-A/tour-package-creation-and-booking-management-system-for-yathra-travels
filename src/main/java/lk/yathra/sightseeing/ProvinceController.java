package lk.yathra.sightseeing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProvinceController {

    @Autowired
    private Provincedao proDao;

    @GetMapping(value = "province/alldata", produces = "application/json")
    public List<Province> getProvinces() {
        return proDao.findAll();
    }
}
