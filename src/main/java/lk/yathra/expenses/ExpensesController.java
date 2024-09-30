package lk.yathra.expenses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ExpensesController {

    @Autowired
    private ExpensesDao expDao;

    @RequestMapping(value = "/expenses", method = RequestMethod.GET)
    public ModelAndView expensesUI() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView expensesView = new ModelAndView();
        expensesView.setViewName("expenses.html");
        expensesView.addObject("loggedusername", auth.getName());
        expensesView.addObject("title", "Yathra Espenses");
        return expensesView;
    }

    @GetMapping(value = "/expenses/alldata", produces = "application/JSON")
    public List<Expenses> getAllExpensesData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return expDao.findAll(Sort.by(Direction.DESC, "id"));
    }

}
