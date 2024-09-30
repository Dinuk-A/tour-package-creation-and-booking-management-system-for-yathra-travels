package lk.yathra;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.yathra.employee.EmployeeDao;
import lk.yathra.user.Role;
import lk.yathra.user.RoleDao;
import lk.yathra.user.User;
import lk.yathra.user.UserDao;

@SpringBootApplication
@RestController
public class YathraApplication {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private RoleDao rDao;

	@Autowired
	private UserDao uDao;

	public static void main(String[] args) {
		SpringApplication.run(YathraApplication.class, args);

		start();
	}

	public static void start() {
		System.out.println("********************");
		System.out.println("appliation started");
		System.out.println("********************");
	}

	@GetMapping(value = "/createadmin")
	public String generateFirstAcc() {

		User adminUser = new User();
		adminUser.setUsername("Admin");
		adminUser.setEmail("admindinuka101@yathra.com");
		adminUser.setPassword(bCryptPasswordEncoder.encode("12345"));
		adminUser.setStatus(true);
		adminUser.setAddeddatetime(LocalDateTime.now());

		adminUser.setEmployee_id(empDao.getReferenceById(1));

		Set<Role> roles = new HashSet<Role>();
		roles.add(rDao.getReferenceById(1));
		adminUser.setRoles(roles);

		uDao.save(adminUser);

		return "<script>window.location.replace('localhost:8080/login');</script>";

	}

}
