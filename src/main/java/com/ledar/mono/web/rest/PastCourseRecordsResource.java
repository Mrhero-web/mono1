package com.ledar.mono.web.rest;

import com.ledar.mono.domain.PastCourseRecords;
import com.ledar.mono.repository.PastCourseRecordsRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.PastCourseRecords}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PastCourseRecordsResource {

    private final Logger log = LoggerFactory.getLogger(PastCourseRecordsResource.class);

    private static final String ENTITY_NAME = "pastCourseRecords";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PastCourseRecordsRepository pastCourseRecordsRepository;

    public PastCourseRecordsResource(PastCourseRecordsRepository pastCourseRecordsRepository) {
        this.pastCourseRecordsRepository = pastCourseRecordsRepository;
    }

    /**
     * {@code POST  /past-course-records} : Create a new pastCourseRecords.
     *
     * @param pastCourseRecords the pastCourseRecords to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pastCourseRecords, or with status {@code 400 (Bad Request)} if the pastCourseRecords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/past-course-records")
    public ResponseEntity<PastCourseRecords> createPastCourseRecords(@RequestBody PastCourseRecords pastCourseRecords)
        throws URISyntaxException {
        log.debug("REST request to save PastCourseRecords : {}", pastCourseRecords);
        if (pastCourseRecords.getId() != null) {
            throw new BadRequestAlertException("A new pastCourseRecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PastCourseRecords result = pastCourseRecordsRepository.save(pastCourseRecords);
        return ResponseEntity
            .created(new URI("/api/past-course-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /past-course-records/:id} : Updates an existing pastCourseRecords.
     *
     * @param id the id of the pastCourseRecords to save.
     * @param pastCourseRecords the pastCourseRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pastCourseRecords,
     * or with status {@code 400 (Bad Request)} if the pastCourseRecords is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pastCourseRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/past-course-records/{id}")
    public ResponseEntity<PastCourseRecords> updatePastCourseRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PastCourseRecords pastCourseRecords
    ) throws URISyntaxException {
        log.debug("REST request to update PastCourseRecords : {}, {}", id, pastCourseRecords);
        if (pastCourseRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pastCourseRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pastCourseRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PastCourseRecords result = pastCourseRecordsRepository.save(pastCourseRecords);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pastCourseRecords.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /past-course-records/:id} : Partial updates given fields of an existing pastCourseRecords, field will ignore if it is null
     *
     * @param id the id of the pastCourseRecords to save.
     * @param pastCourseRecords the pastCourseRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pastCourseRecords,
     * or with status {@code 400 (Bad Request)} if the pastCourseRecords is not valid,
     * or with status {@code 404 (Not Found)} if the pastCourseRecords is not found,
     * or with status {@code 500 (Internal Server Error)} if the pastCourseRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/past-course-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PastCourseRecords> partialUpdatePastCourseRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PastCourseRecords pastCourseRecords
    ) throws URISyntaxException {
        log.debug("REST request to partial update PastCourseRecords partially : {}, {}", id, pastCourseRecords);
        if (pastCourseRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pastCourseRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pastCourseRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PastCourseRecords> result = pastCourseRecordsRepository
            .findById(pastCourseRecords.getId())
            .map(existingPastCourseRecords -> {
                if (pastCourseRecords.getcId() != null) {
                    existingPastCourseRecords.setcId(pastCourseRecords.getcId());
                }
                if (pastCourseRecords.gettId() != null) {
                    existingPastCourseRecords.settId(pastCourseRecords.gettId());
                }
                if (pastCourseRecords.getrId() != null) {
                    existingPastCourseRecords.setrId(pastCourseRecords.getrId());
                }
                if (pastCourseRecords.getClassDate() != null) {
                    existingPastCourseRecords.setClassDate(pastCourseRecords.getClassDate());
                }
                if (pastCourseRecords.getSchoolTime() != null) {
                    existingPastCourseRecords.setSchoolTime(pastCourseRecords.getSchoolTime());
                }
                if (pastCourseRecords.getClassTime() != null) {
                    existingPastCourseRecords.setClassTime(pastCourseRecords.getClassTime());
                }
                if (pastCourseRecords.getcStatus() != null) {
                    existingPastCourseRecords.setcStatus(pastCourseRecords.getcStatus());
                }

                return existingPastCourseRecords;
            })
            .map(pastCourseRecordsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pastCourseRecords.getId().toString())
        );
    }

    /**
     * {@code GET  /past-course-records} : get all the pastCourseRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pastCourseRecords in body.
     */
    @GetMapping("/past-course-records")
    public List<PastCourseRecords> getAllPastCourseRecords() {
        log.debug("REST request to get all PastCourseRecords");
        return pastCourseRecordsRepository.findAll();
    }

    /**
     * {@code GET  /past-course-records/:id} : get the "id" pastCourseRecords.
     *
     * @param id the id of the pastCourseRecords to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pastCourseRecords, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/past-course-records/{id}")
    public ResponseEntity<PastCourseRecords> getPastCourseRecords(@PathVariable Long id) {
        log.debug("REST request to get PastCourseRecords : {}", id);
        Optional<PastCourseRecords> pastCourseRecords = pastCourseRecordsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pastCourseRecords);
    }

    /**
     * {@code DELETE  /past-course-records/:id} : delete the "id" pastCourseRecords.
     *
     * @param id the id of the pastCourseRecords to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/past-course-records/{id}")
    public ResponseEntity<Void> deletePastCourseRecords(@PathVariable Long id) {
        log.debug("REST request to delete PastCourseRecords : {}", id);
        pastCourseRecordsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
