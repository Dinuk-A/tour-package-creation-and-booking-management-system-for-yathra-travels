package lk.yathra.reportsfolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lk.yathra.inquiry.Inquiry;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ReportDataController {

    @Autowired
    private ReportDao repDao;

    // @GetMapping(value = "report/inqsuccessrate/{startDate}/{endDate}", produces =
    // "application/json")
    // public long LgetAllInqsRecieved(@PathVariable("startDate") String startDate,
    // @PathVariable("endDate") String endDate) {
    // return repDao.countInquiriesBetweenGivenDays(LocalDate.parse(startDate),
    // LocalDate.parse(endDate));
    // }

    // all recieved inquiries
    @GetMapping(value = "report/allinqsbygivendate/{startDate}/{endDate}", produces = "application/json")
    public long countInqsRecieved(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return repDao.countInquiriesByGivenDays(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // all success inquiries
    @GetMapping(value = "report/confirmedinqs/{startDate}/{endDate}", produces = "application/json")
    public long countSuccessInqs(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return repDao.countConfirmedInquiries(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    //pkg booked count
    

    // sum off all payments recieved by gieven time period
    @GetMapping("report/sumofpayments/{startDate}/{endDate}")
    public BigDecimal getSumOfPaymentsByGivenDate(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return repDao.findTotalPaidAmountByDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

}
