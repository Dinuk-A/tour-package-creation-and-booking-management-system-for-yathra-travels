package lk.yathra.transport;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "vehiclename")
    private String vehiclename;

    @Column(name = "licencenumber")
    private String licencenumber;

    @Column(name = "regdate")
    private LocalDateTime regdate;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "modal")
    private String modal;

    @Column(name = "year")
    private Integer year;

    @Column(name = "discription")
    private String discription;

    @Column(name = "seatcount")
    private Integer seatcount;

    @Column(name = "iscompanyown")
    private Boolean iscompanyown;

    @Column(name = "agencyname")
    private String agencyname;

    @Column(name = "agencycontactnum")
    private String agencycontactnum;

    @ManyToOne
    @JoinColumn(name = "vehitype_id", referencedColumnName = "id")
    private VehiType vehitype_id;

    @ManyToOne
    @JoinColumn(name = "vehistatus_id", referencedColumnName = "id")
    private VehiStatus vehistatus_id;

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
