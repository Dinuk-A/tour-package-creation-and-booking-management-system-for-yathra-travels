package lk.yathra.tourpackage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ExternalPartiesDao extends JpaRepository<ExternalParties, Integer> {

    @Query(value = "SELECT * FROM yathra.externaldriverorguide as edg where edg.roletype = 'Driver'", nativeQuery = true)
    public List<ExternalParties> getExternalDriversList();

    @Query(value = "SELECT * FROM yathra.externaldriverorguide as edg where edg.roletype = 'Guide'", nativeQuery = true)
    public List<ExternalParties> getExternalGuidesList();

    // @Query(value = "Select ep from ExternalParties ep where ep.nic=?1")
    // public ExternalParties getExpsByNIC(String nic);
    //select e from Employee e where e.nic=?1

    @Query(value = "SELECT * FROM yathra.externaldriverorguide as ep where ep.nic=?1", nativeQuery = true)
    public ExternalParties getExpsByNIC(String nic);

}
