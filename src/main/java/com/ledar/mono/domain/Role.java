package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.RoleName;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.domain.enumeration.WebOrApp;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 角色表
 */
@Schema(description = "角色表")
@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 角色编号,形如 ROLE_ADMIN,必须以ROLE_作为前缀(SpringSecurity要求)
     */
    @Schema(description = "角色编号,形如 ROLE_ADMIN,必须以ROLE_作为前缀(SpringSecurity要求)", required = true)
    @NotNull
    @Column(name = "role_code", nullable = false, unique = true)
    private String roleCode;

    /**
     * 角色英文名称
     */
    @Schema(description = "角色英文名称", required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name_in_en", nullable = false)
    private RoleName roleNameInEn;

    /**
     * 角色中文名称
     */
    @Schema(description = "角色中文名称")
    @Column(name = "role_name_in_cn")
    private String roleNameInCn;

    /**
     * 角色状态
     */
    @Schema(description = "角色状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "role_status")
    private Status roleStatus;

    /**
     * 操作平台
     */
    @Schema(description = "操作平台")
    @Enumerated(EnumType.STRING)
    @Column(name = "web_or_app")
    private WebOrApp webOrApp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public Role roleCode(String roleCode) {
        this.setRoleCode(roleCode);
        return this;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public RoleName getRoleNameInEn() {
        return this.roleNameInEn;
    }

    public Role roleNameInEn(RoleName roleNameInEn) {
        this.setRoleNameInEn(roleNameInEn);
        return this;
    }

    public void setRoleNameInEn(RoleName roleNameInEn) {
        this.roleNameInEn = roleNameInEn;
    }

    public String getRoleNameInCn() {
        return this.roleNameInCn;
    }

    public Role roleNameInCn(String roleNameInCn) {
        this.setRoleNameInCn(roleNameInCn);
        return this;
    }

    public void setRoleNameInCn(String roleNameInCn) {
        this.roleNameInCn = roleNameInCn;
    }

    public Status getRoleStatus() {
        return this.roleStatus;
    }

    public Role roleStatus(Status roleStatus) {
        this.setRoleStatus(roleStatus);
        return this;
    }

    public void setRoleStatus(Status roleStatus) {
        this.roleStatus = roleStatus;
    }

    public WebOrApp getWebOrApp() {
        return this.webOrApp;
    }

    public Role webOrApp(WebOrApp webOrApp) {
        this.setWebOrApp(webOrApp);
        return this;
    }

    public void setWebOrApp(WebOrApp webOrApp) {
        this.webOrApp = webOrApp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", roleCode='" + getRoleCode() + "'" +
            ", roleNameInEn='" + getRoleNameInEn() + "'" +
            ", roleNameInCn='" + getRoleNameInCn() + "'" +
            ", roleStatus='" + getRoleStatus() + "'" +
            ", webOrApp='" + getWebOrApp() + "'" +
            "}";
    }
}
