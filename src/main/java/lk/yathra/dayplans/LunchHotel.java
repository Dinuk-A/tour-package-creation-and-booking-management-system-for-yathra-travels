package lk.yathra.dayplans;

import java.math.BigDecimal;
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
import lk.yathra.sightseeing.District;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lunch_hotel")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class LunchHotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    // @NotNull
    private String name;

    @Column(name = "costperhead")
    private BigDecimal costperhead;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private District district_id;

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

    @Column(name = "note")
    private String note;

    @Column(name = "address")
    private String address;

    @Column(name = "contactnum")
    private String contactnum;

    @ManyToOne
    @JoinColumn(name = "lunchhotel_status_id", referencedColumnName = "id")
    private LunchHotelStatus lhotelstts_id;

}
