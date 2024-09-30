package lk.yathra.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusDao extends JpaRepository<PaymentStatus, Integer> {

}
