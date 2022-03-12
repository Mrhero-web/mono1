package com.ledar.mono.web.rest;

import com.ledar.mono.domain.Therapist;
import com.ledar.mono.repository.TherapistRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.Therapist}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TherapistResource {

    private final Logger log = LoggerFactory.getLogger(TherapistResource.class);

    private static final String ENTITY_NAME = "therapist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TherapistRepository therapistRepository;

    public TherapistResource(TherapistRepository therapistRepository) {
        this.therapistRepository = therapistRepository;
    }

    /**
     * {@code POST  /therapists} : Create a new therapist.
     *
     * @param therapist the therapist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new therapist, or with status {@code 400 (Bad Request)} if the therapist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/therapists")
    public ResponseEntity<Therapist> createTherapist(@RequestBody Therapist therapist) throws URISyntaxException {
        log.debug("REST request to save Therapist : {}", therapist);
        if (therapist.getId() != null) {
            throw new BadRequestAlertException("A new therapist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Therapist result = therapistRepository.save(therapist);
        return ResponseEntity
            .created(new URI("/api/therapists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /therapists/:id} : Updates an existing therapist.
     *
     * @param id the id of the therapist to save.
     * @param therapist the therapist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated therapist,
     * or with status {@code 400 (Bad Request)} if the therapist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the therapist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/therapists/{id}")
    public ResponseEntity<Therapist> updateTherapist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Therapist therapist
    ) throws URISyntaxException {
        log.debug("REST request to update Therapist : {}, {}", id, therapist);
        if (therapist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, therapist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!therapistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Therapist result = therapistRepository.save(therapist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, therapist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /therapists/:id} : Partial updates given fields of an existing therapist, field will ignore if it is null
     *
     * @param id the id of the therapist to save.
     * @param therapist the therapist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated therapist,
     * or with status {@code 400 (Bad Request)} if the therapist is not valid,
     * or with status {@code 404 (Not Found)} if the therapist is not found,
     * or with status {@code 500 (Internal Server Error)} if the therapist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/therapists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Therapist> partialUpdateTherapist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Therapist therapist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Therapist partially : {}, {}", id, therapist);
        if (therapist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, therapist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!therapistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Therapist> result = therapistRepository
            .findById(therapist.getId())
            .map(existingTherapist -> {
                if (therapist.getCureName() != null) {
                    existingTherapist.setCureName(therapist.getCureName());
                }
                if (therapist.getCureId() != null) {
                    existingTherapist.setCureId(therapist.getCureId());
                }
                if (therapist.getName() != null) {
                    existingTherapist.setName(therapist.getName());
                }

                return existingTherapist;
            })
            .map(therapistRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, therapist.getId().toString())
        );
    }

    /**
     * {@code GET  /therapists} : get all the therapists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of therapists in body.
     */
    @GetMapping("/therapists")
    public List<Therapist> getAllTherapists() {
        log.debug("REST request to get all Therapists");
        return therapistRepository.findAll();
    }

    /**
     * {@code GET  /therapists/:id} : get the "id" therapist.
     *
     * @param id the id of the therapist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the therapist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/therapists/{id}")
    public ResponseEntity<Therapist> getTherapist(@PathVariable Long id) {
        log.debug("REST request to get Therapist : {}", id);
        Optional<Therapist> therapist = therapistRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(therapist);
    }

    /**
     * {@code DELETE  /therapists/:id} : delete the "id" therapist.
     *
     * @param id the id of the therapist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/therapists/{id}")
    public ResponseEntity<Void> deleteTherapist(@PathVariable Long id) {
        log.debug("REST request to delete Therapist : {}", id);
        therapistRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
