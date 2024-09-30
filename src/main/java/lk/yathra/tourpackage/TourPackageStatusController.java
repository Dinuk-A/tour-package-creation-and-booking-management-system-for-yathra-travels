package lk.yathra.tourpackage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TourPackageStatusController {

    @Autowired
    private TourPackageStatusDao tpSttsDao;

    @GetMapping(value = "/tpstatus/alldata", produces = "application/json")
    public List<TourPackageStatus> getAttrStatusAllData() {
        return tpSttsDao.findAll();
    }
}
