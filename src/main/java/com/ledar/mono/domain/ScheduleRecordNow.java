package com.ledar.mono.domain;

import com.ledar.mono.domain.enumeration.Confirmation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 当天排程
 */
@Schema(description = "当天排程")
@Entity
@Table(name = "schedule_record_now")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScheduleRecordNow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 治疗项目编号
     */
    @Schema(description = "治疗项目编号")
    @Column(name = "cure_project_num")
    private Long cureProjectNum;

    /**
     * 职工编号
     */
    @Schema(description = "职工编号")
    @Column(name = "cure_id")
    private Long cureId;

    /**
     * 职工名称
     */
    @Schema(description = "职工名称")
    @Column(name = "name")
    private String name;

    /**
     * 排程时间
     */
    @Schema(description = "排程时间")
    @Column(name = "schedule_time")
    private Instant scheduleTime;

    /**
     * 技师确认状态
     */
    @Schema(description = "技师确认状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_isachive")
    private Confirmation scheduleIsachive;

    /**
     * 治疗时间
     */
    @Schema(description = "治疗时间")
    @Column(name = "schedule_cure_time")
    private Instant scheduleCureTime;

    /**
     * 当天详情编号
     */
    @Schema(description = "当天详情编号")
    @Column(name = "details_num")
    private Long detailsNum;

    /**
     * 照片地址
     */
    @Schema(description = "照片地址")
    @Column(name = "photo_url")
    private String photoUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScheduleRecordNow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCureProjectNum() {
        return this.cureProjectNum;
    }

    public ScheduleRecordNow cureProjectNum(Long cureProjectNum) {
        this.setCureProjectNum(cureProjectNum);
        return this;
    }

    public void setCureProjectNum(Long cureProjectNum) {
        this.cureProjectNum = cureProjectNum;
    }

    public Long getCureId() {
        return this.cureId;
    }

    public ScheduleRecordNow cureId(Long cureId) {
        this.setCureId(cureId);
        return this;
    }

    public void setCureId(Long cureId) {
        this.cureId = cureId;
    }

    public String getName() {
        return this.name;
    }

    public ScheduleRecordNow name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getScheduleTime() {
        return this.scheduleTime;
    }

    public ScheduleRecordNow scheduleTime(Instant scheduleTime) {
        this.setScheduleTime(scheduleTime);
        return this;
    }

    public void setScheduleTime(Instant scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Confirmation getScheduleIsachive() {
        return this.scheduleIsachive;
    }

    public ScheduleRecordNow scheduleIsachive(Confirmation scheduleIsachive) {
        this.setScheduleIsachive(scheduleIsachive);
        return this;
    }

    public void setScheduleIsachive(Confirmation scheduleIsachive) {
        this.scheduleIsachive = scheduleIsachive;
    }

    public Instant getScheduleCureTime() {
        return this.scheduleCureTime;
    }

    public ScheduleRecordNow scheduleCureTime(Instant scheduleCureTime) {
        this.setScheduleCureTime(scheduleCureTime);
        return this;
    }

    public void setScheduleCureTime(Instant scheduleCureTime) {
        this.scheduleCureTime = scheduleCureTime;
    }

    public Long getDetailsNum() {
        return this.detailsNum;
    }

    public ScheduleRecordNow detailsNum(Long detailsNum) {
        this.setDetailsNum(detailsNum);
        return this;
    }

    public void setDetailsNum(Long detailsNum) {
        this.detailsNum = detailsNum;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public ScheduleRecordNow photoUrl(String photoUrl) {
        this.setPhotoUrl(photoUrl);
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleRecordNow)) {
            return false;
        }
        return id != null && id.equals(((ScheduleRecordNow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleRecordNow{" +
            "id=" + getId() +
            ", cureProjectNum=" + getCureProjectNum() +
            ", cureId=" + getCureId() +
            ", name='" + getName() + "'" +
            ", scheduleTime='" + getScheduleTime() + "'" +
            ", scheduleIsachive='" + getScheduleIsachive() + "'" +
            ", scheduleCureTime='" + getScheduleCureTime() + "'" +
            ", detailsNum=" + getDetailsNum() +
            ", photoUrl='" + getPhotoUrl() + "'" +
            "}";
    }
}
