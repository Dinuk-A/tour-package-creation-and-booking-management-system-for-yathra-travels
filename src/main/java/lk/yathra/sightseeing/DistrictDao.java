package lk.yathra.sightseeing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DistrictDao extends JpaRepository<District, Integer>{

    @Query(value = "select dis from District dis where dis.province_id.id=?1")
    public List<District> getDistrictByProvince(Integer provinceID);
    
} 
