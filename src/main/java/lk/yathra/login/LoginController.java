package lk.yathra.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.user.User;
import lk.yathra.user.UserDao;

@RestController
public class LoginController {

    @Autowired
    private UserDao uDao;

    @GetMapping(value = "/login")
    public ModelAndView loginUI() {
        ModelAndView loginView = new ModelAndView();
        loginView.setViewName("login.html");
        return loginView;
    }

    @GetMapping(value = "/dashboard")
    public ModelAndView dashboardUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //dan log wela inna kenage details methana thiyanawa

        User loggedUser = uDao.getByUName(auth.getName());
       

        ModelAndView dbView = new ModelAndView();
        dbView.setViewName("dashboard.html");
        dbView.addObject("loggedusername", auth.getName());

        // roles godak thiyana nisa list eke palawni eka witharay enne
        // dbView.addObject("loggeduserrole", loggedUser.getRoles().iterator().next().getName());

        // dbView.addObject("loggeduserphoto", loggedUser.getUser_photo());
        dbView.addObject("title", "Yathra Dashboard");

        return dbView;
    }

    @GetMapping(value = "/error")
    public ModelAndView errorUi() {
        ModelAndView errorView = new ModelAndView();
        errorView.setViewName("error.html");
        return errorView;
    }
}
