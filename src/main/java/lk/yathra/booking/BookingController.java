package lk.yathra.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.employee.Employee;
import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.UserDao;

@RestController
public class BookingController {

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private BookingStatusDao bkSttsDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    public ModelAndView bookingUi() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView bookingView = new ModelAndView(); // create modalandview obj for return a ui
        bookingView.setViewName("booking.html");
        bookingView.addObject("title", "Yathra Booking");
        bookingView.addObject("loggedusername", auth.getName());
        return bookingView;
    }

    // define mapping for get ALL EMPLOYEE DATA [/emp/alldata]
    @GetMapping(value = "/booking/alldata", produces = "application/JSON")
    public List<Booking> getBookingAllData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "BOOKING");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Booking>();
        }

        return bookingDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/booking/pendings", produces = "application/json")
    public List<Booking> getPendingBookings() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "BOOKING");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Booking>();
        }

        return bookingDao.getPendngBookingList();
    }

    @PostMapping(value = "/booking")
    public String saveBookingRecord(@RequestBody Booking booking) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "BOOKING");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        try {
            String nextBookingCode = bookingDao.getNextBookingCode();

            if (nextBookingCode == null || nextBookingCode.equals("")) {
                booking.setBookingcode("BKID00001");
            } else {
                booking.setBookingcode(bookingDao.getNextBookingCode());
            }

            bookingDao.save(booking);
            return "OK";

        } catch (Exception e) {
            return "save not completed : " + e.getMessage();
        }
    }

    @PutMapping(value = "/booking")
    public String updateBookingRecord(@RequestBody Booking booking) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "BOOKING");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed You Dont Have Permission";
        }

        try {
            booking.setLastmodifieddatetime(LocalDateTime.now());
            booking.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            bookingDao.save(booking);
            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }
    }

    @DeleteMapping(value = "/booking")
    public String deleteBooking(@RequestBody Booking bookingObj) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "BOOKING");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed You Dont Have Permission";
        }

        Booking existRecord = bookingDao.getReferenceById(bookingObj.getId());
        if (existRecord == null) {
            return "Record Not Found";
        }
        try {
            existRecord.setDeleteddatetime(LocalDateTime.now());
            existRecord.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            BookingStatus deleteStatus = bkSttsDao.getReferenceById(4);
            existRecord.setBookingstatus_id(deleteStatus);

            return "OK";
        } catch (Exception e) {
            return "Delete Not Completed : " + e.getMessage();
        }
    }

}
