package lk.yathra.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NationalityController {
    @Autowired
    private NationalityDao natDao;

    @GetMapping(value = "/nationality/alldata", produces = "application/json")
    public List<Nationality> getNationalityAllData() {
        return natDao.findAll();
    }
}
