package lk.yathra.stay;

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
@Table(name = "stay")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "address")
    @NotNull
    private String address;

    @Column(name = "contactnum1")
    @NotNull
    private String contactnum1;

    @Column(name = "contactnum2")
    private String contactnum2;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "maxguestscount")
    private Integer maxguestscount;

    @Column(name = "base_price")
    private BigDecimal base_price;

    @Column(name = "incremental_cost")
    private BigDecimal incremental_cost;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private District district_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "staystatus_id", referencedColumnName = "id")
    private StayStatus staystatus_id;

    @ManyToOne
    @JoinColumn(name = "staytype_id", referencedColumnName = "id")
    private StayType staytype_id;

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

    public Stay(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
