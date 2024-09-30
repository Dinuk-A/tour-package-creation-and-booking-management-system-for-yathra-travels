package lk.yathra.employee;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeDao extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT lpad(max(emptbl.empnumber)+1 , 7 , 0) as empnumber FROM yathra.employee as emptbl;", nativeQuery = true)
    public String getEmpNextNum();
    // return type eka string bcz output eka enne CHAR ekak nisa

    // JPQL
    // query for get emp for the given nic METHOD #1
    @Query(value = "select e from Employee e where e.nic=?1")
    // Employee == clz name eka, kalin e table eka me clz ekata link kaapu nisa
    // db eka aye connect karanna oneth na ekath mulu prpject ekatama APPLICATION
    // PROPERTIES walin link karapu nisa
    public Employee getEmpByNic(String nic);

    // query for get emp for the given email ANOTHER METHOD
    @Query(value = "select e from Employee e where e.email=:email")
    public Employee getEmpByEmail(@Param("email") String email);

    @Query(value = "Select e from Employee e where e.mobile=?1")
    public Employee getEmpByMobileNum(String mobile);

    @Query(value = "select e from Employee e where e.id not in (select u.employee_id.id from User u)")
    public List<Employee> getListUsersWithoutAccount();

    // check availability of driver
    @Query(value = "SELECT emp FROM Employee emp where emp.designation_id.id=9 and emp.id not in(select b.driver.id from Booking b where (b.startdate between ?1 and ?2 or b.enddate between ?1 and ?2) and (b.bookingstatus_id.id <> 2 or b.bookingstatus_id.id<> 4))")
    public List<Employee> getAvailableDriversList(LocalDate startDate, LocalDate endDate);

    //check availability of guides
    @Query(value = "SELECT emp FROM Employee emp where emp.designation_id.id=10 and emp.id not in(select b.driver.id from Booking b where (b.startdate between ?1 and ?2 or b.enddate between ?1 and ?2) and (b.bookingstatus_id.id <> 2 or b.bookingstatus_id.id<> 4))")
    public List<Employee> getAvailableGuidesList(LocalDate startDate, LocalDate endDate);

    // SELECT emp.id , emp.empnumber FROM yathra.employee as emp where
    // emp.designation_id = 6 and emp.id not in(select b.driver from yathra.booking
    // as b where (b.starrtdate between '2024-08-01' and '2024-08-05' or b.enddate
    // between '2024-08-01' and '2024-08-05') and (b.bookingstatus_id!= 2 or
    // b.bookingstatus_id!= 4) );

}
