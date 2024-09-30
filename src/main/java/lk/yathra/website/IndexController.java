package lk.yathra.website;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.dayplans.DayPlan;
import lk.yathra.dayplans.DayPlanDao;
import lk.yathra.inquiry.Nationality;
import lk.yathra.inquiry.NationalityDao;
import lk.yathra.privilege.Privilege;
import lk.yathra.sightseeing.Attraction;
import lk.yathra.sightseeing.AttractionDao;
import lk.yathra.tourpackage.TourPackage;
import lk.yathra.tourpackage.TourPackageDao;

@RestController
public class IndexController {

    @Autowired
    private AttractionDao attrDao;

    @Autowired
    private DayPlanDao dpDao;

    @Autowired
    private TourPackageDao tPkgDao;

    @Autowired
    private NationalityDao natDao;

    @RequestMapping(value = "/yathra", method = RequestMethod.GET)
    public ModelAndView yathraUI() {
        ModelAndView yathraView = new ModelAndView();
        yathraView.setViewName("yathra.html");
        return yathraView;
    }

    @GetMapping(value = "/attractionforweb/alldata")
    public List<Attraction> getAttrAllDList() {

        return attrDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/dayplanforweb/alldata", produces = "application/JSON")
    public List<DayPlan> getDayPlanAllData() {

        return dpDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/tourpackageforweb/alldata", produces = "application/json")
    public List<TourPackage> getAllTourPackageData() {

        return tPkgDao.getPkgsToShowWebsite();
    }

     @GetMapping(value = "/nationalityforweb/alldata", produces = "application/json")
    public List<Nationality> getNationalityAllData() {
        return natDao.findAll();
    }
    
}
