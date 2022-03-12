package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 课程安排
 */
@Schema(description = "课程安排")
@Entity
@Table(name = "list_of_courses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ListOfCourses implements Serializable {

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
     * 患者编号
     */
    @Schema(description = "患者编号")
    @Column(name = "p_id")
    private Long pId;

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
     * 安排类型【寒暑假（T）/日常】
     */
    @Schema(description = "安排类型【寒暑假（T）/日常】")
    @Column(name = "l_type")
    private Boolean lType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ListOfCourses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getcId() {
        return this.cId;
    }

    public ListOfCourses cId(Long cId) {
        this.setcId(cId);
        return this;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Long getpId() {
        return this.pId;
    }

    public ListOfCourses pId(Long pId) {
        this.setpId(pId);
        return this;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long gettId() {
        return this.tId;
    }

    public ListOfCourses tId(Long tId) {
        this.settId(tId);
        return this;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Long getrId() {
        return this.rId;
    }

    public ListOfCourses rId(Long rId) {
        this.setrId(rId);
        return this;
    }

    public void setrId(Long rId) {
        this.rId = rId;
    }

    public Instant getSchoolTime() {
        return this.schoolTime;
    }

    public ListOfCourses schoolTime(Instant schoolTime) {
        this.setSchoolTime(schoolTime);
        return this;
    }

    public void setSchoolTime(Instant schoolTime) {
        this.schoolTime = schoolTime;
    }

    public Instant getClassTime() {
        return this.classTime;
    }

    public ListOfCourses classTime(Instant classTime) {
        this.setClassTime(classTime);
        return this;
    }

    public void setClassTime(Instant classTime) {
        this.classTime = classTime;
    }

    public Boolean getlType() {
        return this.lType;
    }

    public ListOfCourses lType(Boolean lType) {
        this.setlType(lType);
        return this;
    }

    public void setlType(Boolean lType) {
        this.lType = lType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListOfCourses)) {
            return false;
        }
        return id != null && id.equals(((ListOfCourses) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ListOfCourses{" +
            "id=" + getId() +
            ", cId=" + getcId() +
            ", pId=" + getpId() +
            ", tId=" + gettId() +
            ", rId=" + getrId() +
            ", schoolTime='" + getSchoolTime() + "'" +
            ", classTime='" + getClassTime() + "'" +
            ", lType='" + getlType() + "'" +
            "}";
    }
}
