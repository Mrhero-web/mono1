package com.ledar.mono.web.rest;

import com.ledar.mono.domain.EItem2;
import com.ledar.mono.repository.EItem2Repository;
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
 * REST controller for managing {@link com.ledar.mono.domain.EItem2}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EItem2Resource {

    private final Logger log = LoggerFactory.getLogger(EItem2Resource.class);

    private static final String ENTITY_NAME = "eItem2";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EItem2Repository eItem2Repository;

    public EItem2Resource(EItem2Repository eItem2Repository) {
        this.eItem2Repository = eItem2Repository;
    }

    /**
     * {@code POST  /e-item-2-s} : Create a new eItem2.
     *
     * @param eItem2 the eItem2 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eItem2, or with status {@code 400 (Bad Request)} if the eItem2 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-item-2-s")
    public ResponseEntity<EItem2> createEItem2(@RequestBody EItem2 eItem2) throws URISyntaxException {
        log.debug("REST request to save EItem2 : {}", eItem2);
        if (eItem2.getId() != null) {
            throw new BadRequestAlertException("A new eItem2 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EItem2 result = eItem2Repository.save(eItem2);
        return ResponseEntity
            .created(new URI("/api/e-item-2-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-item-2-s/:id} : Updates an existing eItem2.
     *
     * @param id the id of the eItem2 to save.
     * @param eItem2 the eItem2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eItem2,
     * or with status {@code 400 (Bad Request)} if the eItem2 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eItem2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-item-2-s/{id}")
    public ResponseEntity<EItem2> updateEItem2(@PathVariable(value = "id", required = false) final Long id, @RequestBody EItem2 eItem2)
        throws URISyntaxException {
        log.debug("REST request to update EItem2 : {}, {}", id, eItem2);
        if (eItem2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eItem2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eItem2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EItem2 result = eItem2Repository.save(eItem2);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eItem2.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-item-2-s/:id} : Partial updates given fields of an existing eItem2, field will ignore if it is null
     *
     * @param id the id of the eItem2 to save.
     * @param eItem2 the eItem2 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eItem2,
     * or with status {@code 400 (Bad Request)} if the eItem2 is not valid,
     * or with status {@code 404 (Not Found)} if the eItem2 is not found,
     * or with status {@code 500 (Internal Server Error)} if the eItem2 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-item-2-s/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EItem2> partialUpdateEItem2(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EItem2 eItem2
    ) throws URISyntaxException {
        log.debug("REST request to partial update EItem2 partially : {}, {}", id, eItem2);
        if (eItem2.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eItem2.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eItem2Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EItem2> result = eItem2Repository
            .findById(eItem2.getId())
            .map(existingEItem2 -> {
                if (eItem2.geteItemResult() != null) {
                    existingEItem2.seteItemResult(eItem2.geteItemResult());
                }
                if (eItem2.geteZ1() != null) {
                    existingEItem2.seteZ1(eItem2.geteZ1());
                }

                return existingEItem2;
            })
            .map(eItem2Repository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eItem2.getId().toString())
        );
    }

    /**
     * {@code GET  /e-item-2-s} : get all the eItem2s.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eItem2s in body.
     */
    @GetMapping("/e-item-2-s")
    public List<EItem2> getAllEItem2s() {
        log.debug("REST request to get all EItem2s");
        return eItem2Repository.findAll();
    }

    /**
     * {@code GET  /e-item-2-s/:id} : get the "id" eItem2.
     *
     * @param id the id of the eItem2 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eItem2, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-item-2-s/{id}")
    public ResponseEntity<EItem2> getEItem2(@PathVariable Long id) {
        log.debug("REST request to get EItem2 : {}", id);
        Optional<EItem2> eItem2 = eItem2Repository.findById(id);
        return ResponseUtil.wrapOrNotFound(eItem2);
    }

    /**
     * {@code DELETE  /e-item-2-s/:id} : delete the "id" eItem2.
     *
     * @param id the id of the eItem2 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-item-2-s/{id}")
    public ResponseEntity<Void> deleteEItem2(@PathVariable Long id) {
        log.debug("REST request to delete EItem2 : {}", id);
        eItem2Repository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
