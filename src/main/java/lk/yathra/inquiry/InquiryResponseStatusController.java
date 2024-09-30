package lk.yathra.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InquiryResponseStatusController {
    @Autowired
    private InquiryResponseStatusDao inqRespSttsDao;

    @GetMapping(value = "/inqresponsestatus/alldata", produces = "application/json")
    public List<InquiryResponseStatus> getInquiryStatusAllData() {
        return inqRespSttsDao.findAll();
    }
}
