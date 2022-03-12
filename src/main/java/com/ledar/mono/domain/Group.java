package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 技/教师小组
 */
@Schema(description = "技/教师小组")
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 组长的员工编号
     */
    @Schema(description = "组长的员工编号", required = true)
    @NotNull
    @Column(name = "group_leader_staff_id", nullable = false)
    private Long groupLeaderStaffId;

    /**
     * 所属科室的编号
     */
    @Schema(description = "所属科室的编号", required = true)
    @NotNull
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Group id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupLeaderStaffId() {
        return this.groupLeaderStaffId;
    }

    public Group groupLeaderStaffId(Long groupLeaderStaffId) {
        this.setGroupLeaderStaffId(groupLeaderStaffId);
        return this;
    }

    public void setGroupLeaderStaffId(Long groupLeaderStaffId) {
        this.groupLeaderStaffId = groupLeaderStaffId;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public Group departmentId(Long departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return id != null && id.equals(((Group) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", groupLeaderStaffId=" + getGroupLeaderStaffId() +
            ", departmentId=" + getDepartmentId() +
            "}";
    }
}
