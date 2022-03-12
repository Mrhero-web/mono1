package com.ledar.mono.web.rest;

import com.ledar.mono.domain.TreatmentProgram;
import com.ledar.mono.repository.TreatmentProgramRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.TreatmentProgram}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TreatmentProgramResource {

    private final Logger log = LoggerFactory.getLogger(TreatmentProgramResource.class);

    private static final String ENTITY_NAME = "treatmentProgram";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TreatmentProgramRepository treatmentProgramRepository;

    public TreatmentProgramResource(TreatmentProgramRepository treatmentProgramRepository) {
        this.treatmentProgramRepository = treatmentProgramRepository;
    }

    /**
     * {@code POST  /treatment-programs} : Create a new treatmentProgram.
     *
     * @param treatmentProgram the treatmentProgram to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new treatmentProgram, or with status {@code 400 (Bad Request)} if the treatmentProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/treatment-programs")
    public ResponseEntity<TreatmentProgram> createTreatmentProgram(@RequestBody TreatmentProgram treatmentProgram)
        throws URISyntaxException {
        log.debug("REST request to save TreatmentProgram : {}", treatmentProgram);
        if (treatmentProgram.getId() != null) {
            throw new BadRequestAlertException("A new treatmentProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TreatmentProgram result = treatmentProgramRepository.save(treatmentProgram);
        return ResponseEntity
            .created(new URI("/api/treatment-programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /treatment-programs/:id} : Updates an existing treatmentProgram.
     *
     * @param id the id of the treatmentProgram to save.
     * @param treatmentProgram the treatmentProgram to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treatmentProgram,
     * or with status {@code 400 (Bad Request)} if the treatmentProgram is not valid,
     * or with status {@code 500 (Internal Server Error)} if the treatmentProgram couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/treatment-programs/{id}")
    public ResponseEntity<TreatmentProgram> updateTreatmentProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TreatmentProgram treatmentProgram
    ) throws URISyntaxException {
        log.debug("REST request to update TreatmentProgram : {}, {}", id, treatmentProgram);
        if (treatmentProgram.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, treatmentProgram.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!treatmentProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TreatmentProgram result = treatmentProgramRepository.save(treatmentProgram);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, treatmentProgram.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /treatment-programs/:id} : Partial updates given fields of an existing treatmentProgram, field will ignore if it is null
     *
     * @param id the id of the treatmentProgram to save.
     * @param treatmentProgram the treatmentProgram to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treatmentProgram,
     * or with status {@code 400 (Bad Request)} if the treatmentProgram is not valid,
     * or with status {@code 404 (Not Found)} if the treatmentProgram is not found,
     * or with status {@code 500 (Internal Server Error)} if the treatmentProgram couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/treatment-programs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TreatmentProgram> partialUpdateTreatmentProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TreatmentProgram treatmentProgram
    ) throws URISyntaxException {
        log.debug("REST request to partial update TreatmentProgram partially : {}, {}", id, treatmentProgram);
        if (treatmentProgram.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, treatmentProgram.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!treatmentProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TreatmentProgram> result = treatmentProgramRepository
            .findById(treatmentProgram.getId())
            .map(existingTreatmentProgram -> {
                if (treatmentProgram.getCureName() != null) {
                    existingTreatmentProgram.setCureName(treatmentProgram.getCureName());
                }
                if (treatmentProgram.getNorms() != null) {
                    existingTreatmentProgram.setNorms(treatmentProgram.getNorms());
                }
                if (treatmentProgram.getUnit() != null) {
                    existingTreatmentProgram.setUnit(treatmentProgram.getUnit());
                }
                if (treatmentProgram.getCharge() != null) {
                    existingTreatmentProgram.setCharge(treatmentProgram.getCharge());
                }
                if (treatmentProgram.getPrice() != null) {
                    existingTreatmentProgram.setPrice(treatmentProgram.getPrice());
                }

                return existingTreatmentProgram;
            })
            .map(treatmentProgramRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, treatmentProgram.getId().toString())
        );
    }

    /**
     * {@code GET  /treatment-programs} : get all the treatmentPrograms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of treatmentPrograms in body.
     */
    @GetMapping("/treatment-programs")
    public List<TreatmentProgram> getAllTreatmentPrograms() {
        log.debug("REST request to get all TreatmentPrograms");
        return treatmentProgramRepository.findAll();
    }

    /**
     * {@code GET  /treatment-programs/:id} : get the "id" treatmentProgram.
     *
     * @param id the id of the treatmentProgram to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the treatmentProgram, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/treatment-programs/{id}")
    public ResponseEntity<TreatmentProgram> getTreatmentProgram(@PathVariable Long id) {
        log.debug("REST request to get TreatmentProgram : {}", id);
        Optional<TreatmentProgram> treatmentProgram = treatmentProgramRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(treatmentProgram);
    }

    /**
     * {@code DELETE  /treatment-programs/:id} : delete the "id" treatmentProgram.
     *
     * @param id the id of the treatmentProgram to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/treatment-programs/{id}")
    public ResponseEntity<Void> deleteTreatmentProgram(@PathVariable Long id) {
        log.debug("REST request to delete TreatmentProgram : {}", id);
        treatmentProgramRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
