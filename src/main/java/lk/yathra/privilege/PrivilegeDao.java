package lk.yathra.privilege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;

// chat gpt >>> @Repository
public interface PrivilegeDao extends JpaRepository<Privilege, Integer> {

    // get qurey for get privilege object y given role id and module id
    @Query("select p from Privilege p where p.role.id=?1 and p.module.id=?2")
    Privilege getByRoleModule(Integer roleid, Integer moduleid);

    //"yathra." kali tika ayn kala, 
    //original eka palleha
    @Query(value = "SELECT bit_or(p.privselect) as pri_select , bit_or(p.privinsert) as pri_insert , bit_or(p.privupdate) as pri_update ,bit_or(p.privdelete) as pri_delete FROM privilege as p where p.module_id in (SELECT m.id FROM module as m where m.name =?2) and p.role_id in (SELECT ur.role_id FROM user_has_role as ur where ur.user_id in (SELECT u.id FROM user as u where u.username =?1))", nativeQuery = true)
    public String getPrivilegesByUserAndModuleUsingDaoQuery(String username, String modulename);

    // @Query(value = "SELECT bit_or(p.privselect) as pri_select , bit_or(p.privinsert) as pri_insert , bit_or(p.privupdate) as pri_update ,bit_or(p.privdelete) as pri_delete FROM yathra.privilege as p where p.module_id in (SELECT m.id FROM yathra.module as m where m.name =?2) and p.role_id in (SELECT ur.role_id FROM yathra.user_has_role as ur where ur.user_id in (SELECT u.id FROM yathra.user as u where u.username =?1));", nativeQuery = true)
    // public String getPrivilegesByUserAndModuleUsingDaoQuery(String username, String modulename);

   
   //by chat gpt, worked in sql bench
    // @Query(value = "SELECT " +
    //                "    bit_or(p.privselect) AS pri_select, " +
    //                "    bit_or(p.privinsert) AS pri_insert, " +
    //                "    bit_or(p.privupdate) AS pri_update, " +
    //                "    bit_or(p.privdelete) AS pri_delete " +
    //                "FROM " +
    //                "    yathra.privilege AS p " +
    //                "JOIN " +
    //                "    yathra.module AS m ON p.module_id = m.id " +
    //                "JOIN " +
    //                "    yathra.user_has_role AS ur ON p.role_id = ur.role_id " +
    //                "JOIN " +
    //                "    yathra.user AS u ON ur.user_id = u.id " +
    //                "WHERE " +
    //                "    u.username = :username " +
    //                "    AND m.name = :modulename", nativeQuery = true)
    // String getPrivilegesByUserAndModuleUsingDaoQuery2(@Param("username") String username, @Param("modulename") String modulename);


     /* MEKATH WADA

     * SELECT
     * bit_or(p.privselect) AS pri_select,
     * bit_or(p.privinsert) AS pri_insert,
     * bit_or(p.privupdate) AS pri_update,
     * bit_or(p.privdelete) AS pri_delete
     * FROM
     * yathra.privilege AS p
     * JOIN
     * yathra.module AS m ON p.module_id = m.id
     * JOIN
     * yathra.user_has_role AS ur ON p.role_id = ur.role_id
     * JOIN
     * yathra.user AS u ON ur.user_id = u.id
     * WHERE
     * u.username = 0000029
     * AND m.name = "EMPLOYEE";
     * 
     */

        // @Query(value = "SELECT " +
    // " MAX(p.privselect) as pri_select, " +
    // " MAX(p.privinsert) as pri_insert, " +
    // " MAX(p.privupdate) as pri_update, " +
    // " MAX(p.privdelete) as pri_delete " +
    // "FROM " +
    // " yathra.privilege p " +
    // "JOIN " +
    // " yathra.module m ON p.module_id = m.id " +
    // "JOIN " +
    // " yathra.user_has_role ur ON p.role_id = ur.role_id " +
    // "JOIN " +
    // " yathra.user u ON ur.user_id = u.id " +
    // "WHERE " +
    // " u.username = ?1 " +
    // " AND m.name = ?2 " , nativeQuery = true )
    // String getPrivilegesByUserAndModuleUsingDaoQuery2(String username, String
    // modulename);

}
