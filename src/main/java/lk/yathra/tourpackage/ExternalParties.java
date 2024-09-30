package lk.yathra.tourpackage;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "externaldriverorguide")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalParties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "nic")
    private String nic;

    @Column(name = "agencyname")
    private String agencyname;

    @Column(name = "roletype")
    private String roletype;

    @Column(name = "contactone")
    private String contactone;

    @Column(name = "extagencycontact")
    private String extagencycontact;

    @Column(name = "chargeperday")
    private BigDecimal chargeperday;

    @ManyToOne
    @JoinColumn(name = "extdorg_status_id", referencedColumnName = "id")
    private EPStatus extstatus_id;

    @Column(name = "note")
    private String note;

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
