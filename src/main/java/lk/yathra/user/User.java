package lk.yathra.user;

import java.time.LocalDateTime;
import java.util.Set;


//import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lk.yathra.employee.Employee;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    // @NotNull
    private String password;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "note") 
    private String note;

    @Column(name = "user_photo")
    private byte[] user_photo;

    // @Column(name = "user_photo_name")
    // private String user_photo_name;

    @Column(name = "status")
    @NotNull
    private Boolean status;

    // @Column(name = "photopath")              
    // private String photopath;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee_id;

     @ManyToMany
    @JoinTable (name = "user_has_role" , joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set <Role> roles ;

     // common 6
     @Column(name = "addeddatetime")
     private LocalDateTime addeddatetime;
 
     @Column(name = "lastmodifieddatetime")
     private LocalDateTime lastmodifieddatetime;
 
     @Column(name = "deleteddatetime")
     private LocalDateTime deleteddatetime;
 
    //  @Column(name = "addeduserid")
    //  private Integer addeduserid;
 
    //  @Column(name = "lastmodifieduserid")
    //  private Integer lastmodifieduserid;
 
    //  @Column(name = "deleteduserid")
    //  private Integer deleteduserid;

    //to get username for print
    public User(String username){
        this.username = username;
    }
}
