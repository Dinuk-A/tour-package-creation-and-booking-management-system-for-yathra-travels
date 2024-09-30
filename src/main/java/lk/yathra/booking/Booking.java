package lk.yathra.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import lk.yathra.client.Client;
import lk.yathra.employee.Employee;
import lk.yathra.inquiry.Inquiry;
import lk.yathra.tourpackage.ExternalParties;
import lk.yathra.tourpackage.TourPackage;
import lk.yathra.transport.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "bookingcode")
    // @NotNull
    private String bookingcode;

    @Column(name = "startdate")
    private LocalDate startdate;

    @Column(name = "enddate")
    private LocalDate enddate;

    @Column(name = "note")
    private String note;

    //how much is the price of the package
    @Column(name = "totalamount")
    private BigDecimal totalamount;

    //get this by calculating the total amount * 40%
    @Column(name = "advance")
    private BigDecimal advance;

    //how much sum of the total payments for this booking 
    @Column(name = "totalpaidamount")
    private BigDecimal totalpaidamount;

    @ManyToOne
    @JoinColumn(name = "inquiry_id", referencedColumnName = "id")
    private Inquiry inquiry_id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client_id;

    //based tpkg
    @ManyToOne
    @JoinColumn(name = "tourpackage_id", referencedColumnName = "id")
    private TourPackage tourpackage_id;

    @ManyToOne
    @JoinColumn(name = "driver", referencedColumnName = "id")
    private Employee driver;

    @ManyToOne
    @JoinColumn(name = "guide", referencedColumnName = "id")
    private Employee guide;

    @ManyToOne
    @JoinColumn(name = "external_driver", referencedColumnName = "id")
    private ExternalParties external_driver;

    @ManyToOne
    @JoinColumn(name = "external_guide", referencedColumnName = "id")
    private ExternalParties external_guide;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle_id;

    @ManyToOne
    @JoinColumn(name = "bookingstatus_id", referencedColumnName = "id")
    private BookingStatus bookingstatus_id;

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
