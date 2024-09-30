package lk.yathra.expenses;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesStatusDao extends JpaRepository<ExpenseStatus, Integer> {

}
