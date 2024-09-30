package lk.yathra.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<Role, Integer>{
    
    //meka one wenne USER ekata
    //create query for get roles list without admin 
    //!= is also <> 
    @Query(value = "select r from Role r where r.name <> 'Admin' ")  
    public List<Role> getRolesExceptAdmin();

    @Query(value = "select r from Role r where r.name = ?1")
    public Role getByRoleName(String name);  
}
