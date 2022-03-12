package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.PatientType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 病人表
 */
@Schema(description = "病人表")
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户编号
     */
    @Schema(description = "用户编号", required = true)
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 住院编号
     */
    @Schema(description = "住院编号")
    @Column(name = "hospital_id")
    private Long hospitalId;

    /**
     * 姓名
     */
    @Schema(description = "姓名", required = true)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 性别
     */
    @Schema(description = "性别")
    @Column(name = "gender")
    private String gender;

    /**
     * 年龄
     */
    @Schema(description = "年龄")
    @Column(name = "age")
    private Integer age;

    /**
     * 出生日期
     */
    @Schema(description = "出生日期")
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * 身份证号
     */
    @Schema(description = "身份证号", required = true)
    @NotNull
    @Column(name = "id_num", nullable = false)
    private String idNum;

    /**
     * 电话号
     */
    @Schema(description = "电话号")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 入院日期
     */
    @Schema(description = "入院日期")
    @Column(name = "admission_date")
    private Instant admissionDate;

    /**
     * 出院日期
     */
    @Schema(description = "出院日期")
    @Column(name = "discharged_date")
    private Instant dischargedDate;

    /**
     * 入院科室
     */
    @Schema(description = "入院科室")
    @Column(name = "admission_department_id")
    private Long admissionDepartmentId;

    /**
     * 住院天数
     */
    @Schema(description = "住院天数")
    @Column(name = "days_in_hospital")
    private Integer daysInHospital;

    /**
     * 住院医师
     */
    @Schema(description = "住院医师")
    @Column(name = "hospital_physician")
    private String hospitalPhysician;

    /**
     * 诊疗技师
     */
    @Schema(description = "诊疗技师")
    @Column(name = "therapist")
    private String therapist;

    /**
     * 入院方式
     */
    @Schema(description = "入院方式")
    @Column(name = "admission_method")
    private String admissionMethod;

    /**
     * 现在科室编号
     */
    @Schema(description = "现在科室编号", required = true)
    @NotNull
    @Column(name = "current_department_id", nullable = false)
    private Long currentDepartmentId;

    /**
     * 病人类型
     */
    @Schema(description = "病人类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "patient_type")
    private PatientType patientType;

    /**
     * 是否住院系统导入
     */
    @Schema(description = "是否住院系统导入", required = true)
    @NotNull
    @Column(name = "from_hospital_system", nullable = false)
    private Boolean fromHospitalSystem;

    /**
     * 登录名
     */
    @Schema(description = "登录名")
    @Column(name = "login", unique = true)
    private String login;

    /**
     * 登录密码
     */
    @Schema(description = "登录密码")
    @Column(name = "password")
    private String password;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Patient userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHospitalId() {
        return this.hospitalId;
    }

    public Patient hospitalId(Long hospitalId) {
        this.setHospitalId(hospitalId);
        return this;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return this.name;
    }

    public Patient name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public Patient gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return this.age;
    }

    public Patient age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public Patient birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIdNum() {
        return this.idNum;
    }

    public Patient idNum(String idNum) {
        this.setIdNum(idNum);
        return this;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Patient phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getAdmissionDate() {
        return this.admissionDate;
    }

    public Patient admissionDate(Instant admissionDate) {
        this.setAdmissionDate(admissionDate);
        return this;
    }

    public void setAdmissionDate(Instant admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Instant getDischargedDate() {
        return this.dischargedDate;
    }

    public Patient dischargedDate(Instant dischargedDate) {
        this.setDischargedDate(dischargedDate);
        return this;
    }

    public void setDischargedDate(Instant dischargedDate) {
        this.dischargedDate = dischargedDate;
    }

    public Long getAdmissionDepartmentId() {
        return this.admissionDepartmentId;
    }

    public Patient admissionDepartmentId(Long admissionDepartmentId) {
        this.setAdmissionDepartmentId(admissionDepartmentId);
        return this;
    }

    public void setAdmissionDepartmentId(Long admissionDepartmentId) {
        this.admissionDepartmentId = admissionDepartmentId;
    }

    public Integer getDaysInHospital() {
        return this.daysInHospital;
    }

    public Patient daysInHospital(Integer daysInHospital) {
        this.setDaysInHospital(daysInHospital);
        return this;
    }

    public void setDaysInHospital(Integer daysInHospital) {
        this.daysInHospital = daysInHospital;
    }

    public String getHospitalPhysician() {
        return this.hospitalPhysician;
    }

    public Patient hospitalPhysician(String hospitalPhysician) {
        this.setHospitalPhysician(hospitalPhysician);
        return this;
    }

    public void setHospitalPhysician(String hospitalPhysician) {
        this.hospitalPhysician = hospitalPhysician;
    }

    public String getTherapist() {
        return this.therapist;
    }

    public Patient therapist(String therapist) {
        this.setTherapist(therapist);
        return this;
    }

    public void setTherapist(String therapist) {
        this.therapist = therapist;
    }

    public String getAdmissionMethod() {
        return this.admissionMethod;
    }

    public Patient admissionMethod(String admissionMethod) {
        this.setAdmissionMethod(admissionMethod);
        return this;
    }

    public void setAdmissionMethod(String admissionMethod) {
        this.admissionMethod = admissionMethod;
    }

    public Long getCurrentDepartmentId() {
        return this.currentDepartmentId;
    }

    public Patient currentDepartmentId(Long currentDepartmentId) {
        this.setCurrentDepartmentId(currentDepartmentId);
        return this;
    }

    public void setCurrentDepartmentId(Long currentDepartmentId) {
        this.currentDepartmentId = currentDepartmentId;
    }

    public PatientType getPatientType() {
        return this.patientType;
    }

    public Patient patientType(PatientType patientType) {
        this.setPatientType(patientType);
        return this;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }

    public Boolean getFromHospitalSystem() {
        return this.fromHospitalSystem;
    }

    public Patient fromHospitalSystem(Boolean fromHospitalSystem) {
        this.setFromHospitalSystem(fromHospitalSystem);
        return this;
    }

    public void setFromHospitalSystem(Boolean fromHospitalSystem) {
        this.fromHospitalSystem = fromHospitalSystem;
    }

    public String getLogin() {
        return this.login;
    }

    public Patient login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public Patient password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", hospitalId=" + getHospitalId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            ", age=" + getAge() +
            ", birthday='" + getBirthday() + "'" +
            ", idNum='" + getIdNum() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", dischargedDate='" + getDischargedDate() + "'" +
            ", admissionDepartmentId=" + getAdmissionDepartmentId() +
            ", daysInHospital=" + getDaysInHospital() +
            ", hospitalPhysician='" + getHospitalPhysician() + "'" +
            ", therapist='" + getTherapist() + "'" +
            ", admissionMethod='" + getAdmissionMethod() + "'" +
            ", currentDepartmentId=" + getCurrentDepartmentId() +
            ", patientType='" + getPatientType() + "'" +
            ", fromHospitalSystem='" + getFromHospitalSystem() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
