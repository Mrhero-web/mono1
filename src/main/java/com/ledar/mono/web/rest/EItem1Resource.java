package com.ledar.mono.web.rest;

import com.ledar.mono.domain.EItem1;
import com.ledar.mono.repository.EItem1Repository;
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
 * REST controller for managing {@link com.ledar.mono.domain.EItem1}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EItem1Resource {

    private final Logger log = LoggerFactory.getLogger(EItem1Resource.class);

    private static final String ENTITY_NAME = "eItem1";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EItem1Repository eItem1Repository;

    public EItem1Resource(EItem1Repository eItem1Repository) {
        this.eItem1Repository = eItem1Repository;
    }

    /**
     * {@code POST  /e-item-1-s} : Create a new eItem1.
     *
     * @param eItem1 the eItem1 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eItem1, or with status {@code 400 (Bad Request)} if the eItem1 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-item-1-s")
    public ResponseEntity<EItem1> createEItem1(@RequestBody EItem1 eItem1) throws URISyntaxException {
        log.debug("REST request to save EItem1 : {}", eItem1);
        if (eItem1.getId() != null) {
            throw new BadRequestAlertException("A new eItem1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EItem1 result = eItem1Repository.save(eItem1);
        return ResponseEntity
            .created(new URI("/api/e-item-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-item-1-s/:id} : Updates an existing eItem1.
     *
     * @param id the id of the eItem1 to save.
     * @param eItem1 the eItem1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eItem1,
     * or with status {@code 400 (Bad Request)} if the eItem1 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eItem1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-item-1-s/{id}")
    public ResponseEntity<EItem1> updateEItem1(@PathVariable(value = "id", required = false) final Long id, @RequestBody EItem1 eItem1)
        throws URISyntaxException {
        log.debug("REST request to update EItem1 : {}, {}", id, eItem1);
        if (eItem1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eItem1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eItem1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EItem1 result = eItem1Repository.save(eItem1);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eItem1.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-item-1-s/:id} : Partial updates given fields of an existing eItem1, field will ignore if it is null
     *
     * @param id the id of the eItem1 to save.
     * @param eItem1 the eItem1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eItem1,
     * or with status {@code 400 (Bad Request)} if the eItem1 is not valid,
     * or with status {@code 404 (Not Found)} if the eItem1 is not found,
     * or with status {@code 500 (Internal Server Error)} if the eItem1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-item-1-s/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EItem1> partialUpdateEItem1(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EItem1 eItem1
    ) throws URISyntaxException {
        log.debug("REST request to partial update EItem1 partially : {}, {}", id, eItem1);
        if (eItem1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eItem1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eItem1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EItem1> result = eItem1Repository
            .findById(eItem1.getId())
            .map(existingEItem1 -> {
                if (eItem1.geteItemResult() != null) {
                    existingEItem1.seteItemResult(eItem1.geteItemResult());
                }
                if (eItem1.geteZ1() != null) {
                    existingEItem1.seteZ1(eItem1.geteZ1());
                }

                return existingEItem1;
            })
            .map(eItem1Repository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eItem1.getId().toString())
        );
    }

    /**
     * {@code GET  /e-item-1-s} : get all the eItem1s.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eItem1s in body.
     */
    @GetMapping("/e-item-1-s")
    public List<EItem1> getAllEItem1s() {
        log.debug("REST request to get all EItem1s");
        return eItem1Repository.findAll();
    }

    /**
     * {@code GET  /e-item-1-s/:id} : get the "id" eItem1.
     *
     * @param id the id of the eItem1 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eItem1, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-item-1-s/{id}")
    public ResponseEntity<EItem1> getEItem1(@PathVariable Long id) {
        log.debug("REST request to get EItem1 : {}", id);
        Optional<EItem1> eItem1 = eItem1Repository.findById(id);
        return ResponseUtil.wrapOrNotFound(eItem1);
    }

    /**
     * {@code DELETE  /e-item-1-s/:id} : delete the "id" eItem1.
     *
     * @param id the id of the eItem1 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-item-1-s/{id}")
    public ResponseEntity<Void> deleteEItem1(@PathVariable Long id) {
        log.debug("REST request to delete EItem1 : {}", id);
        eItem1Repository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
