package lk.yathra.inquiry;

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
@Table(name = "inquiryresponse")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class InquiryResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "inquiry_id", referencedColumnName = "id")
    private Inquiry inquiry_id;

    @Column(name = "addeddatetime")
    private LocalDateTime addeddatetime;

    // @Column(name = "lastmodifieddatetime")
    // private LocalDateTime lastmodifieddatetime;

    @Column(name = "addeduserid")
    private Integer addeduserid;

    // @Column(name = "lastmodifieduserid")
    // private Integer lastmodifieduserid;

    @ManyToOne
    @JoinColumn(name = "inq_response_status_id", referencedColumnName = "id")
    private InquiryResponseStatus inqResponseStatus ;

}
