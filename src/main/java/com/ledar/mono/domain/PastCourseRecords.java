package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 历史课程记录
 */
@Schema(description = "历史课程记录")
@Entity
@Table(name = "past_course_records")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PastCourseRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 课程编号
     */
    @Schema(description = "课程编号")
    @Column(name = "c_id")
    private Long cId;

    /**
     * 教师编号
     */
    @Schema(description = "教师编号")
    @Column(name = "t_id")
    private Long tId;

    /**
     * 教室编号
     */
    @Schema(description = "教室编号")
    @Column(name = "r_id")
    private Long rId;

    /**
     * 上课日期
     */
    @Schema(description = "上课日期")
    @Column(name = "class_date")
    private Instant classDate;

    /**
     * 上课时间
     */
    @Schema(description = "上课时间")
    @Column(name = "school_time")
    private Instant schoolTime;

    /**
     * 下课时间
     */
    @Schema(description = "下课时间")
    @Column(name = "class_time")
    private Instant classTime;

    /**
     * 课程状态
     */
    @Schema(description = "课程状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "c_status")
    private CourseStatus cStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PastCourseRecords id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getcId() {
        return this.cId;
    }

    public PastCourseRecords cId(Long cId) {
        this.setcId(cId);
        return this;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Long gettId() {
        return this.tId;
    }

    public PastCourseRecords tId(Long tId) {
        this.settId(tId);
        return this;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Long getrId() {
        return this.rId;
    }

    public PastCourseRecords rId(Long rId) {
        this.setrId(rId);
        return this;
    }

    public void setrId(Long rId) {
        this.rId = rId;
    }

    public Instant getClassDate() {
        return this.classDate;
    }

    public PastCourseRecords classDate(Instant classDate) {
        this.setClassDate(classDate);
        return this;
    }

    public void setClassDate(Instant classDate) {
        this.classDate = classDate;
    }

    public Instant getSchoolTime() {
        return this.schoolTime;
    }

    public PastCourseRecords schoolTime(Instant schoolTime) {
        this.setSchoolTime(schoolTime);
        return this;
    }

    public void setSchoolTime(Instant schoolTime) {
        this.schoolTime = schoolTime;
    }

    public Instant getClassTime() {
        return this.classTime;
    }

    public PastCourseRecords classTime(Instant classTime) {
        this.setClassTime(classTime);
        return this;
    }

    public void setClassTime(Instant classTime) {
        this.classTime = classTime;
    }

    public CourseStatus getcStatus() {
        return this.cStatus;
    }

    public PastCourseRecords cStatus(CourseStatus cStatus) {
        this.setcStatus(cStatus);
        return this;
    }

    public void setcStatus(CourseStatus cStatus) {
        this.cStatus = cStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PastCourseRecords)) {
            return false;
        }
        return id != null && id.equals(((PastCourseRecords) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PastCourseRecords{" +
            "id=" + getId() +
            ", cId=" + getcId() +
            ", tId=" + gettId() +
            ", rId=" + getrId() +
            ", classDate='" + getClassDate() + "'" +
            ", schoolTime='" + getSchoolTime() + "'" +
            ", classTime='" + getClassTime() + "'" +
            ", cStatus='" + getcStatus() + "'" +
            "}";
    }
}
