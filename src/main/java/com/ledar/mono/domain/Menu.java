package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.MenuType;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.domain.enumeration.WebOrApp;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 菜单表
 */
@Schema(description = "菜单表")
@Entity
@Table(name = "sys_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 菜单编码
     */
    @Schema(description = "菜单编码")
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 父级编号
     */
    @Schema(description = "父级编号")
    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 本级排序号（升序）
     */
    @Schema(description = "本级排序号（升序）")
    @Column(name = "tree_sort")
    private Integer treeSort;

    /**
     * 所有级别排序号
     */
    @Schema(description = "所有级别排序号")
    @Column(name = "tree_sorts")
    private Integer treeSorts;

    /**
     * 是否最末级
     */
    @Schema(description = "是否最末级", required = true)
    @NotNull
    @Column(name = "tree_leaf", nullable = false)
    private Boolean treeLeaf;

    /**
     * 层次级别
     */
    @Schema(description = "层次级别", required = true)
    @NotNull
    @Column(name = "tree_level", nullable = false)
    private Integer treeLevel;

    /**
     * 全节点名
     */
    @Schema(description = "全节点名")
    @Column(name = "tree_names")
    private String treeNames;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称", required = true)
    @NotNull
    @Column(name = "menu_name", nullable = false)
    private String menuName;

    /**
     * 菜单类型
     */
    @Schema(description = "菜单类型", required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "menu_type", nullable = false)
    private MenuType menuType;

    /**
     * 链接
     */
    @Schema(description = "链接")
    @Column(name = "menu_href")
    private String menuHref;

    /**
     * 图标
     */
    @Schema(description = "图标")
    @Column(name = "menu_icon")
    private String menuIcon;

    /**
     * 菜单标题
     */
    @Schema(description = "菜单标题")
    @Column(name = "menu_title")
    private String menuTitle;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    @Column(name = "permission")
    private String permission;

    /**
     * 菜单排序(升序)
     */
    @Schema(description = "菜单排序(升序)", required = true)
    @NotNull
    @Column(name = "menu_sort", nullable = false)
    private Integer menuSort;

    /**
     * 是否显示
     */
    @Schema(description = "是否显示", required = true)
    @NotNull
    @Column(name = "is_show", nullable = false)
    private Boolean isShow;

    /**
     * 归属系统
     */
    @Schema(description = "归属系统")
    @Enumerated(EnumType.STRING)
    @Column(name = "sys_code")
    private WebOrApp sysCode;

    /**
     * 状态
     */
    @Schema(description = "状态", required = true)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * 备注信息
     */
    @Schema(description = "备注信息")
    @Column(name = "remarks")
    private String remarks;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuCode() {
        return this.menuCode;
    }

    public Menu menuCode(String menuCode) {
        this.setMenuCode(menuCode);
        return this;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public Menu parentId(Integer parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return this.parentIds;
    }

    public Menu parentIds(String parentIds) {
        this.setParentIds(parentIds);
        return this;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getTreeSort() {
        return this.treeSort;
    }

    public Menu treeSort(Integer treeSort) {
        this.setTreeSort(treeSort);
        return this;
    }

    public void setTreeSort(Integer treeSort) {
        this.treeSort = treeSort;
    }

    public Integer getTreeSorts() {
        return this.treeSorts;
    }

    public Menu treeSorts(Integer treeSorts) {
        this.setTreeSorts(treeSorts);
        return this;
    }

    public void setTreeSorts(Integer treeSorts) {
        this.treeSorts = treeSorts;
    }

    public Boolean getTreeLeaf() {
        return this.treeLeaf;
    }

    public Menu treeLeaf(Boolean treeLeaf) {
        this.setTreeLeaf(treeLeaf);
        return this;
    }

    public void setTreeLeaf(Boolean treeLeaf) {
        this.treeLeaf = treeLeaf;
    }

    public Integer getTreeLevel() {
        return this.treeLevel;
    }

    public Menu treeLevel(Integer treeLevel) {
        this.setTreeLevel(treeLevel);
        return this;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    public String getTreeNames() {
        return this.treeNames;
    }

    public Menu treeNames(String treeNames) {
        this.setTreeNames(treeNames);
        return this;
    }

    public void setTreeNames(String treeNames) {
        this.treeNames = treeNames;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public Menu menuName(String menuName) {
        this.setMenuName(menuName);
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public MenuType getMenuType() {
        return this.menuType;
    }

    public Menu menuType(MenuType menuType) {
        this.setMenuType(menuType);
        return this;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public String getMenuHref() {
        return this.menuHref;
    }

    public Menu menuHref(String menuHref) {
        this.setMenuHref(menuHref);
        return this;
    }

    public void setMenuHref(String menuHref) {
        this.menuHref = menuHref;
    }

    public String getMenuIcon() {
        return this.menuIcon;
    }

    public Menu menuIcon(String menuIcon) {
        this.setMenuIcon(menuIcon);
        return this;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuTitle() {
        return this.menuTitle;
    }

    public Menu menuTitle(String menuTitle) {
        this.setMenuTitle(menuTitle);
        return this;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getPermission() {
        return this.permission;
    }

    public Menu permission(String permission) {
        this.setPermission(permission);
        return this;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getMenuSort() {
        return this.menuSort;
    }

    public Menu menuSort(Integer menuSort) {
        this.setMenuSort(menuSort);
        return this;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public Boolean getIsShow() {
        return this.isShow;
    }

    public Menu isShow(Boolean isShow) {
        this.setIsShow(isShow);
        return this;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public WebOrApp getSysCode() {
        return this.sysCode;
    }

    public Menu sysCode(WebOrApp sysCode) {
        this.setSysCode(sysCode);
        return this;
    }

    public void setSysCode(WebOrApp sysCode) {
        this.sysCode = sysCode;
    }

    public Status getStatus() {
        return this.status;
    }

    public Menu status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Menu remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", menuCode='" + getMenuCode() + "'" +
            ", parentId=" + getParentId() +
            ", parentIds='" + getParentIds() + "'" +
            ", treeSort=" + getTreeSort() +
            ", treeSorts=" + getTreeSorts() +
            ", treeLeaf='" + getTreeLeaf() + "'" +
            ", treeLevel=" + getTreeLevel() +
            ", treeNames='" + getTreeNames() + "'" +
            ", menuName='" + getMenuName() + "'" +
            ", menuType='" + getMenuType() + "'" +
            ", menuHref='" + getMenuHref() + "'" +
            ", menuIcon='" + getMenuIcon() + "'" +
            ", menuTitle='" + getMenuTitle() + "'" +
            ", permission='" + getPermission() + "'" +
            ", menuSort=" + getMenuSort() +
            ", isShow='" + getIsShow() + "'" +
            ", sysCode='" + getSysCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
