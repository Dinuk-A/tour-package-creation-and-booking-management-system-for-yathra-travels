package lk.yathra.tourpackage;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.yathra.dayplans.DayPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tourpackage_has_dayplan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourPackageHasDayPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dayno")
    private Integer dayno;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tourpackage_id", referencedColumnName = "id")
    private TourPackage tourpackage_id;

    @ManyToOne
    @JoinColumn(name = "dayplan_id", referencedColumnName = "id")
    private DayPlan dayplan_id;

}
