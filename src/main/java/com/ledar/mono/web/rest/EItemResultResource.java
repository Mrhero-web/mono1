package com.ledar.mono.web.rest;

import com.ledar.mono.domain.EItemResult;
import com.ledar.mono.repository.EItemResultRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.EItemResult}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EItemResultResource {

    private final Logger log = LoggerFactory.getLogger(EItemResultResource.class);

    private static final String ENTITY_NAME = "eItemResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EItemResultRepository eItemResultRepository;

    public EItemResultResource(EItemResultRepository eItemResultRepository) {
        this.eItemResultRepository = eItemResultRepository;
    }

    /**
     * {@code POST  /e-item-results} : Create a new eItemResult.
     *
     * @param eItemResult the eItemResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eItemResult, or with status {@code 400 (Bad Request)} if the eItemResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-item-results")
    public ResponseEntity<EItemResult> createEItemResult(@RequestBody EItemResult eItemResult) throws URISyntaxException {
        log.debug("REST request to save EItemResult : {}", eItemResult);
        if (eItemResult.getId() != null) {
            throw new BadRequestAlertException("A new eItemResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EItemResult result = eItemResultRepository.save(eItemResult);
        return ResponseEntity
            .created(new URI("/api/e-item-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-item-results/:id} : Updates an existing eItemResult.
     *
     * @param id the id of the eItemResult to save.
     * @param eItemResult the eItemResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eItemResult,
     * or with status {@code 400 (Bad Request)} if the eItemResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eItemResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-item-results/{id}")
    public ResponseEntity<EItemResult> updateEItemResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EItemResult eItemResult
    ) throws URISyntaxException {
        log.debug("REST request to update EItemResult : {}, {}", id, eItemResult);
        if (eItemResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eItemResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eItemResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EItemResult result = eItemResultRepository.save(eItemResult);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eItemResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-item-results/:id} : Partial updates given fields of an existing eItemResult, field will ignore if it is null
     *
     * @param id the id of the eItemResult to save.
     * @param eItemResult the eItemResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eItemResult,
     * or with status {@code 400 (Bad Request)} if the eItemResult is not valid,
     * or with status {@code 404 (Not Found)} if the eItemResult is not found,
     * or with status {@code 500 (Internal Server Error)} if the eItemResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-item-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EItemResult> partialUpdateEItemResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EItemResult eItemResult
    ) throws URISyntaxException {
        log.debug("REST request to partial update EItemResult partially : {}, {}", id, eItemResult);
        if (eItemResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eItemResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eItemResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EItemResult> result = eItemResultRepository
            .findById(eItemResult.getId())
            .map(existingEItemResult -> {
                if (eItemResult.geteItemResult() != null) {
                    existingEItemResult.seteItemResult(eItemResult.geteItemResult());
                }
                if (eItemResult.geteNumber() != null) {
                    existingEItemResult.seteNumber(eItemResult.geteNumber());
                }
                if (eItemResult.geteItemNumber() != null) {
                    existingEItemResult.seteItemNumber(eItemResult.geteItemNumber());
                }
                if (eItemResult.geteSubitem() != null) {
                    existingEItemResult.seteSubitem(eItemResult.geteSubitem());
                }

                return existingEItemResult;
            })
            .map(eItemResultRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eItemResult.getId().toString())
        );
    }

    /**
     * {@code GET  /e-item-results} : get all the eItemResults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eItemResults in body.
     */
    @GetMapping("/e-item-results")
    public List<EItemResult> getAllEItemResults() {
        log.debug("REST request to get all EItemResults");
        return eItemResultRepository.findAll();
    }

    /**
     * {@code GET  /e-item-results/:id} : get the "id" eItemResult.
     *
     * @param id the id of the eItemResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eItemResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-item-results/{id}")
    public ResponseEntity<EItemResult> getEItemResult(@PathVariable Long id) {
        log.debug("REST request to get EItemResult : {}", id);
        Optional<EItemResult> eItemResult = eItemResultRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eItemResult);
    }

    /**
     * {@code DELETE  /e-item-results/:id} : delete the "id" eItemResult.
     *
     * @param id the id of the eItemResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-item-results/{id}")
    public ResponseEntity<Void> deleteEItemResult(@PathVariable Long id) {
        log.debug("REST request to delete EItemResult : {}", id);
        eItemResultRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
