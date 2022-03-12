package com.ledar.mono.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 教室
 */
@Schema(description = "教室")
@Entity
@Table(name = "classroom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 教室地址
     */
    @Schema(description = "教室地址")
    @Column(name = "r_address")
    private String rAddress;

    /**
     * 教室名称
     */
    @Schema(description = "教室名称")
    @Column(name = "r_name")
    private String rName;

    /**
     * 负责教师
     */
    @Schema(description = "负责教师")
    @Column(name = "t_id")
    private Long tId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classroom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getrAddress() {
        return this.rAddress;
    }

    public Classroom rAddress(String rAddress) {
        this.setrAddress(rAddress);
        return this;
    }

    public void setrAddress(String rAddress) {
        this.rAddress = rAddress;
    }

    public String getrName() {
        return this.rName;
    }

    public Classroom rName(String rName) {
        this.setrName(rName);
        return this;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public Long gettId() {
        return this.tId;
    }

    public Classroom tId(Long tId) {
        this.settId(tId);
        return this;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classroom)) {
            return false;
        }
        return id != null && id.equals(((Classroom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classroom{" +
            "id=" + getId() +
            ", rAddress='" + getrAddress() + "'" +
            ", rName='" + getrName() + "'" +
            ", tId=" + gettId() +
            "}";
    }
}
