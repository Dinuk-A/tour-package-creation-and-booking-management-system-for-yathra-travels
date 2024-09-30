package lk.yathra.sightseeing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttrCategoryController {
 
    @Autowired
    private AttrCategoryDao attrCatDao;
    
    @GetMapping(value = "/category/alldata" , produces = "application/json")
    public List <AttrCategory> getCategories(){
        return attrCatDao.findAll();
    }





    
}
