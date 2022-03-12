package com.ledar.mono.web.rest;

import com.ledar.mono.domain.PastStudentRecords;
import com.ledar.mono.repository.PastStudentRecordsRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.PastStudentRecords}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PastStudentRecordsResource {

    private final Logger log = LoggerFactory.getLogger(PastStudentRecordsResource.class);

    private static final String ENTITY_NAME = "pastStudentRecords";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PastStudentRecordsRepository pastStudentRecordsRepository;

    public PastStudentRecordsResource(PastStudentRecordsRepository pastStudentRecordsRepository) {
        this.pastStudentRecordsRepository = pastStudentRecordsRepository;
    }

    /**
     * {@code POST  /past-student-records} : Create a new pastStudentRecords.
     *
     * @param pastStudentRecords the pastStudentRecords to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pastStudentRecords, or with status {@code 400 (Bad Request)} if the pastStudentRecords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/past-student-records")
    public ResponseEntity<PastStudentRecords> createPastStudentRecords(@RequestBody PastStudentRecords pastStudentRecords)
        throws URISyntaxException {
        log.debug("REST request to save PastStudentRecords : {}", pastStudentRecords);
        if (pastStudentRecords.getId() != null) {
            throw new BadRequestAlertException("A new pastStudentRecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PastStudentRecords result = pastStudentRecordsRepository.save(pastStudentRecords);
        return ResponseEntity
            .created(new URI("/api/past-student-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /past-student-records/:id} : Updates an existing pastStudentRecords.
     *
     * @param id the id of the pastStudentRecords to save.
     * @param pastStudentRecords the pastStudentRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pastStudentRecords,
     * or with status {@code 400 (Bad Request)} if the pastStudentRecords is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pastStudentRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/past-student-records/{id}")
    public ResponseEntity<PastStudentRecords> updatePastStudentRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PastStudentRecords pastStudentRecords
    ) throws URISyntaxException {
        log.debug("REST request to update PastStudentRecords : {}, {}", id, pastStudentRecords);
        if (pastStudentRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pastStudentRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pastStudentRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PastStudentRecords result = pastStudentRecordsRepository.save(pastStudentRecords);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pastStudentRecords.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /past-student-records/:id} : Partial updates given fields of an existing pastStudentRecords, field will ignore if it is null
     *
     * @param id the id of the pastStudentRecords to save.
     * @param pastStudentRecords the pastStudentRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pastStudentRecords,
     * or with status {@code 400 (Bad Request)} if the pastStudentRecords is not valid,
     * or with status {@code 404 (Not Found)} if the pastStudentRecords is not found,
     * or with status {@code 500 (Internal Server Error)} if the pastStudentRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/past-student-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PastStudentRecords> partialUpdatePastStudentRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PastStudentRecords pastStudentRecords
    ) throws URISyntaxException {
        log.debug("REST request to partial update PastStudentRecords partially : {}, {}", id, pastStudentRecords);
        if (pastStudentRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pastStudentRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pastStudentRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PastStudentRecords> result = pastStudentRecordsRepository
            .findById(pastStudentRecords.getId())
            .map(existingPastStudentRecords -> {
                if (pastStudentRecords.getCrId() != null) {
                    existingPastStudentRecords.setCrId(pastStudentRecords.getCrId());
                }
                if (pastStudentRecords.getpId() != null) {
                    existingPastStudentRecords.setpId(pastStudentRecords.getpId());
                }
                if (pastStudentRecords.getSignIn() != null) {
                    existingPastStudentRecords.setSignIn(pastStudentRecords.getSignIn());
                }
                if (pastStudentRecords.getSignInTime() != null) {
                    existingPastStudentRecords.setSignInTime(pastStudentRecords.getSignInTime());
                }
                if (pastStudentRecords.getSignInImage() != null) {
                    existingPastStudentRecords.setSignInImage(pastStudentRecords.getSignInImage());
                }

                return existingPastStudentRecords;
            })
            .map(pastStudentRecordsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pastStudentRecords.getId().toString())
        );
    }

    /**
     * {@code GET  /past-student-records} : get all the pastStudentRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pastStudentRecords in body.
     */
    @GetMapping("/past-student-records")
    public List<PastStudentRecords> getAllPastStudentRecords() {
        log.debug("REST request to get all PastStudentRecords");
        return pastStudentRecordsRepository.findAll();
    }

    /**
     * {@code GET  /past-student-records/:id} : get the "id" pastStudentRecords.
     *
     * @param id the id of the pastStudentRecords to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pastStudentRecords, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/past-student-records/{id}")
    public ResponseEntity<PastStudentRecords> getPastStudentRecords(@PathVariable Long id) {
        log.debug("REST request to get PastStudentRecords : {}", id);
        Optional<PastStudentRecords> pastStudentRecords = pastStudentRecordsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pastStudentRecords);
    }

    /**
     * {@code DELETE  /past-student-records/:id} : delete the "id" pastStudentRecords.
     *
     * @param id the id of the pastStudentRecords to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/past-student-records/{id}")
    public ResponseEntity<Void> deletePastStudentRecords(@PathVariable Long id) {
        log.debug("REST request to delete PastStudentRecords : {}", id);
        pastStudentRecordsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
