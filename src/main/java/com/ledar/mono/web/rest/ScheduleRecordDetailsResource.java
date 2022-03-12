package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ScheduleRecordDetails;
import com.ledar.mono.repository.ScheduleRecordDetailsRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ScheduleRecordDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleRecordDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleRecordDetailsResource.class);

    private static final String ENTITY_NAME = "scheduleRecordDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleRecordDetailsRepository scheduleRecordDetailsRepository;

    public ScheduleRecordDetailsResource(ScheduleRecordDetailsRepository scheduleRecordDetailsRepository) {
        this.scheduleRecordDetailsRepository = scheduleRecordDetailsRepository;
    }

    /**
     * {@code POST  /schedule-record-details} : Create a new scheduleRecordDetails.
     *
     * @param scheduleRecordDetails the scheduleRecordDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleRecordDetails, or with status {@code 400 (Bad Request)} if the scheduleRecordDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-record-details")
    public ResponseEntity<ScheduleRecordDetails> createScheduleRecordDetails(@RequestBody ScheduleRecordDetails scheduleRecordDetails)
        throws URISyntaxException {
        log.debug("REST request to save ScheduleRecordDetails : {}", scheduleRecordDetails);
        if (scheduleRecordDetails.getId() != null) {
            throw new BadRequestAlertException("A new scheduleRecordDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleRecordDetails result = scheduleRecordDetailsRepository.save(scheduleRecordDetails);
        return ResponseEntity
            .created(new URI("/api/schedule-record-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-record-details/:id} : Updates an existing scheduleRecordDetails.
     *
     * @param id the id of the scheduleRecordDetails to save.
     * @param scheduleRecordDetails the scheduleRecordDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordDetails,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-record-details/{id}")
    public ResponseEntity<ScheduleRecordDetails> updateScheduleRecordDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordDetails scheduleRecordDetails
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduleRecordDetails : {}, {}", id, scheduleRecordDetails);
        if (scheduleRecordDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduleRecordDetails result = scheduleRecordDetailsRepository.save(scheduleRecordDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-record-details/:id} : Partial updates given fields of an existing scheduleRecordDetails, field will ignore if it is null
     *
     * @param id the id of the scheduleRecordDetails to save.
     * @param scheduleRecordDetails the scheduleRecordDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleRecordDetails,
     * or with status {@code 400 (Bad Request)} if the scheduleRecordDetails is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleRecordDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleRecordDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-record-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleRecordDetails> partialUpdateScheduleRecordDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ScheduleRecordDetails scheduleRecordDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduleRecordDetails partially : {}, {}", id, scheduleRecordDetails);
        if (scheduleRecordDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleRecordDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleRecordDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleRecordDetails> result = scheduleRecordDetailsRepository
            .findById(scheduleRecordDetails.getId())
            .map(existingScheduleRecordDetails -> {
                if (scheduleRecordDetails.getdName() != null) {
                    existingScheduleRecordDetails.setdName(scheduleRecordDetails.getdName());
                }
                if (scheduleRecordDetails.getCureId() != null) {
                    existingScheduleRecordDetails.setCureId(scheduleRecordDetails.getCureId());
                }
                if (scheduleRecordDetails.getMedicalNumber() != null) {
                    existingScheduleRecordDetails.setMedicalNumber(scheduleRecordDetails.getMedicalNumber());
                }
                if (scheduleRecordDetails.getdNum() != null) {
                    existingScheduleRecordDetails.setdNum(scheduleRecordDetails.getdNum());
                }
                if (scheduleRecordDetails.getIdNum() != null) {
                    existingScheduleRecordDetails.setIdNum(scheduleRecordDetails.getIdNum());
                }

                return existingScheduleRecordDetails;
            })
            .map(scheduleRecordDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleRecordDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-record-details} : get all the scheduleRecordDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleRecordDetails in body.
     */
    @GetMapping("/schedule-record-details")
    public List<ScheduleRecordDetails> getAllScheduleRecordDetails() {
        log.debug("REST request to get all ScheduleRecordDetails");
        return scheduleRecordDetailsRepository.findAll();
    }

    /**
     * {@code GET  /schedule-record-details/:id} : get the "id" scheduleRecordDetails.
     *
     * @param id the id of the scheduleRecordDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleRecordDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-record-details/{id}")
    public ResponseEntity<ScheduleRecordDetails> getScheduleRecordDetails(@PathVariable Long id) {
        log.debug("REST request to get ScheduleRecordDetails : {}", id);
        Optional<ScheduleRecordDetails> scheduleRecordDetails = scheduleRecordDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduleRecordDetails);
    }

    /**
     * {@code DELETE  /schedule-record-details/:id} : delete the "id" scheduleRecordDetails.
     *
     * @param id the id of the scheduleRecordDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-record-details/{id}")
    public ResponseEntity<Void> deleteScheduleRecordDetails(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleRecordDetails : {}", id);
        scheduleRecordDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
