package lk.yathra.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientDao extends JpaRepository<Client, Integer> {

    // @Query(value = "select lpad(max(c.clientregno)+1 , 7 ,0) as clientregno from
    // Client c ")
    // public String getNextClientRegNo();

    // @Query(value = "select lpad(max(c.clientregno)+1 , 7 ,0) as clientregno from
    // yathra.client as c", nativeQuery = true)
    // public String getNextClientRegNo();

    @Query(value = "SELECT CONCAT('CL', LPAD(COALESCE(MAX(CAST(SUBSTRING(c.clientregno, 3) AS UNSIGNED)), 0) + 1, 5, '0')) AS clientregno FROM yathra.client AS c", nativeQuery = true)
    public String getNextClientRegNo();

    // get clien by given passport or NIC(both in same field)
    @Query(value = "select c from Client c where c.passportnumornic =?1")
    public Client getClientByPassportOrNIC(String passportnumornic);

}
