package lk.yathra.tourpackage;

import java.util.List;

// import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

public interface TourPackageDao extends JpaRepository<TourPackage, Integer> {

    @Query(value = "SELECT concat('TP', lpad(substring(max(tp.packagecode),3)+1 , 5 , 0))  as packagecode FROM yathra.tourpackage as tp;", nativeQuery = true)
    public String nextTPCode();

    //get active tourpkgs that made to show on website
    @Query(value = "SELECT * FROM yathra.tourpackage as tpkg where tpkg.iscustompkg=0 and tpkg.tourpackagestatus_id=1;", nativeQuery = true)
    public List<TourPackage> getPkgsToShowWebsite();

    //get only custom packages
    @Query(value = "SELECT * FROM yathra.tourpackage as tpkg where tpkg.iscustompkg=1 and tpkg.tourpackagestatus_id=1;", nativeQuery = true)
    public List<TourPackage> getCustomPkgs();




}
//     @Query(value = "SELECT lpad(max(tp.packagecode)+1 , 5 , 0) as packagecode FROM yathra.tourpackage as tp;", nativeQuery = true)
//     public String nextTPCode();
// }
