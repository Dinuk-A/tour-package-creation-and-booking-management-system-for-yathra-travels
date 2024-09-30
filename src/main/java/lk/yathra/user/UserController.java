package lk.yathra.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
//import java.time.LocalDateTime;
import java.util.List;

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

import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;

@RestController
public class UserController {
    @Autowired
    private UserDao dao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PrivilegeController prvcntrler;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView userUi() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView userView = new ModelAndView(); // create modalandview obj for return a ui
        userView.setViewName("user.html"); // set view name
        userView.addObject("username", auth.getName());
        userView.addObject("title", "Yathra User");

        return userView;
    }

    @GetMapping(value = "/user/alldata", produces = "application/json")
    public List<User> getUserAllData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "USER");
        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<User>();
        }

        return dao.findAll(Sort.by(Direction.DESC, "id"));
    }

    @GetMapping(value = "/username/byid/{userid}", produces = "application/json")
    public User getUserById(@PathVariable("userid") int userID) {
        return dao.getUserNameById(userID);
    }

    @PostMapping(value = "/user")
    public String saveUser(@RequestBody User user) {

        // authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "USER");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        // duplications
        User isUserEmailExist = dao.getUserByEmail(user.getEmail());
        if (isUserEmailExist != null) {
            return "User save not completed : Given " + user.getEmail() + " already exist";
        }

        User extUserEmployee = dao.getByEmployee(user.getEmployee_id().getId());
        if (extUserEmployee != null) {
            return "User save not completed : Given Employee alredy ext";
        }

        try {

            // set auto values
            user.setAddeddatetime(LocalDateTime.now());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            // operator
            dao.save(user);

            // dependacies

            return "OK";

        } catch (Exception e) {
            return "Save Not Completed :" + e.getMessage();
        }
    }

    @PutMapping(value = "/user")
    public String updateUser(@RequestBody User user) {

        // authentication and authorization
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "USER");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Update not completed you dont have permission";
        }

        // existing values
        User isUserExist = dao.getReferenceById(user.getId());
        if (isUserExist == null) {
            return "Update not Completed: User not exist...";
        } else {
            if (bCryptPasswordEncoder.matches(user.getPassword(), isUserExist.getPassword())) {
                return "Update not completed : Pw already exists";
            }
        }

        // check password exist
        if (user.getPassword() != null) {
            if (bCryptPasswordEncoder.matches(user.getPassword(), isUserExist.getPassword())) {
                return "Update Not Complete : changed Password Already Exists";
            } else {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
        } else {
            user.setPassword((isUserExist.getPassword()));
        }
        // MEKA SECURITY RISK EKAK NEMEDA?? EKAMA PW EKA WENA KENEK GAWATH THIYANAWA
        // KIYANA EKA ??

        // BITPRJCT EKE THIYENNE WENAMA FN EKAK

        try {
            user.setLastmodifieddatetime(LocalDateTime.now());
            dao.save(user);
            return "OK";
        } catch (Exception e) {
            return "Update not completed" + e.getMessage();

        }
    }

    @DeleteMapping(value = "/user")
    public String deleteUser(@RequestBody User user) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "USER");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        // exist
        User existUser = dao.getReferenceById(user.getId());
        if (existUser == null) {
            return "Delete Not Completed : USER NOT FOUND ";
        }
        try {
            existUser.setDeleteddatetime(LocalDateTime.now());
            existUser.setStatus(false); // setStatus= FROM USER ENTITY'S "STATUS" FIELD, (BOOLEAN)
            dao.save(existUser);
            return "OK";
        } catch (Exception e) {
            return "Delete Not Completed : " + e.getMessage();
        }
    }

}
