package lk.yathra.inquiry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InquiryResponseDao extends JpaRepository<InquiryResponse, Integer> {

    @Query("select ir from InquiryResponse ir where ir.inquiry_id.id=?1")
    List<InquiryResponse> byinquiry(int inquid);


    
}
