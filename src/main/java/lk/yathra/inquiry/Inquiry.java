package lk.yathra.inquiry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.yathra.tourpackage.TourPackage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inquiry")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "clientname")
    private String clientname;

    @Column(name = "clienttitle")
    private String clienttitle;

    @Column(name = "inqcode")
    private String inqcode;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

    @Column(name = "passportnumornic")
    private String passportnumornic;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "contactnum")
    private String contactnum;

    @Column(name = "contactnumtwo")
    private String contactnumtwo;

    @Column(name = "inqtype")
    private String inqtype;

    @Column(name = "recievedmethod")
    private String recievedmethod;

    @Column(name = "recievedcontactoremail")
    private String recievedcontactoremail;

    @Column(name = "recieveddate")
    private LocalDate recieveddate;

    @Column(name = "recievedtime")
    private LocalTime recievedtime;

    @Column(name = "enquiry")
    private String enquiry;

    // ####TRAVELLERS COUNT STARTS#####
    @Column(name = "localadultcount")
    private Integer inqlocaladultcount;

    @Column(name = "localchildcount")
    private Integer inqlocalchildcount;

    @Column(name = "foreignadultcount")
    private Integer inqforeignadultcount;

    @Column(name = "foreignchildcount")
    private Integer inqforeignchildcount;
    // ####TRAVELLERS COUNT ENDS####

    @Column(name = "pickuplocation")
    private String pickuplocation;

    @Column(name = "dropofflocation")
    private String dropofflocation;

    @Column(name = "arrivaldate")
    private LocalDate arrivaldate;

    // @Column(name = "departuredate")
    // private LocalDate departuredate;

    @Column(name = "isguideinclude")
    private Boolean isguideinclude;

    @Column(name = "prefcontactmethod")
    private String prefcontactmethod;

    @Column(name = "isonworking")
    private Boolean isonworking;

    @Column(name = "currentworkinguser")
    private Integer currentworkinguser;

    // this inquiry is created based on this package in website
    @ManyToOne(optional = true)
    @JoinColumn(name = "tourpackage_id", referencedColumnName = "id")
    private TourPackage based_tpkg_id;

    // this pkg is created for this inq
    @ManyToOne(optional = true)
    @JoinColumn(name = "cus_tourpackage_id", referencedColumnName = "id")
    private TourPackage cus_tpkg_id;

    @ManyToOne
    @JoinColumn(name = "inquiry_status_id", referencedColumnName = "id")
    private InquiryStatus inquirystatus;

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
