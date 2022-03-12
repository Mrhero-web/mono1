package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 科室信息
 */
@Schema(description = "科室信息")
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 科室名称
     */
    @Schema(description = "科室名称")
    @Column(name = "d_name")
    private String dName;

    /**
     * 科室主任名称
     */
    @Schema(description = "科室主任名称")
    @Column(name = "d_manager")
    private String dManager;

    /**
     * 科室主任编号
     */
    @Schema(description = "科室主任编号")
    @Column(name = "d_num")
    private Long dNum;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Department id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getdName() {
        return this.dName;
    }

    public Department dName(String dName) {
        this.setdName(dName);
        return this;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdManager() {
        return this.dManager;
    }

    public Department dManager(String dManager) {
        this.setdManager(dManager);
        return this;
    }

    public void setdManager(String dManager) {
        this.dManager = dManager;
    }

    public Long getdNum() {
        return this.dNum;
    }

    public Department dNum(Long dNum) {
        this.setdNum(dNum);
        return this;
    }

    public void setdNum(Long dNum) {
        this.dNum = dNum;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", dName='" + getdName() + "'" +
            ", dManager='" + getdManager() + "'" +
            ", dNum=" + getdNum() +
            "}";
    }
}
