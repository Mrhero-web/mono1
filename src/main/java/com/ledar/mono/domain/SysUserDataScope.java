package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.CtrlType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户数据权限表
 */
@Schema(description = "用户数据权限表")
@Entity
@Table(name = "sys_user_data_scope")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysUserDataScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 控制用户ID
     */
    @Schema(description = "控制用户ID")
    @Column(name = "sys_user_id")
    private Long sysUserId;

    /**
     * 控制类型
     */
    @Schema(description = "控制类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "ctrl_type")
    private CtrlType ctrlType;

    /**
     * 控制数据，如控制类型为公司，那么这里就是公司编号
     */
    @Schema(description = "控制数据，如控制类型为公司，那么这里就是公司编号")
    @Column(name = "ctrl_data")
    private String ctrlData;

    /**
     * 控制权限
     */
    @Schema(description = "控制权限")
    @Column(name = "ctrl_permit")
    private String ctrlPermit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SysUserDataScope id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysUserId() {
        return this.sysUserId;
    }

    public SysUserDataScope sysUserId(Long sysUserId) {
        this.setSysUserId(sysUserId);
        return this;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public CtrlType getCtrlType() {
        return this.ctrlType;
    }

    public SysUserDataScope ctrlType(CtrlType ctrlType) {
        this.setCtrlType(ctrlType);
        return this;
    }

    public void setCtrlType(CtrlType ctrlType) {
        this.ctrlType = ctrlType;
    }

    public String getCtrlData() {
        return this.ctrlData;
    }

    public SysUserDataScope ctrlData(String ctrlData) {
        this.setCtrlData(ctrlData);
        return this;
    }

    public void setCtrlData(String ctrlData) {
        this.ctrlData = ctrlData;
    }

    public String getCtrlPermit() {
        return this.ctrlPermit;
    }

    public SysUserDataScope ctrlPermit(String ctrlPermit) {
        this.setCtrlPermit(ctrlPermit);
        return this;
    }

    public void setCtrlPermit(String ctrlPermit) {
        this.ctrlPermit = ctrlPermit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysUserDataScope)) {
            return false;
        }
        return id != null && id.equals(((SysUserDataScope) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysUserDataScope{" +
            "id=" + getId() +
            ", sysUserId=" + getSysUserId() +
            ", ctrlType='" + getCtrlType() + "'" +
            ", ctrlData='" + getCtrlData() + "'" +
            ", ctrlPermit='" + getCtrlPermit() + "'" +
            "}";
    }
}
