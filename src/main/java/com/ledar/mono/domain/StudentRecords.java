package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.SignInStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 学生签到记录
 */
@Schema(description = "学生签到记录")
@Entity
@Table(name = "student_records")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 课程记录编号
     */
    @Schema(description = "课程记录编号")
    @Column(name = "cr_id")
    private Long crId;

    /**
     * 学生编号
     */
    @Schema(description = "学生编号")
    @Column(name = "p_id")
    private Long pId;

    /**
     * 学生签到状态
     */
    @Schema(description = "学生签到状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "sign_in")
    private SignInStatus signIn;

    /**
     * 签到时间
     */
    @Schema(description = "签到时间")
    @Column(name = "sign_in_time")
    private Instant signInTime;

    /**
     * 签到照片
     */
    @Schema(description = "签到照片")
    @Column(name = "sign_in_image")
    private String signInImage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentRecords id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCrId() {
        return this.crId;
    }

    public StudentRecords crId(Long crId) {
        this.setCrId(crId);
        return this;
    }

    public void setCrId(Long crId) {
        this.crId = crId;
    }

    public Long getpId() {
        return this.pId;
    }

    public StudentRecords pId(Long pId) {
        this.setpId(pId);
        return this;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public SignInStatus getSignIn() {
        return this.signIn;
    }

    public StudentRecords signIn(SignInStatus signIn) {
        this.setSignIn(signIn);
        return this;
    }

    public void setSignIn(SignInStatus signIn) {
        this.signIn = signIn;
    }

    public Instant getSignInTime() {
        return this.signInTime;
    }

    public StudentRecords signInTime(Instant signInTime) {
        this.setSignInTime(signInTime);
        return this;
    }

    public void setSignInTime(Instant signInTime) {
        this.signInTime = signInTime;
    }

    public String getSignInImage() {
        return this.signInImage;
    }

    public StudentRecords signInImage(String signInImage) {
        this.setSignInImage(signInImage);
        return this;
    }

    public void setSignInImage(String signInImage) {
        this.signInImage = signInImage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentRecords)) {
            return false;
        }
        return id != null && id.equals(((StudentRecords) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentRecords{" +
            "id=" + getId() +
            ", crId=" + getCrId() +
            ", pId=" + getpId() +
            ", signIn='" + getSignIn() + "'" +
            ", signInTime='" + getSignInTime() + "'" +
            ", signInImage='" + getSignInImage() + "'" +
            "}";
    }
}
