package lk.yathra.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusDao extends JpaRepository<BookingStatus, Integer>{
    
}
