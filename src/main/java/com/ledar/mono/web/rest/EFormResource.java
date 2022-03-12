package com.ledar.mono.web.rest;

import com.ledar.mono.domain.EForm;
import com.ledar.mono.repository.EFormRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.EForm}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EFormResource {

    private final Logger log = LoggerFactory.getLogger(EFormResource.class);

    private static final String ENTITY_NAME = "eForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EFormRepository eFormRepository;

    public EFormResource(EFormRepository eFormRepository) {
        this.eFormRepository = eFormRepository;
    }

    /**
     * {@code POST  /e-forms} : Create a new eForm.
     *
     * @param eForm the eForm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eForm, or with status {@code 400 (Bad Request)} if the eForm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-forms")
    public ResponseEntity<EForm> createEForm(@RequestBody EForm eForm) throws URISyntaxException {
        log.debug("REST request to save EForm : {}", eForm);
        if (eForm.getId() != null) {
            throw new BadRequestAlertException("A new eForm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EForm result = eFormRepository.save(eForm);
        return ResponseEntity
            .created(new URI("/api/e-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-forms/:id} : Updates an existing eForm.
     *
     * @param id the id of the eForm to save.
     * @param eForm the eForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eForm,
     * or with status {@code 400 (Bad Request)} if the eForm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-forms/{id}")
    public ResponseEntity<EForm> updateEForm(@PathVariable(value = "id", required = false) final Long id, @RequestBody EForm eForm)
        throws URISyntaxException {
        log.debug("REST request to update EForm : {}, {}", id, eForm);
        if (eForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EForm result = eFormRepository.save(eForm);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eForm.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-forms/:id} : Partial updates given fields of an existing eForm, field will ignore if it is null
     *
     * @param id the id of the eForm to save.
     * @param eForm the eForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eForm,
     * or with status {@code 400 (Bad Request)} if the eForm is not valid,
     * or with status {@code 404 (Not Found)} if the eForm is not found,
     * or with status {@code 500 (Internal Server Error)} if the eForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-forms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EForm> partialUpdateEForm(@PathVariable(value = "id", required = false) final Long id, @RequestBody EForm eForm)
        throws URISyntaxException {
        log.debug("REST request to partial update EForm partially : {}, {}", id, eForm);
        if (eForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EForm> result = eFormRepository
            .findById(eForm.getId())
            .map(existingEForm -> {
                if (eForm.getCureId() != null) {
                    existingEForm.setCureId(eForm.getCureId());
                }
                if (eForm.getIdNum() != null) {
                    existingEForm.setIdNum(eForm.getIdNum());
                }
                if (eForm.geteCategory() != null) {
                    existingEForm.seteCategory(eForm.geteCategory());
                }
                if (eForm.getStaffId() != null) {
                    existingEForm.setStaffId(eForm.getStaffId());
                }
                if (eForm.geteConclusion() != null) {
                    existingEForm.seteConclusion(eForm.geteConclusion());
                }
                if (eForm.geteTime() != null) {
                    existingEForm.seteTime(eForm.geteTime());
                }
                if (eForm.geteIllness() != null) {
                    existingEForm.seteIllness(eForm.geteIllness());
                }

                return existingEForm;
            })
            .map(eFormRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eForm.getId().toString())
        );
    }

    /**
     * {@code GET  /e-forms} : get all the eForms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eForms in body.
     */
    @GetMapping("/e-forms")
    public List<EForm> getAllEForms() {
        log.debug("REST request to get all EForms");
        return eFormRepository.findAll();
    }

    /**
     * {@code GET  /e-forms/:id} : get the "id" eForm.
     *
     * @param id the id of the eForm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eForm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-forms/{id}")
    public ResponseEntity<EForm> getEForm(@PathVariable Long id) {
        log.debug("REST request to get EForm : {}", id);
        Optional<EForm> eForm = eFormRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eForm);
    }

    /**
     * {@code DELETE  /e-forms/:id} : delete the "id" eForm.
     *
     * @param id the id of the eForm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-forms/{id}")
    public ResponseEntity<Void> deleteEForm(@PathVariable Long id) {
        log.debug("REST request to delete EForm : {}", id);
        eFormRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
