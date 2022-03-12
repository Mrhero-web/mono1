package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 评估表
 */
@Schema(description = "评估表")
@Entity
@Table(name = "e_form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
     * 评估类别
     */
    @Schema(description = "评估类别")
    @Enumerated(EnumType.STRING)
    @Column(name = "e_category")
    private Category eCategory;

    /**
     * 职工编号
     */
    @Schema(description = "职工编号")
    @Column(name = "staff_id")
    private Long staffId;

    /**
     * 既往病史
     */
    @Schema(description = "既往病史")
    @Column(name = "e_conclusion")
    private String eConclusion;

    /**
     * 评估时间
     */
    @Schema(description = "评估时间")
    @Column(name = "e_time")
    private Instant eTime;

    /**
     * 评估结论
     */
    @Schema(description = "评估结论")
    @Column(name = "e_illness")
    private String eIllness;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EForm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCureId() {
        return this.cureId;
    }

    public EForm cureId(Long cureId) {
        this.setCureId(cureId);
        return this;
    }

    public void setCureId(Long cureId) {
        this.cureId = cureId;
    }

    public String getIdNum() {
        return this.idNum;
    }

    public EForm idNum(String idNum) {
        this.setIdNum(idNum);
        return this;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public Category geteCategory() {
        return this.eCategory;
    }

    public EForm eCategory(Category eCategory) {
        this.seteCategory(eCategory);
        return this;
    }

    public void seteCategory(Category eCategory) {
        this.eCategory = eCategory;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public EForm staffId(Long staffId) {
        this.setStaffId(staffId);
        return this;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String geteConclusion() {
        return this.eConclusion;
    }

    public EForm eConclusion(String eConclusion) {
        this.seteConclusion(eConclusion);
        return this;
    }

    public void seteConclusion(String eConclusion) {
        this.eConclusion = eConclusion;
    }

    public Instant geteTime() {
        return this.eTime;
    }

    public EForm eTime(Instant eTime) {
        this.seteTime(eTime);
        return this;
    }

    public void seteTime(Instant eTime) {
        this.eTime = eTime;
    }

    public String geteIllness() {
        return this.eIllness;
    }

    public EForm eIllness(String eIllness) {
        this.seteIllness(eIllness);
        return this;
    }

    public void seteIllness(String eIllness) {
        this.eIllness = eIllness;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EForm)) {
            return false;
        }
        return id != null && id.equals(((EForm) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EForm{" +
            "id=" + getId() +
            ", cureId=" + getCureId() +
            ", idNum='" + getIdNum() + "'" +
            ", eCategory='" + geteCategory() + "'" +
            ", staffId=" + getStaffId() +
            ", eConclusion='" + geteConclusion() + "'" +
            ", eTime='" + geteTime() + "'" +
            ", eIllness='" + geteIllness() + "'" +
            "}";
    }
}
