package com.ledar.mono.web.rest;

import com.ledar.mono.domain.StudentRecords;
import com.ledar.mono.repository.StudentRecordsRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.StudentRecords}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StudentRecordsResource {

    private final Logger log = LoggerFactory.getLogger(StudentRecordsResource.class);

    private static final String ENTITY_NAME = "studentRecords";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentRecordsRepository studentRecordsRepository;

    public StudentRecordsResource(StudentRecordsRepository studentRecordsRepository) {
        this.studentRecordsRepository = studentRecordsRepository;
    }

    /**
     * {@code POST  /student-records} : Create a new studentRecords.
     *
     * @param studentRecords the studentRecords to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentRecords, or with status {@code 400 (Bad Request)} if the studentRecords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-records")
    public ResponseEntity<StudentRecords> createStudentRecords(@RequestBody StudentRecords studentRecords) throws URISyntaxException {
        log.debug("REST request to save StudentRecords : {}", studentRecords);
        if (studentRecords.getId() != null) {
            throw new BadRequestAlertException("A new studentRecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentRecords result = studentRecordsRepository.save(studentRecords);
        return ResponseEntity
            .created(new URI("/api/student-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-records/:id} : Updates an existing studentRecords.
     *
     * @param id the id of the studentRecords to save.
     * @param studentRecords the studentRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentRecords,
     * or with status {@code 400 (Bad Request)} if the studentRecords is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-records/{id}")
    public ResponseEntity<StudentRecords> updateStudentRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentRecords studentRecords
    ) throws URISyntaxException {
        log.debug("REST request to update StudentRecords : {}, {}", id, studentRecords);
        if (studentRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentRecords result = studentRecordsRepository.save(studentRecords);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentRecords.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-records/:id} : Partial updates given fields of an existing studentRecords, field will ignore if it is null
     *
     * @param id the id of the studentRecords to save.
     * @param studentRecords the studentRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentRecords,
     * or with status {@code 400 (Bad Request)} if the studentRecords is not valid,
     * or with status {@code 404 (Not Found)} if the studentRecords is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentRecords> partialUpdateStudentRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudentRecords studentRecords
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentRecords partially : {}, {}", id, studentRecords);
        if (studentRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentRecords> result = studentRecordsRepository
            .findById(studentRecords.getId())
            .map(existingStudentRecords -> {
                if (studentRecords.getCrId() != null) {
                    existingStudentRecords.setCrId(studentRecords.getCrId());
                }
                if (studentRecords.getpId() != null) {
                    existingStudentRecords.setpId(studentRecords.getpId());
                }
                if (studentRecords.getSignIn() != null) {
                    existingStudentRecords.setSignIn(studentRecords.getSignIn());
                }
                if (studentRecords.getSignInTime() != null) {
                    existingStudentRecords.setSignInTime(studentRecords.getSignInTime());
                }
                if (studentRecords.getSignInImage() != null) {
                    existingStudentRecords.setSignInImage(studentRecords.getSignInImage());
                }

                return existingStudentRecords;
            })
            .map(studentRecordsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentRecords.getId().toString())
        );
    }

    /**
     * {@code GET  /student-records} : get all the studentRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentRecords in body.
     */
    @GetMapping("/student-records")
    public List<StudentRecords> getAllStudentRecords() {
        log.debug("REST request to get all StudentRecords");
        return studentRecordsRepository.findAll();
    }

    /**
     * {@code GET  /student-records/:id} : get the "id" studentRecords.
     *
     * @param id the id of the studentRecords to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentRecords, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-records/{id}")
    public ResponseEntity<StudentRecords> getStudentRecords(@PathVariable Long id) {
        log.debug("REST request to get StudentRecords : {}", id);
        Optional<StudentRecords> studentRecords = studentRecordsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentRecords);
    }

    /**
     * {@code DELETE  /student-records/:id} : delete the "id" studentRecords.
     *
     * @param id the id of the studentRecords to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-records/{id}")
    public ResponseEntity<Void> deleteStudentRecords(@PathVariable Long id) {
        log.debug("REST request to delete StudentRecords : {}", id);
        studentRecordsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
