package lk.yathra.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.*;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "empnumber")
    private String empnumber;

    @Column(name = "nic")
    private String nic;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "landno")
    private String landno;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @Column(name = "gender")
    private String gender;

    @Column(name = "martialstatus")
    private String martialstatus;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "emp_photo")
    private byte[] emp_photo;

    @Column(name = "emp_photo_name")
    private String emp_photo_name;

    @ManyToOne
    @JoinColumn(name = "designation_id", referencedColumnName = "id")
    private Designation designation_id;

    @ManyToOne
    @JoinColumn(name = "employeestatus_id", referencedColumnName = "id")
    private EmployeeStatus employeestatus_id;

    // common 6
    @Column(name = "addeddatetime")
    private LocalDateTime addeddatetime;

    @Column(name = "lastmodifieddatetime")
    private LocalDateTime lastmodifieddatetime;

    @Column(name = "deleteddatetime")
    private LocalDateTime deleteddatetime;

    @Column(name = "addeduserid")
    private Integer addeduserid;

    @Column(name = "lastmodifieduserid")
    private Integer lastmodifieduserid;

    @Column(name = "deleteduserid")
    private Integer deleteduserid;

}
