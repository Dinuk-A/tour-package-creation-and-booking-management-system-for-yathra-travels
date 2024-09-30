package lk.yathra.stay;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StayStatusDao extends JpaRepository<StayStatus, Integer> {
    
}
