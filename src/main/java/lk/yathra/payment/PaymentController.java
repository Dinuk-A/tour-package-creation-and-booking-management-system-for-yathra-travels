package lk.yathra.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.booking.Booking;
import lk.yathra.booking.BookingDao;
import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.UserDao;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@RestController
public class PaymentController {

    @Autowired
    private PaymentDao payDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private BookingDao bookingDao;

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public ModelAndView paymentUi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView paymentView = new ModelAndView();
        paymentView.setViewName("payment.html"); // set view name
        paymentView.addObject("loggedusername", auth.getName());
        paymentView.addObject("title", "Yathra Payment");

        return paymentView;
    }

    @GetMapping(value = "/payment/alldata", produces = "application/json")
    public List<Payment> getAllPaymentData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "PAYMENT");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Payment>();
        }

        return payDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @PostMapping(value = "/payment")
    public String savePayment(@RequestBody Payment pay) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "PAYMENT");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {

            String payCodeVar = payDao.getNextPaymentCode();
            if (payCodeVar == null || payCodeVar.equals("")) {
                pay.setPaycode("PAY00001");
            } else {
                pay.setPaycode(payCodeVar);
            }

            pay.setAddeddate(LocalDate.now());
            pay.setAddeduserid(userDao.getByUName(auth.getName()).getId());

            Booking bookingObj = bookingDao.getReferenceById(pay.getBooking_id().getId());

            if (bookingObj == null) {
                return "Booking not found";
            }

            BigDecimal currentTotalPaidAmount;
            if (bookingObj.getTotalpaidamount() != null) {
                currentTotalPaidAmount = bookingObj.getTotalpaidamount();
            } else {
                currentTotalPaidAmount = BigDecimal.ZERO;
            }

            BigDecimal newPaidAmount = currentTotalPaidAmount.add(pay.getPaidamount());
            bookingObj.setTotalpaidamount(newPaidAmount);
            System.out.println(newPaidAmount);

            System.out.println(bookingObj.getTotalpaidamount());
            bookingDao.save(bookingObj);

            payDao.save(pay);
            return "OK";

        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    @PutMapping("/payment")
    public String updateInquiry(@RequestBody Payment pay) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "PAYMENT");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed : You Dont Have Permission";
        }

        try {
            Payment existingPayRecord = payDao.getReferenceById(pay.getId());

            Booking bookingObjPut = bookingDao.getReferenceById(existingPayRecord.getBooking_id().getId());

            if (bookingObjPut == null) {
                return "Booking not found";
            }

            BigDecimal oldPaidAmount = existingPayRecord.getPaidamount();
            BigDecimal newPaidAmount = pay.getPaidamount();
            BigDecimal differenceOfPayments = newPaidAmount.subtract(oldPaidAmount);

            BigDecimal updatedTotalPaidAmount = bookingObjPut.getTotalpaidamount().add(differenceOfPayments);

            /*
             * if (updatedTotalPaidAmount.compareTo(BigDecimal.ZERO) < 0) {
             * updatedTotalPaidAmount = BigDecimal.ZERO;
             * }
             */
            // NEGATIVE WENA EKA NAWATHTHANNA ONNAM

            bookingObjPut.setTotalpaidamount(updatedTotalPaidAmount);

            existingPayRecord.setPaidamount(newPaidAmount);

            existingPayRecord.setLastmodifieddatetime(LocalDateTime.now());
            existingPayRecord.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());

            bookingDao.save(bookingObjPut);
            payDao.save(existingPayRecord);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }

    }
}
