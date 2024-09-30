package lk.yathra.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lk.yathra.inquiry.Inquiry;
import lk.yathra.inquiry.Nationality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.*;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "clientname")
    private String clientname;

    @Column(name = "clienttitle")
    private String clienttitle;

    @Column(name = "clientregno")
    // @NotNull
    private String clientregno;

    @Column(name = "passportnumornic")
    private String passportnumornic;

    @Column(name = "contactnum")
    private String contactnum;

    @Column(name = "contactnumtwo")
    private String contactnumtwo;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

    @ManyToOne
    @JoinColumn(name = "inquiry_id", referencedColumnName = "id")
    private Inquiry inquiry_id;

    @ManyToOne
    @JoinColumn(name = "clientstatus_id", referencedColumnName = "id")
    private ClientStatus clientstatus;

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
