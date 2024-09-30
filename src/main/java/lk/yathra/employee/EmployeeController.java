package lk.yathra.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.transaction.Transactional;
import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.Role;
import lk.yathra.user.RoleDao;
import lk.yathra.user.User;
import lk.yathra.user.UserDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
//import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeDao dao;

    @Autowired
    private EmployeeStatusDao empStatusDao;

    @Autowired
    private UserDao uDao;

    @Autowired
    private RoleDao rdao;

    @Autowired
    private BCryptPasswordEncoder bcrpwdencdr;

    /*
     * private PrivilegeController prvcntrler = new PrivilegeController();
     * AUTOWIRED NATHUWA MEHEMA DAMMOTH ERRORS ENAWA
     */

    @Autowired
    private PrivilegeController prvcntrler;

    // PARANA WIDIYA ::: mapping for return employee UI [/emp]
    @RequestMapping(value = "/emp", method = RequestMethod.GET)
    public ModelAndView empUi() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView empView = new ModelAndView(); // create modalandview obj for return a ui
        empView.setViewName("employee.html"); // set view name
        empView.addObject("loggedusername", auth.getName());
        empView.addObject("title", "Yathra Employee");

        return empView;
    }

    // define mapping for get ALL EMPLOYEE DATA [/emp/alldata]
    @GetMapping(value = "/emp/alldata", produces = "application/JSON")
    public List<Employee> getEmployeeAllData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege =
        prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EMPLOYEE");

        if (!loggedUserPrivilege.getPrivselect()) {
        return new ArrayList<Employee>();
        }

        return dao.findAll(Sort.by(Direction.DESC, "id"));
    }

    // get available driver list
    @GetMapping(value = "emp/availabledriver/{startDate}/{endDate}", produces = "application/JSON")
    public List<Employee> getAvailableDriverEmps(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return dao.getAvailableDriversList(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // get available driver list
    @GetMapping(value = "emp/availableguide/{startDate}/{endDate}", produces = "application/JSON")
    public List<Employee> getAvailableGuideEmps(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return dao.getAvailableGuidesList(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // mapping for SAVE employee data
    @PostMapping(value = "/emp")
    @Transactional
    public String saveEmployee(@RequestBody Employee employee) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EMPLOYEE");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        // check duplications
        Employee isEmpExistByNic = dao.getEmpByNic(employee.getNic());
        if (isEmpExistByNic != null) {
            return "An Employee With This NIC Already Exists";
        }

        Employee isExistEmpByEmail = dao.getEmpByEmail(employee.getEmail());
        if (isExistEmpByEmail != null) {
            return "This Email Is Already Exists";
        }

        Employee isEmpExistByMobile = dao.getEmpByMobileNum(employee.getMobile());
        if (isEmpExistByMobile != null) {
            return "Save not completed, This Mobile Number Already Exists";
        }

        try {
            // need to set auto generated values
            employee.setAddeddatetime(LocalDateTime.now());
            employee.setAddeduserid(uDao.getByUName(auth.getName()).getId());

            String empNextNum = dao.getEmpNextNum();

            // emp data mukuth nathuwa nam ,,initial value ekak apima denawa
            // (empNextNum==(null) || empNextNum.isEmpty())
            if (empNextNum.equals(null) || empNextNum.equals("")) {
                employee.setEmpnumber("0000001");
            } else {
                employee.setEmpnumber(dao.getEmpNextNum());
            }

            Employee savedNewEmployee = dao.save(employee);

            // DEPENDANCIES (add new user acc if required)
            if (employee.getDesignation_id().getUseraccount()) {

                User newUser = new User();
                newUser.setUsername(employee.getEmpnumber());
                newUser.setPassword(bcrpwdencdr.encode(employee.getNic()));
                newUser.setEmail(employee.getEmail());
                newUser.setAddeddatetime(LocalDateTime.now());
                newUser.setStatus(true);
                newUser.setEmployee_id(savedNewEmployee);
                // newUser.setUser_photo(savedNewEmployee.getEmp_photo());
                // newUser.setUser_photo_name(savedNewEmployee.getEmp_photo_name());
                // newUser.setuser_photo(savedNewEmployee.getEmp_photo());

                // newUser.setuser_photo_name(savedNewEmployee.getEmp_photo_name());

                // role set ekata instance ekak haduwa, userta roles one nisa
                // role obj ekak gennuwa role table eken, designation ekata related wena
                // eka add krnwa role set ekata

                Set<Role> userRole = new HashSet<>(); // create empty role obj
                Role newRoleObj = rdao.getByRoleName(employee.getDesignation_id().getName());
                userRole.add(newRoleObj);
                newUser.setRoles(userRole);

                uDao.save(newUser);
            }

            return "OK";
        } catch (Exception e) {

            return "Save Not Completed: " + e.getMessage();
        }

    }

    // PUT MAPPING for update an existing emp recoed [/emp]
    @PutMapping(value = "/emp")
    @Transactional
    public String updateEmpRecord(@RequestBody Employee employee) {

        // authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EMPLOYEE");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update Not Completed : You Dont Have Permission";
        }

        // check existing
        Employee isEmpExist = dao.getReferenceById(employee.getId()); // dao balanawa me id eken rec ekak db eke
                                                                      // thiynwads kiyala
        if (isEmpExist == null) {
            return "Update Not Completed : Employee Does Not Exists";
        }

        // duplications

        // by mobile
        Employee isEmpExistByMobile = dao.getEmpByMobileNum(employee.getMobile());
        if (isEmpExistByMobile != null) {
            return "Update not completed, This Mobile Number Already Exists";
        }

        // check email
        Employee isEmpExistByEmail = dao.getEmpByEmail(employee.getEmail());                                                                           
        if (isEmpExistByEmail != null && isEmpExistByEmail.getId() != employee.getId()) {
            return "Update Not Completed, This Email Already Exists";
        }

        // check nic duplications
        Employee isEmpExistByNIC = dao.getEmpByNic(employee.getNic());
        if (isEmpExistByNIC != null && isEmpExistByNIC.getId() != employee.getId()) {
            return "Update Not Completed, This NIC Already Exists";
        }

        try {
            employee.setLastmodifieddatetime(LocalDateTime.now());
            employee.setLastmodifieduserid(uDao.getByUName(auth.getName()).getId());
            dao.save(employee);

            // status eka maru unama acc eka inactive wenawa
            if (employee.getEmployeestatus_id().getName().equals("Resigned")
                    || employee.getEmployeestatus_id().getName().equals("Deleted")) {
                User existUser = uDao.getByEmployee(employee.getId());

                if (existUser != null) {
                    existUser.setStatus(false);
                    uDao.save(existUser);
                }
            }

            if (employee.getEmployeestatus_id().getName().equals("Working")) {
                User existUser = uDao.getByEmployee(employee.getId());

                if (existUser != null) {
                    existUser.setStatus(true);
                    uDao.save(existUser);
                }
            }

            return "OK";
        } catch (Exception e) {
            return "Update Not Completed Because :" + e.getMessage();
        }
    }

    // create get mapping for get employees who doesnt have USER ACCS
    @GetMapping(value = "/emp/listwithoutuseracc", produces = "application/json")
    public List<Employee> getEmpWOUserAccs() {
        return dao.getListUsersWithoutAccount();
    }

    @DeleteMapping(value = "/emp")
    @Transactional // delet eeka athule transaction 2k wena nisa , ewa wena wenama manage karanna
    // mapping for DELETE records [/emp]

    public String deleteEmployee(@RequestBody Employee emp) {

        // authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "EMPLOYEE");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        // check existence
        Employee isEmpExist = dao.getReferenceById(emp.getId());
        if (isEmpExist == null) {
            return "Delete Not Completed , Employee Not Exist";
        }
        try {

            isEmpExist.setDeleteddatetime(LocalDateTime.now());
            isEmpExist.setDeleteduserid(uDao.getByUName(auth.getName()).getId());

            // HARD DELETE (NOT RECOMMENDED 💥)
            // dao.delete(emp); // meka wada kale naththan ilaga line eka
            // dao.delete(dao.getReferenceById(emp.getId()));

            // soft delete
            EmployeeStatus deleteStatus = empStatusDao.getReferenceById(3);
            isEmpExist.setEmployeestatus_id(deleteStatus);
            dao.save(isEmpExist);

            // dependancies

            // need to change user acc if acc is exist
            User existUser = uDao.getByEmployee(isEmpExist.getId());

            // user acc eka thiyanawa nam
            if (existUser != null) {
                // extser.setnote("because acc is disabled")
                existUser.setStatus(false);
                uDao.save(existUser);
            }

            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed " + e.getMessage();
        }

    }

}
