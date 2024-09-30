package lk.yathra.inquiry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InquiryDao extends JpaRepository<Inquiry, Integer> {

    // create an unique code for every Inquiry
    @Query(value = "SELECT concat('INQ', lpad(substring(max(inq.inqcode),4)+1 , 5 , 0))  as inqcode FROM yathra.inquiry as inq;", nativeQuery = true)
    public String getNextInquiryCode();

    // GET ONLY THE NEW INQUIRIES
    @Query(value = "select inq from Inquiry inq where inq.inquirystatus.id=1")
    public List<Inquiry> getOnlyNewInquiries();

    // GET ONLY THE IN-PROGRESS INQUIRIES
    @Query(value = "select inq from Inquiry inq where inq.inquirystatus.id=2")
    public List<Inquiry> getOnlyInProgressInquiries();

    // GET INQUIRIES ASCENDING ORDER BY STATUS >>>NEW,IN-PROGRESS,...ETC
    @Query(value = "select inq from Inquiry inq order by inq.inquirystatus.id asc")
    public List<Inquiry> findAllDataByStatus();

    @Query(value = "select inq from Inquiry inq where inq.inquirystatus.id <> 1 and inq.inquirystatus.id <> 2 order by inq.inquirystatus.id asc")
    public List<Inquiry> getAllInqsExceptNewAndInProgress();

    // get only NEW and INPROGRESS inqs
    @Query(value = "select inq from Inquiry inq where inq.inquirystatus.id=1 or inq.inquirystatus.id=2 ")
    public List<Inquiry> getNewAndInprogressInqList();

    @Query(value = "select inq from Inquiry inq where inq.inqcode=?1")
    public Inquiry getByCode(String basedinquiry);

}
