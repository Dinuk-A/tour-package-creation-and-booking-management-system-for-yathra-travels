package lk.yathra.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InquiryStatusController {
    
    @Autowired
    private InquiryStatusDao inqSttsDao;

    @GetMapping(value = "/inqstatus/alldata" , produces="application/json")
public List<InquiryStatus> getInquiryStatusAllData(){
    return inqSttsDao.findAll();
}
}
