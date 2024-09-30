package lk.yathra.booking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingDao extends JpaRepository<Booking, Integer> {

    // create an unique code for every Inquiry

    // @Query(value = "SELECT concat('BKID', lpad(substring(max(bk.bookingcode),4)+1
    // , 5 , 0)) as bookingcode FROM yathra.booking as bk;", nativeQuery = true)
    // public String getNextBookingCode();

    @Query(value = "SELECT CONCAT('BK', LPAD(COALESCE(MAX(CAST(SUBSTRING(bk.bookingcode, 3) AS UNSIGNED)), 0) + 1, 6, '0')) AS bookingcode FROM yathra.booking AS bk", nativeQuery = true)
    public String getNextBookingCode();

    // @Query(value = "SELECT CONCAT('BKID',
    // LPAD(IFNULL(MAX(CAST(SUBSTRING(bk.bookingcode, 5) AS UNSIGNED)) + 1, 1), 5,
    // '0')) AS bookingcode "
    // +
    // "FROM booking bk", nativeQuery = true)
    // String getNextBookingCode();

    // get only PENDING bookings
    @Query(value = "select bk from Booking bk where bk.bookingstatus_id.id=1 ")
    public List<Booking> getPendngBookingList();

}
