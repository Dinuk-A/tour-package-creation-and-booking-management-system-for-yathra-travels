package lk.yathra.dayplans;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LunchHotelDao extends JpaRepository<LunchHotel,Integer> {
    
     @Query(value = "select lh from LunchHotel lh where lh.district_id.id=?1")
     public List<LunchHotel> getLunchHotelsByGivenDistrict(Integer givenDistrict);
}
