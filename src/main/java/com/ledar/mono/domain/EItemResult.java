package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 评估分项结果表
 */
@Schema(description = "评估分项结果表")
@Entity
@Table(name = "e_item_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EItemResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 评估分项结果
     */
    @Schema(description = "评估分项结果")
    @Column(name = "e_item_result")
    private String eItemResult;

    /**
     * 评估编号
     */
    @Schema(description = "评估编号")
    @Column(name = "e_number")
    private Long eNumber;

    /**
     * 评估分项编号
     */
    @Schema(description = "评估分项编号")
    @Column(name = "e_item_number")
    private Long eItemNumber;

    /**
     * 评估分项
     */
    @Schema(description = "评估分项")
    @Column(name = "e_subitem")
    private Long eSubitem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EItemResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String geteItemResult() {
        return this.eItemResult;
    }

    public EItemResult eItemResult(String eItemResult) {
        this.seteItemResult(eItemResult);
        return this;
    }

    public void seteItemResult(String eItemResult) {
        this.eItemResult = eItemResult;
    }

    public Long geteNumber() {
        return this.eNumber;
    }

    public EItemResult eNumber(Long eNumber) {
        this.seteNumber(eNumber);
        return this;
    }

    public void seteNumber(Long eNumber) {
        this.eNumber = eNumber;
    }

    public Long geteItemNumber() {
        return this.eItemNumber;
    }

    public EItemResult eItemNumber(Long eItemNumber) {
        this.seteItemNumber(eItemNumber);
        return this;
    }

    public void seteItemNumber(Long eItemNumber) {
        this.eItemNumber = eItemNumber;
    }

    public Long geteSubitem() {
        return this.eSubitem;
    }

    public EItemResult eSubitem(Long eSubitem) {
        this.seteSubitem(eSubitem);
        return this;
    }

    public void seteSubitem(Long eSubitem) {
        this.eSubitem = eSubitem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EItemResult)) {
            return false;
        }
        return id != null && id.equals(((EItemResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EItemResult{" +
            "id=" + getId() +
            ", eItemResult='" + geteItemResult() + "'" +
            ", eNumber=" + geteNumber() +
            ", eItemNumber=" + geteItemNumber() +
            ", eSubitem=" + geteSubitem() +
            "}";
    }
}
