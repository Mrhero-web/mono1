package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 课程-医嘱关联表
 */
@Schema(description = "课程-医嘱关联表")
@Entity
@Table(name = "a_relevance_c")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ARelevanceC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 医嘱项目编号
     */
    @Schema(description = "医嘱项目编号")
    @Column(name = "a_id")
    private Long aId;

    /**
     * 课程编号
     */
    @Schema(description = "课程编号")
    @Column(name = "c_id")
    private Long cId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ARelevanceC id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getaId() {
        return this.aId;
    }

    public ARelevanceC aId(Long aId) {
        this.setaId(aId);
        return this;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public Long getcId() {
        return this.cId;
    }

    public ARelevanceC cId(Long cId) {
        this.setcId(cId);
        return this;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ARelevanceC)) {
            return false;
        }
        return id != null && id.equals(((ARelevanceC) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ARelevanceC{" +
            "id=" + getId() +
            ", aId=" + getaId() +
            ", cId=" + getcId() +
            "}";
    }
}
