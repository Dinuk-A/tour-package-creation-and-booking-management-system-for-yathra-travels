package lk.yathra.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentStatusController {

    @Autowired
    private PaymentStatusDao pmntSttsDao;

    @GetMapping(value = "/paymentstatus/alldata", produces = "application/json")
    public List<PaymentStatus> getPaymentStatusAllData() {
        return pmntSttsDao.findAll();
    }

}
