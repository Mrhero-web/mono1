package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 治疗技师对应表
 */
@Schema(description = "治疗技师对应表")
@Entity
@Table(name = "therapist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Therapist implements Serializable {

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
     * 职工编号
     */
    @Schema(description = "职工编号")
    @Column(name = "cure_id")
    private Long cureId;

    /**
     * 职工名称
     */
    @Schema(description = "职工名称")
    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Therapist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCureName() {
        return this.cureName;
    }

    public Therapist cureName(String cureName) {
        this.setCureName(cureName);
        return this;
    }

    public void setCureName(String cureName) {
        this.cureName = cureName;
    }

    public Long getCureId() {
        return this.cureId;
    }

    public Therapist cureId(Long cureId) {
        this.setCureId(cureId);
        return this;
    }

    public void setCureId(Long cureId) {
        this.cureId = cureId;
    }

    public String getName() {
        return this.name;
    }

    public Therapist name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Therapist)) {
            return false;
        }
        return id != null && id.equals(((Therapist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Therapist{" +
            "id=" + getId() +
            ", cureName='" + getCureName() + "'" +
            ", cureId=" + getCureId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
