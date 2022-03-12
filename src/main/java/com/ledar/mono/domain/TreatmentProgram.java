package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 治疗项目
 */
@Schema(description = "治疗项目")
@Entity
@Table(name = "treatment_program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TreatmentProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TreatmentProgram id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCureName() {
        return this.cureName;
    }

    public TreatmentProgram cureName(String cureName) {
        this.setCureName(cureName);
        return this;
    }

    public void setCureName(String cureName) {
        this.cureName = cureName;
    }

    public String getNorms() {
        return this.norms;
    }

    public TreatmentProgram norms(String norms) {
        this.setNorms(norms);
        return this;
    }

    public void setNorms(String norms) {
        this.norms = norms;
    }

    public String getUnit() {
        return this.unit;
    }

    public TreatmentProgram unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCharge() {
        return this.charge;
    }

    public TreatmentProgram charge(String charge) {
        this.setCharge(charge);
        return this;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Float getPrice() {
        return this.price;
    }

    public TreatmentProgram price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreatmentProgram)) {
            return false;
        }
        return id != null && id.equals(((TreatmentProgram) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TreatmentProgram{" +
            "id=" + getId() +
            ", cureName='" + getCureName() + "'" +
            ", norms='" + getNorms() + "'" +
            ", unit='" + getUnit() + "'" +
            ", charge='" + getCharge() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
