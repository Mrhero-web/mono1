package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 当天排程记录详情
 */
@Schema(description = "当天排程记录详情")
@Entity
@Table(name = "schedule_record_details_now")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScheduleRecordDetailsNow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 可是名称
     */
    @Schema(description = "可是名称")
    @Column(name = "d_name")
    private String dName;

    /**
     * 患者编号
     */
    @Schema(description = "患者编号")
    @Column(name = "cure_id")
    private Long cureId;

    /**
     * 医嘱编号
     */
    @Schema(description = "医嘱编号")
    @Column(name = "medical_number")
    private Long medicalNumber;

    /**
     * 科室编号
     */
    @Schema(description = "科室编号")
    @Column(name = "d_num")
    private Long dNum;

    /**
     * 身份证编号
     */
    @Schema(description = "身份证编号")
    @Column(name = "id_num")
    private String idNum;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScheduleRecordDetailsNow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getdName() {
        return this.dName;
    }

    public ScheduleRecordDetailsNow dName(String dName) {
        this.setdName(dName);
        return this;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public Long getCureId() {
        return this.cureId;
    }

    public ScheduleRecordDetailsNow cureId(Long cureId) {
        this.setCureId(cureId);
        return this;
    }

    public void setCureId(Long cureId) {
        this.cureId = cureId;
    }

    public Long getMedicalNumber() {
        return this.medicalNumber;
    }

    public ScheduleRecordDetailsNow medicalNumber(Long medicalNumber) {
        this.setMedicalNumber(medicalNumber);
        return this;
    }

    public void setMedicalNumber(Long medicalNumber) {
        this.medicalNumber = medicalNumber;
    }

    public Long getdNum() {
        return this.dNum;
    }

    public ScheduleRecordDetailsNow dNum(Long dNum) {
        this.setdNum(dNum);
        return this;
    }

    public void setdNum(Long dNum) {
        this.dNum = dNum;
    }

    public String getIdNum() {
        return this.idNum;
    }

    public ScheduleRecordDetailsNow idNum(String idNum) {
        this.setIdNum(idNum);
        return this;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleRecordDetailsNow)) {
            return false;
        }
        return id != null && id.equals(((ScheduleRecordDetailsNow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleRecordDetailsNow{" +
            "id=" + getId() +
            ", dName='" + getdName() + "'" +
            ", cureId=" + getCureId() +
            ", medicalNumber=" + getMedicalNumber() +
            ", dNum=" + getdNum() +
            ", idNum='" + getIdNum() + "'" +
            "}";
    }
}
