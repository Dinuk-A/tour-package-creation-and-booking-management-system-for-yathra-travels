package lk.yathra.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingStatusController {
    
    @Autowired
    private BookingStatusDao bookSttsDao;

    @GetMapping(value = "/bookingstatus/alldata" , produces="application/json")
    public List<BookingStatus> getAllBookingStatusData(){
        return bookSttsDao.findAll();
    }

}
