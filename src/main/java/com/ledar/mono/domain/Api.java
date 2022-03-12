package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 接口表
 */
@Schema(description = "接口表")
@Entity
@Table(name = "sys_api")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Api implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 接口名称
     */
    @Schema(description = "接口名称")
    @Column(name = "name")
    private String name;

    /**
     * 请求方法
     */
    @Schema(description = "请求方法")
    @Column(name = "request_method")
    private String requestMethod;

    /**
     * 路径
     */
    @Schema(description = "路径")
    @Column(name = "url")
    private String url;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Api id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Api name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public Api requestMethod(String requestMethod) {
        this.setRequestMethod(requestMethod);
        return this;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return this.url;
    }

    public Api url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Api)) {
            return false;
        }
        return id != null && id.equals(((Api) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Api{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", requestMethod='" + getRequestMethod() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
