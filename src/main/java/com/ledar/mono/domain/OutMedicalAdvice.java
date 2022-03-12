package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.State;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 历史医嘱表
 */
@Schema(description = "历史医嘱表")
@Entity
@Table(name = "out_medical_advice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OutMedicalAdvice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 治疗编号
     */
    @Schema(description = "治疗编号")
    @Column(name = "cure_number")
    private Long cureNumber;

    /**
     * 治疗名称
     */
    @Schema(description = "治疗名称")
    @Column(name = "cure_name")
    private String cureName;

    /**
     * 规格说明
     */
    @Schema(description = "规格说明")
    @Column(name = "norms")
    private String norms;

    /**
     * 计量单位
     */
    @Schema(description = "计量单位")
    @Column(name = "unit")
    private String unit;

    /**
     * 住院收费方式
     */
    @Schema(description = "住院收费方式")
    @Column(name = "charge")
    private String charge;

    /**
     * 住院标准金额
     */
    @Schema(description = "住院标准金额")
    @Column(name = "price")
    private Float price;

    /**
     * 使用数量
     */
    @Schema(description = "使用数量")
    @Column(name = "use_number")
    private Integer useNumber;

    /**
     * 信息录入职工编号
     */
    @Schema(description = "信息录入职工编号")
    @Column(name = "staff_id")
    private Long staffId;

    /**
     * 患者编号
     */
    @Schema(description = "患者编号")
    @Column(name = "cure_id")
    private Long cureId;

    /**
     * 患者身份证
     */
    @Schema(description = "患者身份证")
    @Column(name = "id_num")
    private String idNum;

    /**
     * 处方医生
     */
    @Schema(description = "处方医生")
    @Column(name = "start_doctor")
    private Long startDoctor;

    /**
     * 处方科室
     */
    @Schema(description = "处方科室")
    @Column(name = "start_department")
    private String startDepartment;

    /**
     * 核算科室
     */
    @Schema(description = "核算科室")
    @Column(name = "nurse_department")
    private String nurseDepartment;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
    @Column(name = "start_time")
    private Instant startTime;

    /**
     * 停止时间
     */
    @Schema(description = "停止时间")
    @Column(name = "stop_time")
    private Instant stopTime;

    /**
     * 核对确认
     */
    @Schema(description = "核对确认")
    @Column(name = "nurse_confirmation")
    private Long nurseConfirmation;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    /**
     * 此系统录入
     */
    @Schema(description = "此系统录入")
    @Column(name = "this_system")
    private Boolean thisSystem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OutMedicalAdvice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCureNumber() {
        return this.cureNumber;
    }

    public OutMedicalAdvice cureNumber(Long cureNumber) {
        this.setCureNumber(cureNumber);
        return this;
    }

    public void setCureNumber(Long cureNumber) {
        this.cureNumber = cureNumber;
    }

    public String getCureName() {
        return this.cureName;
    }

    public OutMedicalAdvice cureName(String cureName) {
        this.setCureName(cureName);
        return this;
    }

    public void setCureName(String cureName) {
        this.cureName = cureName;
    }

    public String getNorms() {
        return this.norms;
    }

    public OutMedicalAdvice norms(String norms) {
        this.setNorms(norms);
        return this;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    public String getUnit() {
        return this.unit;
    }

    public OutMedicalAdvice unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCharge() {
        return this.charge;
    }

    public OutMedicalAdvice charge(String charge) {
        this.setCharge(charge);
        return this;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Float getPrice() {
        return this.price;
    }

    public OutMedicalAdvice price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getUseNumber() {
        return this.useNumber;
    }

    public OutMedicalAdvice useNumber(Integer useNumber) {
        this.setUseNumber(useNumber);
        return this;
    }

    public void setUseNumber(Integer useNumber) {
        this.useNumber = useNumber;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public OutMedicalAdvice staffId(Long staffId) {
        this.setStaffId(staffId);
        return this;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getCureId() {
        return this.cureId;
    }

    public OutMedicalAdvice cureId(Long cureId) {
        this.setCureId(cureId);
        return this;
    }

    public void setCureId(Long cureId) {
        this.cureId = cureId;
    }

    public String getIdNum() {
        return this.idNum;
    }

    public OutMedicalAdvice idNum(String idNum) {
        this.setIdNum(idNum);
        return this;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public Long getStartDoctor() {
        return this.startDoctor;
    }

    public OutMedicalAdvice startDoctor(Long startDoctor) {
        this.setStartDoctor(startDoctor);
        return this;
    }

    public void setStartDoctor(Long startDoctor) {
        this.startDoctor = startDoctor;
    }

    public String getStartDepartment() {
        return this.startDepartment;
    }

    public OutMedicalAdvice startDepartment(String startDepartment) {
        this.setStartDepartment(startDepartment);
        return this;
    }

    public void setStartDepartment(String startDepartment) {
        this.startDepartment = startDepartment;
    }

    public String getNurseDepartment() {
        return this.nurseDepartment;
    }

    public OutMedicalAdvice nurseDepartment(String nurseDepartment) {
        this.setNurseDepartment(nurseDepartment);
        return this;
    }

    public void setNurseDepartment(String nurseDepartment) {
        this.nurseDepartment = nurseDepartment;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public OutMedicalAdvice startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getStopTime() {
        return this.stopTime;
    }

    public OutMedicalAdvice stopTime(Instant stopTime) {
        this.setStopTime(stopTime);
        return this;
    }

    public void setStopTime(Instant stopTime) {
        this.stopTime = stopTime;
    }

    public Long getNurseConfirmation() {
        return this.nurseConfirmation;
    }

    public OutMedicalAdvice nurseConfirmation(Long nurseConfirmation) {
        this.setNurseConfirmation(nurseConfirmation);
        return this;
    }

    public void setNurseConfirmation(Long nurseConfirmation) {
        this.nurseConfirmation = nurseConfirmation;
    }

    public State getState() {
        return this.state;
    }

    public OutMedicalAdvice state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Boolean getThisSystem() {
        return this.thisSystem;
    }

    public OutMedicalAdvice thisSystem(Boolean thisSystem) {
        this.setThisSystem(thisSystem);
        return this;
    }

    public void setThisSystem(Boolean thisSystem) {
        this.thisSystem = thisSystem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutMedicalAdvice)) {
            return false;
        }
        return id != null && id.equals(((OutMedicalAdvice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutMedicalAdvice{" +
            "id=" + getId() +
            ", cureNumber=" + getCureNumber() +
            ", cureName='" + getCureName() + "'" +
            ", norms='" + getNorms() + "'" +
            ", unit='" + getUnit() + "'" +
            ", charge='" + getCharge() + "'" +
            ", price=" + getPrice() +
            ", useNumber=" + getUseNumber() +
            ", staffId=" + getStaffId() +
            ", cureId=" + getCureId() +
            ", idNum='" + getIdNum() + "'" +
            ", startDoctor=" + getStartDoctor() +
            ", startDepartment='" + getStartDepartment() + "'" +
            ", nurseDepartment='" + getNurseDepartment() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", stopTime='" + getStopTime() + "'" +
            ", nurseConfirmation=" + getNurseConfirmation() +
            ", state='" + getState() + "'" +
            ", thisSystem='" + getThisSystem() + "'" +
            "}";
    }
}
