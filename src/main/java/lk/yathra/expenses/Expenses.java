package lk.yathra.expenses;

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
import lk.yathra.booking.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "billnumber")
    private String billnumber;

    @Column(name = "exp_type")
    private String exp_type;

    @Column(name = "paymentmethod")
    private String paymentmethod;

    @Column(name = "paidamount")
    private BigDecimal paidamount;

    @Column(name = "paiddate")
    private LocalDate paiddate;

    // trx_proof blob
    // @Column(name = "emp_photo")
    // private byte[] emp_photo;

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

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking_id;

    @ManyToOne
    @JoinColumn(name = "exp_status_id", referencedColumnName = "id")
    private ExpenseStatus exp_status_id;

}
