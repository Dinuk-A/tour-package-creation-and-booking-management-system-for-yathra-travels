package lk.yathra.reportsfolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.user.UserDao;

@RestController
public class ReportUIController {
    @Autowired
    private UserDao uDao;

    // inq success rate
    @RequestMapping(value = "/reportsuccessinqs", method = RequestMethod.GET)
    public ModelAndView reportInqSuccessUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView inqSuccessReportView = new ModelAndView(); // create modalandview obj for return a ui
        inqSuccessReportView.setViewName("reportinqsuccess.html"); // set view name
        inqSuccessReportView.addObject("loggedusername", auth.getName());
        inqSuccessReportView.addObject("title", "Yathra Reports");

        return inqSuccessReportView;
    }

    // total packages booked count
    @RequestMapping(value = "/reportpackagebookcount", method = RequestMethod.GET)
    public ModelAndView reportPkgBookcount() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView pkgBookedCountReportView = new ModelAndView(); // create modalandview obj for return a ui
        pkgBookedCountReportView.setViewName("reportpkgbookings.html"); // set view name
        pkgBookedCountReportView.addObject("loggedusername", auth.getName());
        pkgBookedCountReportView.addObject("title", "Yathra Reports");

        return pkgBookedCountReportView;
    }

    // sum of all payments
    @RequestMapping(value = "/reportpaymentsum", method = RequestMethod.GET)
    public ModelAndView reportPaymentSum() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView paymentSumReportView = new ModelAndView(); // create modalandview obj for return a ui
        paymentSumReportView.setViewName("reportshowpaymentsum.html"); // set view name
        paymentSumReportView.addObject("loggedusername", auth.getName());
        paymentSumReportView.addObject("title", "Yathra Reports");

        return paymentSumReportView;
    }

}
