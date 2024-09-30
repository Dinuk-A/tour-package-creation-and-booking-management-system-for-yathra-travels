package lk.yathra.tourpackage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lk.yathra.dayplans.DayPlan;
import lk.yathra.transport.VehiType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tourpackage")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "packagename")
    private String packagename;

    @Column(name = "packagecode")
    private String packagecode;
    
    //this package is created for this inq
    @Column(name = "basedinquiry")
    private String basedinquiry;

    @Column(name = "totaldayscount")
    private Integer totaldayscount;

    @Column(name = "tourstartdate")
    private LocalDate tourstartdate;

    @Column(name = "tourenddate")
    private LocalDate tourenddate;

    // ####TRAVELLERS COUNT STARTS#####
    @Column(name = "localadultcount")
    private Integer localadultcount;

    @Column(name = "localchildcount")
    private Integer localchildcount;

    @Column(name = "foreignadultcount")
    private Integer foreignadultcount;

    @Column(name = "foreignchildcount")
    private Integer foreignchildcount;
    // ####TRAVELLERS COUNT ENDS####

    // TO CALC VEHICLE COST STARTS
    @ManyToOne
    @JoinColumn(name = "vehitype_id", referencedColumnName = "id")
    private VehiType vehicletype;

    @Column(name = "totalkmcountofpkg")
    private BigDecimal totalkmcountofpkg;
    // TO CALC VEHICLE COST ENDS

    // #### SUM OF COSTS STARTS#####
    @Column(name = "totaltktcostforall")
    private BigDecimal totaltktcostforall;

    @Column(name = "totallunchcost")
    private BigDecimal totallunchcost;

    @Column(name = "totalvehiparkingcost")
    private BigDecimal totalvehiparkingcost;

    @Column(name = "totalvehiclecost")
    private BigDecimal totalvehiclecost;

    @Column(name = "totalstaycost")
    private BigDecimal totalstaycost;

    @Column(name = "othercosts")
    private BigDecimal othercosts;

    @Column(name = "onewbcost")
    private BigDecimal onewbcost;

    @Column(name = "totaladditionalcosts")
    private BigDecimal totaladditionalcosts;

    @Column(name = "pkgtotalcost")
    private BigDecimal pkgtotalcost;
    // #### SUM OF COSTS ENDS#####    

    //FINAL PRICE    
    @Column(name = "pkgprice")
    private BigDecimal pkgprice;
    
    @Column(name = "advanceamount")
    private BigDecimal advanceamount;

    // for internal use
    @Column(name = "note")
    private String note;

    @Column(name = "othercostreason")
    private String othercostreason;

    // to show in website
    @Column(name = "description")
    private String description;

    @Column(name = "img1")
    private byte[] img1;

    @Column(name = "img2")
    private byte[] img2;

    @Column(name = "img3")
    private byte[] img3;

    @Column(name = "img4")
    private byte[] img4;

    @Column(name = "iscustompkg")
    private Boolean iscustompkg;

    // why????????
    @OneToMany(mappedBy = "tourpackage_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourPackageHasDayPlan> tourpkgHasDaypPlanList;

    @ManyToOne
    @JoinColumn(name = "tourpackagestatus_id", referencedColumnName = "id")
    private TourPackageStatus tourpackagestatus_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sd_dayplan_id", referencedColumnName = "id")
    private DayPlan sd_dayplan_id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ed_dayplan_id", referencedColumnName = "id")
    private DayPlan ed_dayplan_id;

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

    /*
    ADDITIONAL COSTS MEKATA LINK KARANNA
     */

}
