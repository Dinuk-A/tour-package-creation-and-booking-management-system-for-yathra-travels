package lk.yathra.transport;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleDao extends JpaRepository<Vehicle, Integer> {

    @Query(value = "select lpad(max(vtbl.vehiclename)+1 , 5 , 0)as vehiclename from yathra.vehicle as vtbl;", nativeQuery = true)
    public String getNextVehiName();

    @Query(value = "select v from Vehicle v where v.licencenumber=?1")
    public Vehicle getVehiByLNum(String licencenumber);

    // get available COMPANY vehicles and vehi.vehitype_id=?3
    @Query(value = "SELECT * FROM vehicle vehi WHERE vehi.iscompanyown=1 AND vehi.vehistatus_id=1 AND vehi.id NOT IN (SELECT b.vehicle_id FROM booking b WHERE (b.startdate BETWEEN ?1 AND ?2 OR b.enddate BETWEEN ?1 AND ?2) AND (b.bookingstatus_id != 2 OR b.bookingstatus_id != 4))", nativeQuery = true)
    public List<Vehicle> getAvailableVehicleList(LocalDate startDate, LocalDate endDate);

    // same butalso filtered by vehi type✅
    @Query(value = "SELECT * FROM vehicle vehi WHERE vehi.iscompanyown=1 AND vehi.vehistatus_id=1 and vehi.vehitype_id=?3 AND vehi.id NOT IN (SELECT b.vehicle_id FROM booking b WHERE (b.startdate BETWEEN ?1 AND ?2 OR b.enddate BETWEEN ?1 AND ?2) AND (b.bookingstatus_id != 2 OR b.bookingstatus_id != 4))", nativeQuery = true)
    public List<Vehicle> getAvailableVehicleListByVehiType(LocalDate startDate, LocalDate endDate,Integer vehiTypeId);

    // get available EXTERNAL vehicles
    @Query(value = "SELECT * FROM yathra.vehicle as vehi where vehi.iscompanyown=0 ", nativeQuery = true)
    public List<Vehicle> getPreviousRentalVehicleList();

    @Query(value = "SELECT * FROM yathra.vehicle as vehi where vehi.iscompanyown=0 and vehi.vehitype_id=?1", nativeQuery = true)
    public List<Vehicle> getPreviousRentalVehicleListByType(Integer vehitypeId);

    // SELECT * FROM yathra.vehicle as vehi where vehi.vehistatus_id=1 and
    // vehi.iscompanyown=1 and vehi.vehitype_id=2 and vehi.id not in(select
    // b.vehicle_id from yathra.booking as b where (b.startdate between '2024-08-01'
    // and '2024-08-05' or b.enddate between '2024-08-01' and '2024-08-05') and
    // (b.bookingstatus_id!= 2 or b.bookingstatus_id!= 4) );

}
