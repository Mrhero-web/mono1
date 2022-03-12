package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ARelevanceC;
import com.ledar.mono.repository.ARelevanceCRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ARelevanceC}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ARelevanceCResource {

    private final Logger log = LoggerFactory.getLogger(ARelevanceCResource.class);

    private static final String ENTITY_NAME = "aRelevanceC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ARelevanceCRepository aRelevanceCRepository;

    public ARelevanceCResource(ARelevanceCRepository aRelevanceCRepository) {
        this.aRelevanceCRepository = aRelevanceCRepository;
    }

    /**
     * {@code POST  /a-relevance-cs} : Create a new aRelevanceC.
     *
     * @param aRelevanceC the aRelevanceC to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aRelevanceC, or with status {@code 400 (Bad Request)} if the aRelevanceC has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/a-relevance-cs")
    public ResponseEntity<ARelevanceC> createARelevanceC(@RequestBody ARelevanceC aRelevanceC) throws URISyntaxException {
        log.debug("REST request to save ARelevanceC : {}", aRelevanceC);
        if (aRelevanceC.getId() != null) {
            throw new BadRequestAlertException("A new aRelevanceC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ARelevanceC result = aRelevanceCRepository.save(aRelevanceC);
        return ResponseEntity
            .created(new URI("/api/a-relevance-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /a-relevance-cs/:id} : Updates an existing aRelevanceC.
     *
     * @param id the id of the aRelevanceC to save.
     * @param aRelevanceC the aRelevanceC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aRelevanceC,
     * or with status {@code 400 (Bad Request)} if the aRelevanceC is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aRelevanceC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/a-relevance-cs/{id}")
    public ResponseEntity<ARelevanceC> updateARelevanceC(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ARelevanceC aRelevanceC
    ) throws URISyntaxException {
        log.debug("REST request to update ARelevanceC : {}, {}", id, aRelevanceC);
        if (aRelevanceC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aRelevanceC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aRelevanceCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ARelevanceC result = aRelevanceCRepository.save(aRelevanceC);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aRelevanceC.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /a-relevance-cs/:id} : Partial updates given fields of an existing aRelevanceC, field will ignore if it is null
     *
     * @param id the id of the aRelevanceC to save.
     * @param aRelevanceC the aRelevanceC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aRelevanceC,
     * or with status {@code 400 (Bad Request)} if the aRelevanceC is not valid,
     * or with status {@code 404 (Not Found)} if the aRelevanceC is not found,
     * or with status {@code 500 (Internal Server Error)} if the aRelevanceC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/a-relevance-cs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ARelevanceC> partialUpdateARelevanceC(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ARelevanceC aRelevanceC
    ) throws URISyntaxException {
        log.debug("REST request to partial update ARelevanceC partially : {}, {}", id, aRelevanceC);
        if (aRelevanceC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aRelevanceC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aRelevanceCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ARelevanceC> result = aRelevanceCRepository
            .findById(aRelevanceC.getId())
            .map(existingARelevanceC -> {
                if (aRelevanceC.getaId() != null) {
                    existingARelevanceC.setaId(aRelevanceC.getaId());
                }
                if (aRelevanceC.getcId() != null) {
                    existingARelevanceC.setcId(aRelevanceC.getcId());
                }

                return existingARelevanceC;
            })
            .map(aRelevanceCRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aRelevanceC.getId().toString())
        );
    }

    /**
     * {@code GET  /a-relevance-cs} : get all the aRelevanceCS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aRelevanceCS in body.
     */
    @GetMapping("/a-relevance-cs")
    public List<ARelevanceC> getAllARelevanceCS() {
        log.debug("REST request to get all ARelevanceCS");
        return aRelevanceCRepository.findAll();
    }

    /**
     * {@code GET  /a-relevance-cs/:id} : get the "id" aRelevanceC.
     *
     * @param id the id of the aRelevanceC to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aRelevanceC, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/a-relevance-cs/{id}")
    public ResponseEntity<ARelevanceC> getARelevanceC(@PathVariable Long id) {
        log.debug("REST request to get ARelevanceC : {}", id);
        Optional<ARelevanceC> aRelevanceC = aRelevanceCRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aRelevanceC);
    }

    /**
     * {@code DELETE  /a-relevance-cs/:id} : delete the "id" aRelevanceC.
     *
     * @param id the id of the aRelevanceC to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/a-relevance-cs/{id}")
    public ResponseEntity<Void> deleteARelevanceC(@PathVariable Long id) {
        log.debug("REST request to delete ARelevanceC : {}", id);
        aRelevanceCRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
