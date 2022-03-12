package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 角色菜单关联表
 */
@Schema(description = "角色菜单关联表")
@Entity
@Table(name = "sys_role_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysRoleMenu implements Serializable {

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
     * 菜单外键
     */
    @Schema(description = "菜单外键")
    @Column(name = "sys_menu_id")
    private Long sysMenuId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SysRoleMenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysRoleId() {
        return this.sysRoleId;
    }

    public SysRoleMenu sysRoleId(Long sysRoleId) {
        this.setSysRoleId(sysRoleId);
        return this;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public Long getSysMenuId() {
        return this.sysMenuId;
    }

    public SysRoleMenu sysMenuId(Long sysMenuId) {
        this.setSysMenuId(sysMenuId);
        return this;
    }

    public void setSysMenuId(Long sysMenuId) {
        this.sysMenuId = sysMenuId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysRoleMenu)) {
            return false;
        }
        return id != null && id.equals(((SysRoleMenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysRoleMenu{" +
            "id=" + getId() +
            ", sysRoleId=" + getSysRoleId() +
            ", sysMenuId=" + getSysMenuId() +
            "}";
    }
}
