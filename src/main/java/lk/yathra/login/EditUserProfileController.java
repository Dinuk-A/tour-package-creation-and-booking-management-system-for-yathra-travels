package lk.yathra.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lk.yathra.user.User;
import lk.yathra.user.UserDao;

@RestController
public class EditUserProfileController {

    @Autowired
    private UserDao uDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "/loggeduser", produces = "application/json")
    public EditUser getLoggedUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = uDao.getByUName(auth.getName());

        EditUser newEditUser = new EditUser();

        newEditUser.setId(loggedUser.getId());
        newEditUser.setUsername(loggedUser.getUsername());
        newEditUser.setEmail(loggedUser.getEmail());
        newEditUser.setCurrentpw(loggedUser.getPassword());

        return newEditUser;
    }

    @PutMapping(value = "/edituserinfo")
    public String editUserInfoPortal(@RequestBody EditUser editUser) {
        try {
            User existingUser = uDao.getReferenceById(editUser.getId());

            // Validate current password
            if (!bCryptPasswordEncoder.matches(editUser.getCurrentpw(), existingUser.getPassword())) {
                return "Invalid current password";
            }

            // Check if new password is provided
            if (editUser.getNewpw() != null) {
                // Update password
                existingUser.setPassword(bCryptPasswordEncoder.encode(editUser.getNewpw()));
            }

            // Update other user details (username, email, etc.)
            existingUser.setUsername(editUser.getUsername());
            existingUser.setEmail(editUser.getEmail());
            // ... other fields to update

            uDao.save(existingUser);
            return "OK";
        } catch (Exception e) {
            return "Profile update failed " + e.getMessage();
        }
    }

}
