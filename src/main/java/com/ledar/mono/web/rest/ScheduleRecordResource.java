package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ScheduleRecord;
import com.ledar.mono.repository.ScheduleRecordRepository;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ledar.mono.domain.ScheduleRecord}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleRecordResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleRecordResource.class);

    private static final String ENTITY_NAME = "scheduleRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleRecordRepository scheduleRecordRepository;

    public ScheduleRecordResource(ScheduleRecordRepository scheduleRecordRepository) {
        this.scheduleRecordRepository = scheduleRecordRepository;
    }

    /**
     * {@code POST  /schedule-records} : Create a new scheduleRecord.
     *
     * @param scheduleRecord the scheduleRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleRecord, or with status {@code 400 (Bad Request)} if the scheduleRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-records")
    public ResponseEntity<ScheduleRecord> createScheduleRecord(@RequestBody ScheduleRecord scheduleRecord) throws URISyntaxException {
        log.debug("REST request to save ScheduleRecord : {}", scheduleRecord);
        if (scheduleRecord.getId() != null) {
            throw new BadRequestAlertException("A new scheduleRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleRecord result = scheduleRecordRepository.save(scheduleRecord);
        return ResponseEntity
            .created(new URI("/api/schedule-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-records/:id} : Updates an existing scheduleRecord.
     *
     * @param id the id of the scheduleRecord to save.
     * @param scheduleRecord the scheduleRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecord,
     * or with status {@code 400 (Bad Request)} if the scheduleRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-records/{id}")
    public ResponseEntity<ScheduleRecord> updateScheduleRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecord scheduleRecord
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduleRecord : {}, {}", id, scheduleRecord);
        if (scheduleRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduleRecord result = scheduleRecordRepository.save(scheduleRecord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecord.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-records/:id} : Partial updates given fields of an existing scheduleRecord, field will ignore if it is null
     *
     * @param id the id of the scheduleRecord to save.
     * @param scheduleRecord the scheduleRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecord,
     * or with status {@code 400 (Bad Request)} if the scheduleRecord is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleRecord is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleRecord> partialUpdateScheduleRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecord scheduleRecord
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduleRecord partially : {}, {}", id, scheduleRecord);
        if (scheduleRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleRecord> result = scheduleRecordRepository
            .findById(scheduleRecord.getId())
            .map(existingScheduleRecord -> {
                if (scheduleRecord.getCureProjectNum() != null) {
                    existingScheduleRecord.setCureProjectNum(scheduleRecord.getCureProjectNum());
                }
                if (scheduleRecord.getCureId() != null) {
                    existingScheduleRecord.setCureId(scheduleRecord.getCureId());
                }
                if (scheduleRecord.getName() != null) {
                    existingScheduleRecord.setName(scheduleRecord.getName());
                }
                if (scheduleRecord.getScheduleTime() != null) {
                    existingScheduleRecord.setScheduleTime(scheduleRecord.getScheduleTime());
                }
                if (scheduleRecord.getScheduleIsachive() != null) {
                    existingScheduleRecord.setScheduleIsachive(scheduleRecord.getScheduleIsachive());
                }
                if (scheduleRecord.getScheduleCureTime() != null) {
                    existingScheduleRecord.setScheduleCureTime(scheduleRecord.getScheduleCureTime());
                }
                if (scheduleRecord.getDetailsNum() != null) {
                    existingScheduleRecord.setDetailsNum(scheduleRecord.getDetailsNum());
                }
                if (scheduleRecord.getPhotoUrl() != null) {
                    existingScheduleRecord.setPhotoUrl(scheduleRecord.getPhotoUrl());
                }

                return existingScheduleRecord;
            })
            .map(scheduleRecordRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecord.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-records} : get all the scheduleRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleRecords in body.
     */
    @GetMapping("/schedule-records")
    public List<ScheduleRecord> getAllScheduleRecords() {
        log.debug("REST request to get all ScheduleRecords");
        return scheduleRecordRepository.findAll();
    }

    /**
     * {@code GET  /schedule-records/:id} : get the "id" scheduleRecord.
     *
     * @param id the id of the scheduleRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-records/{id}")
    public ResponseEntity<ScheduleRecord> getScheduleRecord(@PathVariable Long id) {
        log.debug("REST request to get ScheduleRecord : {}", id);
        Optional<ScheduleRecord> scheduleRecord = scheduleRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduleRecord);
    }

    /**
     * {@code DELETE  /schedule-records/:id} : delete the "id" scheduleRecord.
     *
     * @param id the id of the scheduleRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-records/{id}")
    public ResponseEntity<Void> deleteScheduleRecord(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleRecord : {}", id);
        scheduleRecordRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
