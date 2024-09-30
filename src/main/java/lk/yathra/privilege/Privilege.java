package lk.yathra.privilege;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lk.yathra.user.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")    
    private Integer id;

    @Column(name = "privselect")
    @NotNull
    private Boolean privselect;

    @Column(name = "privinsert")
    @NotNull
    private Boolean privinsert;

    @Column(name = "privupdate")
    @NotNull
    private Boolean privupdate;

    @Column(name = "privdelete")
    @NotNull
    private Boolean privdelete;

    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
    
    // common 6
    @Column(name = "addeddatetime")
    private LocalDateTime addeddatetime;

    @Column(name = "lastmodifieddatetime")
    private LocalDateTime lastmodifieddatetime;

    @Column(name = "deleteddatetime")
    private LocalDateTime deleteddatetime;

    // @Column(name = "addeduserid")
    // private Integer addeduserid;

    // @Column(name = "lastmodifieduserid")
    // private Integer lastmodifieduserid;

    // @Column(name = "deleteduserid")
    // private Integer deleteduserid;

     // aluth constructer ekak hadanawa
     public Privilege (Boolean prv_select,Boolean prv_insert, Boolean prv_update, Boolean prv_delete ){
        this.privselect = prv_select ;
        this.privinsert = prv_insert ;
        this.privupdate = prv_update ;
        this.privdelete = prv_delete ;
    }

}
