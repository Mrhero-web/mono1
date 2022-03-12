package com.ledar.mono.web.rest;

import com.ledar.mono.domain.InMedicalAdvice;
import com.ledar.mono.repository.InMedicalAdviceRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.InMedicalAdvice}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InMedicalAdviceResource {

    private final Logger log = LoggerFactory.getLogger(InMedicalAdviceResource.class);

    private static final String ENTITY_NAME = "inMedicalAdvice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InMedicalAdviceRepository inMedicalAdviceRepository;

    public InMedicalAdviceResource(InMedicalAdviceRepository inMedicalAdviceRepository) {
        this.inMedicalAdviceRepository = inMedicalAdviceRepository;
    }

    /**
     * {@code POST  /in-medical-advices} : Create a new inMedicalAdvice.
     *
     * @param inMedicalAdvice the inMedicalAdvice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inMedicalAdvice, or with status {@code 400 (Bad Request)} if the inMedicalAdvice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/in-medical-advices")
    public ResponseEntity<InMedicalAdvice> createInMedicalAdvice(@RequestBody InMedicalAdvice inMedicalAdvice) throws URISyntaxException {
        log.debug("REST request to save InMedicalAdvice : {}", inMedicalAdvice);
        if (inMedicalAdvice.getId() != null) {
            throw new BadRequestAlertException("A new inMedicalAdvice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InMedicalAdvice result = inMedicalAdviceRepository.save(inMedicalAdvice);
        return ResponseEntity
            .created(new URI("/api/in-medical-advices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /in-medical-advices/:id} : Updates an existing inMedicalAdvice.
     *
     * @param id the id of the inMedicalAdvice to save.
     * @param inMedicalAdvice the inMedicalAdvice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inMedicalAdvice,
     * or with status {@code 400 (Bad Request)} if the inMedicalAdvice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inMedicalAdvice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/in-medical-advices/{id}")
    public ResponseEntity<InMedicalAdvice> updateInMedicalAdvice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InMedicalAdvice inMedicalAdvice
    ) throws URISyntaxException {
        log.debug("REST request to update InMedicalAdvice : {}, {}", id, inMedicalAdvice);
        if (inMedicalAdvice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inMedicalAdvice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inMedicalAdviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InMedicalAdvice result = inMedicalAdviceRepository.save(inMedicalAdvice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inMedicalAdvice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /in-medical-advices/:id} : Partial updates given fields of an existing inMedicalAdvice, field will ignore if it is null
     *
     * @param id the id of the inMedicalAdvice to save.
     * @param inMedicalAdvice the inMedicalAdvice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inMedicalAdvice,
     * or with status {@code 400 (Bad Request)} if the inMedicalAdvice is not valid,
     * or with status {@code 404 (Not Found)} if the inMedicalAdvice is not found,
     * or with status {@code 500 (Internal Server Error)} if the inMedicalAdvice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/in-medical-advices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InMedicalAdvice> partialUpdateInMedicalAdvice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InMedicalAdvice inMedicalAdvice
    ) throws URISyntaxException {
        log.debug("REST request to partial update InMedicalAdvice partially : {}, {}", id, inMedicalAdvice);
        if (inMedicalAdvice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inMedicalAdvice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inMedicalAdviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InMedicalAdvice> result = inMedicalAdviceRepository
            .findById(inMedicalAdvice.getId())
            .map(existingInMedicalAdvice -> {
                if (inMedicalAdvice.getCureNumber() != null) {
                    existingInMedicalAdvice.setCureNumber(inMedicalAdvice.getCureNumber());
                }
                if (inMedicalAdvice.getCureName() != null) {
                    existingInMedicalAdvice.setCureName(inMedicalAdvice.getCureName());
                }
                if (inMedicalAdvice.getNorms() != null) {
                    existingInMedicalAdvice.setNorms(inMedicalAdvice.getNorms());
                }
                if (inMedicalAdvice.getUnit() != null) {
                    existingInMedicalAdvice.setUnit(inMedicalAdvice.getUnit());
                }
                if (inMedicalAdvice.getCharge() != null) {
                    existingInMedicalAdvice.setCharge(inMedicalAdvice.getCharge());
                }
                if (inMedicalAdvice.getPrice() != null) {
                    existingInMedicalAdvice.setPrice(inMedicalAdvice.getPrice());
                }
                if (inMedicalAdvice.getUseNumber() != null) {
                    existingInMedicalAdvice.setUseNumber(inMedicalAdvice.getUseNumber());
                }
                if (inMedicalAdvice.getStaffId() != null) {
                    existingInMedicalAdvice.setStaffId(inMedicalAdvice.getStaffId());
                }
                if (inMedicalAdvice.getCureId() != null) {
                    existingInMedicalAdvice.setCureId(inMedicalAdvice.getCureId());
                }
                if (inMedicalAdvice.getIdNum() != null) {
                    existingInMedicalAdvice.setIdNum(inMedicalAdvice.getIdNum());
                }
                if (inMedicalAdvice.getStartDoctor() != null) {
                    existingInMedicalAdvice.setStartDoctor(inMedicalAdvice.getStartDoctor());
                }
                if (inMedicalAdvice.getStartDepartment() != null) {
                    existingInMedicalAdvice.setStartDepartment(inMedicalAdvice.getStartDepartment());
                }
                if (inMedicalAdvice.getNurseDepartment() != null) {
                    existingInMedicalAdvice.setNurseDepartment(inMedicalAdvice.getNurseDepartment());
                }
                if (inMedicalAdvice.getStartTime() != null) {
                    existingInMedicalAdvice.setStartTime(inMedicalAdvice.getStartTime());
                }
                if (inMedicalAdvice.getStopTime() != null) {
                    existingInMedicalAdvice.setStopTime(inMedicalAdvice.getStopTime());
                }
                if (inMedicalAdvice.getNurseConfirmation() != null) {
                    existingInMedicalAdvice.setNurseConfirmation(inMedicalAdvice.getNurseConfirmation());
                }
                if (inMedicalAdvice.getState() != null) {
                    existingInMedicalAdvice.setState(inMedicalAdvice.getState());
                }
                if (inMedicalAdvice.getThisSystem() != null) {
                    existingInMedicalAdvice.setThisSystem(inMedicalAdvice.getThisSystem());
                }

                return existingInMedicalAdvice;
            })
            .map(inMedicalAdviceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inMedicalAdvice.getId().toString())
        );
    }

    /**
     * {@code GET  /in-medical-advices} : get all the inMedicalAdvices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inMedicalAdvices in body.
     */
    @GetMapping("/in-medical-advices")
    public List<InMedicalAdvice> getAllInMedicalAdvices() {
        log.debug("REST request to get all InMedicalAdvices");
        return inMedicalAdviceRepository.findAll();
    }

    /**
     * {@code GET  /in-medical-advices/:id} : get the "id" inMedicalAdvice.
     *
     * @param id the id of the inMedicalAdvice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inMedicalAdvice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/in-medical-advices/{id}")
    public ResponseEntity<InMedicalAdvice> getInMedicalAdvice(@PathVariable Long id) {
        log.debug("REST request to get InMedicalAdvice : {}", id);
        Optional<InMedicalAdvice> inMedicalAdvice = inMedicalAdviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(inMedicalAdvice);
    }

    /**
     * {@code DELETE  /in-medical-advices/:id} : delete the "id" inMedicalAdvice.
     *
     * @param id the id of the inMedicalAdvice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/in-medical-advices/{id}")
    public ResponseEntity<Void> deleteInMedicalAdvice(@PathVariable Long id) {
        log.debug("REST request to delete InMedicalAdvice : {}", id);
        inMedicalAdviceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
