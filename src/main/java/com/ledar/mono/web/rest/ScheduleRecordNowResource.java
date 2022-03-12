package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ScheduleRecordNow;
import com.ledar.mono.repository.ScheduleRecordNowRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ScheduleRecordNow}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleRecordNowResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleRecordNowResource.class);

    private static final String ENTITY_NAME = "scheduleRecordNow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleRecordNowRepository scheduleRecordNowRepository;

    public ScheduleRecordNowResource(ScheduleRecordNowRepository scheduleRecordNowRepository) {
        this.scheduleRecordNowRepository = scheduleRecordNowRepository;
    }

    /**
     * {@code POST  /schedule-record-nows} : Create a new scheduleRecordNow.
     *
     * @param scheduleRecordNow the scheduleRecordNow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleRecordNow, or with status {@code 400 (Bad Request)} if the scheduleRecordNow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-record-nows")
    public ResponseEntity<ScheduleRecordNow> createScheduleRecordNow(@RequestBody ScheduleRecordNow scheduleRecordNow)
        throws URISyntaxException {
        log.debug("REST request to save ScheduleRecordNow : {}", scheduleRecordNow);
        if (scheduleRecordNow.getId() != null) {
            throw new BadRequestAlertException("A new scheduleRecordNow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleRecordNow result = scheduleRecordNowRepository.save(scheduleRecordNow);
        return ResponseEntity
            .created(new URI("/api/schedule-record-nows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-record-nows/:id} : Updates an existing scheduleRecordNow.
     *
     * @param id the id of the scheduleRecordNow to save.
     * @param scheduleRecordNow the scheduleRecordNow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordNow,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordNow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordNow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-record-nows/{id}")
    public ResponseEntity<ScheduleRecordNow> updateScheduleRecordNow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordNow scheduleRecordNow
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduleRecordNow : {}, {}", id, scheduleRecordNow);
        if (scheduleRecordNow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordNow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordNowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduleRecordNow result = scheduleRecordNowRepository.save(scheduleRecordNow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordNow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-record-nows/:id} : Partial updates given fields of an existing scheduleRecordNow, field will ignore if it is null
     *
     * @param id the id of the scheduleRecordNow to save.
     * @param scheduleRecordNow the scheduleRecordNow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordNow,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordNow is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleRecordNow is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordNow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-record-nows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleRecordNow> partialUpdateScheduleRecordNow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordNow scheduleRecordNow
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduleRecordNow partially : {}, {}", id, scheduleRecordNow);
        if (scheduleRecordNow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordNow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordNowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleRecordNow> result = scheduleRecordNowRepository
            .findById(scheduleRecordNow.getId())
            .map(existingScheduleRecordNow -> {
                if (scheduleRecordNow.getCureProjectNum() != null) {
                    existingScheduleRecordNow.setCureProjectNum(scheduleRecordNow.getCureProjectNum());
                }
                if (scheduleRecordNow.getCureId() != null) {
                    existingScheduleRecordNow.setCureId(scheduleRecordNow.getCureId());
                }
                if (scheduleRecordNow.getName() != null) {
                    existingScheduleRecordNow.setName(scheduleRecordNow.getName());
                }
                if (scheduleRecordNow.getScheduleTime() != null) {
                    existingScheduleRecordNow.setScheduleTime(scheduleRecordNow.getScheduleTime());
                }
                if (scheduleRecordNow.getScheduleIsachive() != null) {
                    existingScheduleRecordNow.setScheduleIsachive(scheduleRecordNow.getScheduleIsachive());
                }
                if (scheduleRecordNow.getScheduleCureTime() != null) {
                    existingScheduleRecordNow.setScheduleCureTime(scheduleRecordNow.getScheduleCureTime());
                }
                if (scheduleRecordNow.getDetailsNum() != null) {
                    existingScheduleRecordNow.setDetailsNum(scheduleRecordNow.getDetailsNum());
                }
                if (scheduleRecordNow.getPhotoUrl() != null) {
                    existingScheduleRecordNow.setPhotoUrl(scheduleRecordNow.getPhotoUrl());
                }

                return existingScheduleRecordNow;
            })
            .map(scheduleRecordNowRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordNow.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-record-nows} : get all the scheduleRecordNows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleRecordNows in body.
     */
    @GetMapping("/schedule-record-nows")
    public List<ScheduleRecordNow> getAllScheduleRecordNows() {
        log.debug("REST request to get all ScheduleRecordNows");
        return scheduleRecordNowRepository.findAll();
    }

    /**
     * {@code GET  /schedule-record-nows/:id} : get the "id" scheduleRecordNow.
     *
     * @param id the id of the scheduleRecordNow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleRecordNow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-record-nows/{id}")
    public ResponseEntity<ScheduleRecordNow> getScheduleRecordNow(@PathVariable Long id) {
        log.debug("REST request to get ScheduleRecordNow : {}", id);
        Optional<ScheduleRecordNow> scheduleRecordNow = scheduleRecordNowRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduleRecordNow);
    }

    /**
     * {@code DELETE  /schedule-record-nows/:id} : delete the "id" scheduleRecordNow.
     *
     * @param id the id of the scheduleRecordNow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-record-nows/{id}")
    public ResponseEntity<Void> deleteScheduleRecordNow(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleRecordNow : {}", id);
        scheduleRecordNowRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
