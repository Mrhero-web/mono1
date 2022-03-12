package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 角色接口关联表
 */
@Schema(description = "角色接口关联表")
@Entity
@Table(name = "sys_role_api")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysRoleApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 角色外键
     */
    @Schema(description = "角色外键")
    @Column(name = "sys_role_id")
    private Long sysRoleId;

    /**
     * 接口外键
     */
    @Schema(description = "接口外键")
    @Column(name = "sys_api_id")
    private Long sysApiId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SysRoleApi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysRoleId() {
        return this.sysRoleId;
    }

    public SysRoleApi sysRoleId(Long sysRoleId) {
        this.setSysRoleId(sysRoleId);
        return this;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public Long getSysApiId() {
        return this.sysApiId;
    }

    public SysRoleApi sysApiId(Long sysApiId) {
        this.setSysApiId(sysApiId);
        return this;
    }

    public void setSysApiId(Long sysApiId) {
        this.sysApiId = sysApiId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRoleApi)) {
            return false;
        }
        return id != null && id.equals(((SysRoleApi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysRoleApi{" +
            "id=" + getId() +
            ", sysRoleId=" + getSysRoleId() +
            ", sysApiId=" + getSysApiId() +
            "}";
    }
}
