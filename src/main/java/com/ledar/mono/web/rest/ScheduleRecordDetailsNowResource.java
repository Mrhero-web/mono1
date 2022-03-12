package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ScheduleRecordDetailsNow;
import com.ledar.mono.repository.ScheduleRecordDetailsNowRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ScheduleRecordDetailsNow}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleRecordDetailsNowResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleRecordDetailsNowResource.class);

    private static final String ENTITY_NAME = "scheduleRecordDetailsNow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleRecordDetailsNowRepository scheduleRecordDetailsNowRepository;

    public ScheduleRecordDetailsNowResource(ScheduleRecordDetailsNowRepository scheduleRecordDetailsNowRepository) {
        this.scheduleRecordDetailsNowRepository = scheduleRecordDetailsNowRepository;
    }

    /**
     * {@code POST  /schedule-record-details-nows} : Create a new scheduleRecordDetailsNow.
     *
     * @param scheduleRecordDetailsNow the scheduleRecordDetailsNow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleRecordDetailsNow, or with status {@code 400 (Bad Request)} if the scheduleRecordDetailsNow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-record-details-nows")
    public ResponseEntity<ScheduleRecordDetailsNow> createScheduleRecordDetailsNow(
        @RequestBody ScheduleRecordDetailsNow scheduleRecordDetailsNow
    ) throws URISyntaxException {
        log.debug("REST request to save ScheduleRecordDetailsNow : {}", scheduleRecordDetailsNow);
        if (scheduleRecordDetailsNow.getId() != null) {
            throw new BadRequestAlertException("A new scheduleRecordDetailsNow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleRecordDetailsNow result = scheduleRecordDetailsNowRepository.save(scheduleRecordDetailsNow);
        return ResponseEntity
            .created(new URI("/api/schedule-record-details-nows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-record-details-nows/:id} : Updates an existing scheduleRecordDetailsNow.
     *
     * @param id the id of the scheduleRecordDetailsNow to save.
     * @param scheduleRecordDetailsNow the scheduleRecordDetailsNow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordDetailsNow,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordDetailsNow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordDetailsNow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-record-details-nows/{id}")
    public ResponseEntity<ScheduleRecordDetailsNow> updateScheduleRecordDetailsNow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordDetailsNow scheduleRecordDetailsNow
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduleRecordDetailsNow : {}, {}", id, scheduleRecordDetailsNow);
        if (scheduleRecordDetailsNow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordDetailsNow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordDetailsNowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduleRecordDetailsNow result = scheduleRecordDetailsNowRepository.save(scheduleRecordDetailsNow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordDetailsNow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-record-details-nows/:id} : Partial updates given fields of an existing scheduleRecordDetailsNow, field will ignore if it is null
     *
     * @param id the id of the scheduleRecordDetailsNow to save.
     * @param scheduleRecordDetailsNow the scheduleRecordDetailsNow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordDetailsNow,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordDetailsNow is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleRecordDetailsNow is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordDetailsNow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-record-details-nows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleRecordDetailsNow> partialUpdateScheduleRecordDetailsNow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordDetailsNow scheduleRecordDetailsNow
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduleRecordDetailsNow partially : {}, {}", id, scheduleRecordDetailsNow);
        if (scheduleRecordDetailsNow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordDetailsNow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordDetailsNowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleRecordDetailsNow> result = scheduleRecordDetailsNowRepository
            .findById(scheduleRecordDetailsNow.getId())
            .map(existingScheduleRecordDetailsNow -> {
                if (scheduleRecordDetailsNow.getdName() != null) {
                    existingScheduleRecordDetailsNow.setdName(scheduleRecordDetailsNow.getdName());
                }
                if (scheduleRecordDetailsNow.getCureId() != null) {
                    existingScheduleRecordDetailsNow.setCureId(scheduleRecordDetailsNow.getCureId());
                }
                if (scheduleRecordDetailsNow.getMedicalNumber() != null) {
                    existingScheduleRecordDetailsNow.setMedicalNumber(scheduleRecordDetailsNow.getMedicalNumber());
                }
                if (scheduleRecordDetailsNow.getdNum() != null) {
                    existingScheduleRecordDetailsNow.setdNum(scheduleRecordDetailsNow.getdNum());
                }
                if (scheduleRecordDetailsNow.getIdNum() != null) {
                    existingScheduleRecordDetailsNow.setIdNum(scheduleRecordDetailsNow.getIdNum());
                }

                return existingScheduleRecordDetailsNow;
            })
            .map(scheduleRecordDetailsNowRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordDetailsNow.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-record-details-nows} : get all the scheduleRecordDetailsNows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleRecordDetailsNows in body.
     */
    @GetMapping("/schedule-record-details-nows")
    public List<ScheduleRecordDetailsNow> getAllScheduleRecordDetailsNows() {
        log.debug("REST request to get all ScheduleRecordDetailsNows");
        return scheduleRecordDetailsNowRepository.findAll();
    }

    /**
     * {@code GET  /schedule-record-details-nows/:id} : get the "id" scheduleRecordDetailsNow.
     *
     * @param id the id of the scheduleRecordDetailsNow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleRecordDetailsNow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-record-details-nows/{id}")
    public ResponseEntity<ScheduleRecordDetailsNow> getScheduleRecordDetailsNow(@PathVariable Long id) {
        log.debug("REST request to get ScheduleRecordDetailsNow : {}", id);
        Optional<ScheduleRecordDetailsNow> scheduleRecordDetailsNow = scheduleRecordDetailsNowRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduleRecordDetailsNow);
    }

    /**
     * {@code DELETE  /schedule-record-details-nows/:id} : delete the "id" scheduleRecordDetailsNow.
     *
     * @param id the id of the scheduleRecordDetailsNow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-record-details-nows/{id}")
    public ResponseEntity<Void> deleteScheduleRecordDetailsNow(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleRecordDetailsNow : {}", id);
        scheduleRecordDetailsNowRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
