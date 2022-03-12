package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ScheduleRecordHistory;
import com.ledar.mono.repository.ScheduleRecordHistoryRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ScheduleRecordHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleRecordHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleRecordHistoryResource.class);

    private static final String ENTITY_NAME = "scheduleRecordHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleRecordHistoryRepository scheduleRecordHistoryRepository;

    public ScheduleRecordHistoryResource(ScheduleRecordHistoryRepository scheduleRecordHistoryRepository) {
        this.scheduleRecordHistoryRepository = scheduleRecordHistoryRepository;
    }

    /**
     * {@code POST  /schedule-record-histories} : Create a new scheduleRecordHistory.
     *
     * @param scheduleRecordHistory the scheduleRecordHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleRecordHistory, or with status {@code 400 (Bad Request)} if the scheduleRecordHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-record-histories")
    public ResponseEntity<ScheduleRecordHistory> createScheduleRecordHistory(@RequestBody ScheduleRecordHistory scheduleRecordHistory)
        throws URISyntaxException {
        log.debug("REST request to save ScheduleRecordHistory : {}", scheduleRecordHistory);
        if (scheduleRecordHistory.getId() != null) {
            throw new BadRequestAlertException("A new scheduleRecordHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleRecordHistory result = scheduleRecordHistoryRepository.save(scheduleRecordHistory);
        return ResponseEntity
            .created(new URI("/api/schedule-record-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-record-histories/:id} : Updates an existing scheduleRecordHistory.
     *
     * @param id the id of the scheduleRecordHistory to save.
     * @param scheduleRecordHistory the scheduleRecordHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordHistory,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-record-histories/{id}")
    public ResponseEntity<ScheduleRecordHistory> updateScheduleRecordHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordHistory scheduleRecordHistory
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduleRecordHistory : {}, {}", id, scheduleRecordHistory);
        if (scheduleRecordHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduleRecordHistory result = scheduleRecordHistoryRepository.save(scheduleRecordHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-record-histories/:id} : Partial updates given fields of an existing scheduleRecordHistory, field will ignore if it is null
     *
     * @param id the id of the scheduleRecordHistory to save.
     * @param scheduleRecordHistory the scheduleRecordHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordHistory,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordHistory is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleRecordHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-record-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleRecordHistory> partialUpdateScheduleRecordHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordHistory scheduleRecordHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduleRecordHistory partially : {}, {}", id, scheduleRecordHistory);
        if (scheduleRecordHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleRecordHistory> result = scheduleRecordHistoryRepository
            .findById(scheduleRecordHistory.getId())
            .map(existingScheduleRecordHistory -> {
                if (scheduleRecordHistory.getCureProjectNum() != null) {
                    existingScheduleRecordHistory.setCureProjectNum(scheduleRecordHistory.getCureProjectNum());
                }
                if (scheduleRecordHistory.getCureId() != null) {
                    existingScheduleRecordHistory.setCureId(scheduleRecordHistory.getCureId());
                }
                if (scheduleRecordHistory.getName() != null) {
                    existingScheduleRecordHistory.setName(scheduleRecordHistory.getName());
                }
                if (scheduleRecordHistory.getScheduleTime() != null) {
                    existingScheduleRecordHistory.setScheduleTime(scheduleRecordHistory.getScheduleTime());
                }
                if (scheduleRecordHistory.getScheduleIsachive() != null) {
                    existingScheduleRecordHistory.setScheduleIsachive(scheduleRecordHistory.getScheduleIsachive());
                }
                if (scheduleRecordHistory.getScheduleCureTime() != null) {
                    existingScheduleRecordHistory.setScheduleCureTime(scheduleRecordHistory.getScheduleCureTime());
                }
                if (scheduleRecordHistory.getDetailsNum() != null) {
                    existingScheduleRecordHistory.setDetailsNum(scheduleRecordHistory.getDetailsNum());
                }
                if (scheduleRecordHistory.getPhotoUrl() != null) {
                    existingScheduleRecordHistory.setPhotoUrl(scheduleRecordHistory.getPhotoUrl());
                }

                return existingScheduleRecordHistory;
            })
            .map(scheduleRecordHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-record-histories} : get all the scheduleRecordHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleRecordHistories in body.
     */
    @GetMapping("/schedule-record-histories")
    public List<ScheduleRecordHistory> getAllScheduleRecordHistories() {
        log.debug("REST request to get all ScheduleRecordHistories");
        return scheduleRecordHistoryRepository.findAll();
    }

    /**
     * {@code GET  /schedule-record-histories/:id} : get the "id" scheduleRecordHistory.
     *
     * @param id the id of the scheduleRecordHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleRecordHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-record-histories/{id}")
    public ResponseEntity<ScheduleRecordHistory> getScheduleRecordHistory(@PathVariable Long id) {
        log.debug("REST request to get ScheduleRecordHistory : {}", id);
        Optional<ScheduleRecordHistory> scheduleRecordHistory = scheduleRecordHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduleRecordHistory);
    }

    /**
     * {@code DELETE  /schedule-record-histories/:id} : delete the "id" scheduleRecordHistory.
     *
     * @param id the id of the scheduleRecordHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-record-histories/{id}")
    public ResponseEntity<Void> deleteScheduleRecordHistory(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleRecordHistory : {}", id);
        scheduleRecordHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
