package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ScheduleRecordDetailsHistory;
import com.ledar.mono.repository.ScheduleRecordDetailsHistoryRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ScheduleRecordDetailsHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleRecordDetailsHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleRecordDetailsHistoryResource.class);

    private static final String ENTITY_NAME = "scheduleRecordDetailsHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleRecordDetailsHistoryRepository scheduleRecordDetailsHistoryRepository;

    public ScheduleRecordDetailsHistoryResource(ScheduleRecordDetailsHistoryRepository scheduleRecordDetailsHistoryRepository) {
        this.scheduleRecordDetailsHistoryRepository = scheduleRecordDetailsHistoryRepository;
    }

    /**
     * {@code POST  /schedule-record-details-histories} : Create a new scheduleRecordDetailsHistory.
     *
     * @param scheduleRecordDetailsHistory the scheduleRecordDetailsHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleRecordDetailsHistory, or with status {@code 400 (Bad Request)} if the scheduleRecordDetailsHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-record-details-histories")
    public ResponseEntity<ScheduleRecordDetailsHistory> createScheduleRecordDetailsHistory(
        @RequestBody ScheduleRecordDetailsHistory scheduleRecordDetailsHistory
    ) throws URISyntaxException {
        log.debug("REST request to save ScheduleRecordDetailsHistory : {}", scheduleRecordDetailsHistory);
        if (scheduleRecordDetailsHistory.getId() != null) {
            throw new BadRequestAlertException("A new scheduleRecordDetailsHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleRecordDetailsHistory result = scheduleRecordDetailsHistoryRepository.save(scheduleRecordDetailsHistory);
        return ResponseEntity
            .created(new URI("/api/schedule-record-details-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-record-details-histories/:id} : Updates an existing scheduleRecordDetailsHistory.
     *
     * @param id the id of the scheduleRecordDetailsHistory to save.
     * @param scheduleRecordDetailsHistory the scheduleRecordDetailsHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordDetailsHistory,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordDetailsHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordDetailsHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-record-details-histories/{id}")
    public ResponseEntity<ScheduleRecordDetailsHistory> updateScheduleRecordDetailsHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordDetailsHistory scheduleRecordDetailsHistory
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduleRecordDetailsHistory : {}, {}", id, scheduleRecordDetailsHistory);
        if (scheduleRecordDetailsHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordDetailsHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordDetailsHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduleRecordDetailsHistory result = scheduleRecordDetailsHistoryRepository.save(scheduleRecordDetailsHistory);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordDetailsHistory.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-record-details-histories/:id} : Partial updates given fields of an existing scheduleRecordDetailsHistory, field will ignore if it is null
     *
     * @param id the id of the scheduleRecordDetailsHistory to save.
     * @param scheduleRecordDetailsHistory the scheduleRecordDetailsHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordDetailsHistory,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordDetailsHistory is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleRecordDetailsHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordDetailsHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-record-details-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleRecordDetailsHistory> partialUpdateScheduleRecordDetailsHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordDetailsHistory scheduleRecordDetailsHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduleRecordDetailsHistory partially : {}, {}", id, scheduleRecordDetailsHistory);
        if (scheduleRecordDetailsHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordDetailsHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordDetailsHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleRecordDetailsHistory> result = scheduleRecordDetailsHistoryRepository
            .findById(scheduleRecordDetailsHistory.getId())
            .map(existingScheduleRecordDetailsHistory -> {
                if (scheduleRecordDetailsHistory.getdName() != null) {
                    existingScheduleRecordDetailsHistory.setdName(scheduleRecordDetailsHistory.getdName());
                }
                if (scheduleRecordDetailsHistory.getCureId() != null) {
                    existingScheduleRecordDetailsHistory.setCureId(scheduleRecordDetailsHistory.getCureId());
                }
                if (scheduleRecordDetailsHistory.getMedicalNumber() != null) {
                    existingScheduleRecordDetailsHistory.setMedicalNumber(scheduleRecordDetailsHistory.getMedicalNumber());
                }
                if (scheduleRecordDetailsHistory.getdNum() != null) {
                    existingScheduleRecordDetailsHistory.setdNum(scheduleRecordDetailsHistory.getdNum());
                }
                if (scheduleRecordDetailsHistory.getIdNum() != null) {
                    existingScheduleRecordDetailsHistory.setIdNum(scheduleRecordDetailsHistory.getIdNum());
                }

                return existingScheduleRecordDetailsHistory;
            })
            .map(scheduleRecordDetailsHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordDetailsHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-record-details-histories} : get all the scheduleRecordDetailsHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleRecordDetailsHistories in body.
     */
    @GetMapping("/schedule-record-details-histories")
    public List<ScheduleRecordDetailsHistory> getAllScheduleRecordDetailsHistories() {
        log.debug("REST request to get all ScheduleRecordDetailsHistories");
        return scheduleRecordDetailsHistoryRepository.findAll();
    }

    /**
     * {@code GET  /schedule-record-details-histories/:id} : get the "id" scheduleRecordDetailsHistory.
     *
     * @param id the id of the scheduleRecordDetailsHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleRecordDetailsHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-record-details-histories/{id}")
    public ResponseEntity<ScheduleRecordDetailsHistory> getScheduleRecordDetailsHistory(@PathVariable Long id) {
        log.debug("REST request to get ScheduleRecordDetailsHistory : {}", id);
        Optional<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistory = scheduleRecordDetailsHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduleRecordDetailsHistory);
    }

    /**
     * {@code DELETE  /schedule-record-details-histories/:id} : delete the "id" scheduleRecordDetailsHistory.
     *
     * @param id the id of the scheduleRecordDetailsHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-record-details-histories/{id}")
    public ResponseEntity<Void> deleteScheduleRecordDetailsHistory(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleRecordDetailsHistory : {}", id);
        scheduleRecordDetailsHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
