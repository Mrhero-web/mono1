package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 课程
 */
@Schema(description = "课程")
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 课程名称
     */
    @Schema(description = "课程名称")
    @Column(name = "c_name")
    private String cName;

    /**
     * 课程介绍
     */
    @Schema(description = "课程介绍")
    @Column(name = "c_introduce")
    private String cIntroduce;

    /**
     * 所属科室
     */
    @Schema(description = "所属科室")
    @Column(name = "d_id")
    private Long dId;

    /**
     * 课程类型【单人课（T）/多人课】
     */
    @Schema(description = "课程类型【单人课（T）/多人课】")
    @Column(name = "c_type")
    private Boolean cType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getcName() {
        return this.cName;
    }

    public Course cName(String cName) {
        this.setcName(cName);
        return this;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcIntroduce() {
        return this.cIntroduce;
    }

    public Course cIntroduce(String cIntroduce) {
        this.setcIntroduce(cIntroduce);
        return this;
    }

    public void setcIntroduce(String cIntroduce) {
        this.cIntroduce = cIntroduce;
    }

    public Long getdId() {
        return this.dId;
    }

    public Course dId(Long dId) {
        this.setdId(dId);
        return this;
    }

    public void setdId(Long dId) {
        this.dId = dId;
    }

    public Boolean getcType() {
        return this.cType;
    }

    public Course cType(Boolean cType) {
        this.setcType(cType);
        return this;
    }

    public void setcType(Boolean cType) {
        this.cType = cType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", cName='" + getcName() + "'" +
            ", cIntroduce='" + getcIntroduce() + "'" +
            ", dId=" + getdId() +
            ", cType='" + getcType() + "'" +
            "}";
    }
}
