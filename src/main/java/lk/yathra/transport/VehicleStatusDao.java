package lk.yathra.transport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleStatusDao extends JpaRepository<VehiStatus,Integer> {
    
}
