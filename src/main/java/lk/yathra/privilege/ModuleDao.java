package lk.yathra.privilege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleDao extends JpaRepository<Module , Integer> {
    
    @Query("select m from Module m where m.id not in(select p.module.id from Privilege p where p.role.id=?1)")
    public List<Module> getModulesByRole(Integer roleid);
    

}