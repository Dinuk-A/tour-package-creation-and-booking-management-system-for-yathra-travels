package lk.yathra.payment;

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
import lk.yathra.booking.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //auto gen
    @Column(name = "paycode")
    // @NotNull
    private String paycode;

    //how much cx paid this time
    @Column(name = "paidamount")
    private BigDecimal paidamount;

    @Column(name = "note")
    private String note;

    // pay slip or ss
    @Column(name = "trx_proof")
    private byte[] trx_proof;

    // common 6
    @Column(name = "addeddate")
    private LocalDate addeddate;

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

    //to which particular booking do we make this payment
    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking_id;

    @ManyToOne
    @JoinColumn(name = "paymentstatus_id", referencedColumnName = "id")
    private PaymentStatus paymentstatus_id;

}
