package lk.yathra.expenses;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesDao extends JpaRepository<Expenses, Integer> {
    
}
