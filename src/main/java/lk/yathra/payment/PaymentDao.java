package lk.yathra.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentDao extends JpaRepository<Payment, Integer> {

    // create an unique code for every payment

    @Query(value = "SELECT concat('PID', lpad(substring(max(pmnt.paycode),4)+1 , 5 , 0))  as paycode FROM yathra.payment as pmnt;", nativeQuery = true)

    public String getNextPaymentCode();
}
