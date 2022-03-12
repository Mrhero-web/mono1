package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 评估分项表
 */
@Schema(description = "评估分项表")
@Entity
@Table(name = "e_item_1")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EItem1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 评估分项结果编号
     */
    @Schema(description = "评估分项结果编号")
    @Column(name = "e_item_result")
    private Long eItemResult;

    /**
     * 评估字段
     */
    @Schema(description = "评估字段")
    @Column(name = "e_z_1")
    private String eZ1;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EItem1 id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long geteItemResult() {
        return this.eItemResult;
    }

    public EItem1 eItemResult(Long eItemResult) {
        this.seteItemResult(eItemResult);
        return this;
    }

    public void seteItemResult(Long eItemResult) {
        this.eItemResult = eItemResult;
    }

    public String geteZ1() {
        return this.eZ1;
    }

    public EItem1 eZ1(String eZ1) {
        this.seteZ1(eZ1);
        return this;
    }

    public void seteZ1(String eZ1) {
        this.eZ1 = eZ1;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EItem1)) {
            return false;
        }
        return id != null && id.equals(((EItem1) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EItem1{" +
            "id=" + getId() +
            ", eItemResult=" + geteItemResult() +
            ", eZ1='" + geteZ1() + "'" +
            "}";
    }
}
