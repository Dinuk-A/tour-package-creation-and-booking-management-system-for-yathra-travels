package lk.yathra.dayplans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

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
import jakarta.validation.constraints.NotNull;
import lk.yathra.sightseeing.Attraction;
import lk.yathra.sightseeing.District;
import lk.yathra.stay.Stay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dayplan")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DayPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "daytitle")
    @NotNull
    private String daytitle;

    @Column(name = "dayplancode")
    @NotNull
    private String dayplancode;

    @Column(name = "startlocation")
    private String startlocation;

    @Column(name = "endlocation")
    private String endlocation;

    @Column(name = "note")
    private String note;

    //this dayplan is created for this inq...not the whole object, just the name
    @Column(name = "dpbasedinq")
    private String dpbasedinq;

    // ****TICKETS COSTS STARTS
    @Column(name = "totalforeignadultticketcost")
    private BigDecimal totalforeignadultticketcost;

    @Column(name = "totalforeignchildticketcost")
    private BigDecimal totalforeignchildticketcost;

    @Column(name = "totallocaladultticketcost")
    private BigDecimal totallocaladultticketcost;

    @Column(name = "totallocalchildticketcost")
    private BigDecimal totallocalchildticketcost;
    // ****TICKETS COSTS ENDS

    @Column(name = "kmcountforday")
    private BigDecimal kmcountforday;

    @Column(name = "vehiparkingfeeforday")
    private BigDecimal vehiparkingfeeforday;

    //total cost for today modification
    @Column(name = "totallocostfortoday")
    private BigDecimal totallocostfortoday;

    @ManyToOne
    @JoinColumn(name = "start_district_id", referencedColumnName = "id")
    private District start_district_id;

    @ManyToOne
    @JoinColumn(name = "end_district_id", referencedColumnName = "id")
    private District end_district_id;

    @ManyToOne
    @JoinColumn(name = "start_stay_id", referencedColumnName = "id")
    private Stay start_stay_id;

    @ManyToOne
    @JoinColumn(name = "end_stay_id", referencedColumnName = "id")
    private Stay end_stay_id;

    @ManyToOne
    @JoinColumn(name = "lunch_hotel_id", referencedColumnName = "id")
    private LunchHotel lunch_hotel_id;

    @ManyToOne
    @JoinColumn(name = "dpstatus_id", referencedColumnName = "id")
    private DayPlanStatus dpstatus_id;

    @ManyToMany
    @JoinTable(name = "dayplan_has_attraction", joinColumns = @JoinColumn(name = "dayplan_id"), inverseJoinColumns = @JoinColumn(name = "attraction_id"))
    private Set<Attraction> vplaces;

    // common 6
    @Column(name = "addeddatetime")
    @NotNull
    private LocalDateTime addeddatetime;

    @Column(name = "lastmodifieddatetime")
    private LocalDateTime lastmodifieddatetime;

    @Column(name = "deleteddatetime")
    private LocalDateTime deleteddatetime;

    @Column(name = "addeduserid")
    @NotNull
    private Integer addeduserid;

    @Column(name = "lastmodifieduserid")
    private Integer lastmodifieduserid;

    @Column(name = "deleteduserid")
    private Integer deleteduserid;

}
