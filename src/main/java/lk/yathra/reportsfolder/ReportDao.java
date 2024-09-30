package lk.yathra.reportsfolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.yathra.inquiry.Inquiry;

public interface ReportDao extends JpaRepository<Inquiry, Integer> {

    // @Query(value = "SELECT count(*) FROM yathra.inquiry as inq where
    // inq.addeddatetime between ?1 and ?2" , nativeQuery = true)
    // @Query(value = "SELECT * FROM yathra.inquiry as inq WHERE inq.addeddatetime
    // BETWEEN ?1 AND ?2 OR inq.recieveddatetime BETWEEN ?1 AND ?2", nativeQuery =
    // true)
    // public long getAllInquiriesBetweenGivenDays(LocalDate starDate, LocalDate
    // endDate);

    // get the count of all inquiries
    @Query(value = "SELECT count(*) FROM yathra.inquiry as inq WHERE inq.addeddatetime BETWEEN ?1 AND ?2", nativeQuery = true)
    public long countInquiriesByGivenDays(LocalDate startDate, LocalDate endDate);

    // only get count of succeded inqs
    @Query(value = "SELECT count(*) FROM yathra.inquiry as inq where inq.addeddatetime BETWEEN ?1 AND ?2 and inq.inquiry_status_id = 4 ", nativeQuery = true)
    public long countConfirmedInquiries(LocalDate startDate, LocalDate endDate);

    // date range ekakadi hambuna total payment sum eka
    @Query(value = "SELECT sum(y.paidamount) FROM yathra.payment as y where y.addeddate between ?1 and ?2 ;", nativeQuery = true)
    public BigDecimal findTotalPaidAmountByDateRange(LocalDate startDate, LocalDate endDate);


}
