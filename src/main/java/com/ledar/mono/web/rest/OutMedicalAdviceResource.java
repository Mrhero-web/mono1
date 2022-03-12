package com.ledar.mono.web.rest;

import com.ledar.mono.domain.OutMedicalAdvice;
import com.ledar.mono.repository.OutMedicalAdviceRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.OutMedicalAdvice}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OutMedicalAdviceResource {

    private final Logger log = LoggerFactory.getLogger(OutMedicalAdviceResource.class);

    private static final String ENTITY_NAME = "outMedicalAdvice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutMedicalAdviceRepository outMedicalAdviceRepository;

    public OutMedicalAdviceResource(OutMedicalAdviceRepository outMedicalAdviceRepository) {
        this.outMedicalAdviceRepository = outMedicalAdviceRepository;
    }

    /**
     * {@code POST  /out-medical-advices} : Create a new outMedicalAdvice.
     *
     * @param outMedicalAdvice the outMedicalAdvice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outMedicalAdvice, or with status {@code 400 (Bad Request)} if the outMedicalAdvice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/out-medical-advices")
    public ResponseEntity<OutMedicalAdvice> createOutMedicalAdvice(@RequestBody OutMedicalAdvice outMedicalAdvice)
        throws URISyntaxException {
        log.debug("REST request to save OutMedicalAdvice : {}", outMedicalAdvice);
        if (outMedicalAdvice.getId() != null) {
            throw new BadRequestAlertException("A new outMedicalAdvice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutMedicalAdvice result = outMedicalAdviceRepository.save(outMedicalAdvice);
        return ResponseEntity
            .created(new URI("/api/out-medical-advices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /out-medical-advices/:id} : Updates an existing outMedicalAdvice.
     *
     * @param id the id of the outMedicalAdvice to save.
     * @param outMedicalAdvice the outMedicalAdvice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outMedicalAdvice,
     * or with status {@code 400 (Bad Request)} if the outMedicalAdvice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outMedicalAdvice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/out-medical-advices/{id}")
    public ResponseEntity<OutMedicalAdvice> updateOutMedicalAdvice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OutMedicalAdvice outMedicalAdvice
    ) throws URISyntaxException {
        log.debug("REST request to update OutMedicalAdvice : {}, {}", id, outMedicalAdvice);
        if (outMedicalAdvice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outMedicalAdvice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outMedicalAdviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutMedicalAdvice result = outMedicalAdviceRepository.save(outMedicalAdvice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outMedicalAdvice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /out-medical-advices/:id} : Partial updates given fields of an existing outMedicalAdvice, field will ignore if it is null
     *
     * @param id the id of the outMedicalAdvice to save.
     * @param outMedicalAdvice the outMedicalAdvice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outMedicalAdvice,
     * or with status {@code 400 (Bad Request)} if the outMedicalAdvice is not valid,
     * or with status {@code 404 (Not Found)} if the outMedicalAdvice is not found,
     * or with status {@code 500 (Internal Server Error)} if the outMedicalAdvice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/out-medical-advices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutMedicalAdvice> partialUpdateOutMedicalAdvice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OutMedicalAdvice outMedicalAdvice
    ) throws URISyntaxException {
        log.debug("REST request to partial update OutMedicalAdvice partially : {}, {}", id, outMedicalAdvice);
        if (outMedicalAdvice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outMedicalAdvice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outMedicalAdviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutMedicalAdvice> result = outMedicalAdviceRepository
            .findById(outMedicalAdvice.getId())
            .map(existingOutMedicalAdvice -> {
                if (outMedicalAdvice.getCureNumber() != null) {
                    existingOutMedicalAdvice.setCureNumber(outMedicalAdvice.getCureNumber());
                }
                if (outMedicalAdvice.getCureName() != null) {
                    existingOutMedicalAdvice.setCureName(outMedicalAdvice.getCureName());
                }
                if (outMedicalAdvice.getNorms() != null) {
                    existingOutMedicalAdvice.setNorms(outMedicalAdvice.getNorms());
                }
                if (outMedicalAdvice.getUnit() != null) {
                    existingOutMedicalAdvice.setUnit(outMedicalAdvice.getUnit());
                }
                if (outMedicalAdvice.getCharge() != null) {
                    existingOutMedicalAdvice.setCharge(outMedicalAdvice.getCharge());
                }
                if (outMedicalAdvice.getPrice() != null) {
                    existingOutMedicalAdvice.setPrice(outMedicalAdvice.getPrice());
                }
                if (outMedicalAdvice.getUseNumber() != null) {
                    existingOutMedicalAdvice.setUseNumber(outMedicalAdvice.getUseNumber());
                }
                if (outMedicalAdvice.getStaffId() != null) {
                    existingOutMedicalAdvice.setStaffId(outMedicalAdvice.getStaffId());
                }
                if (outMedicalAdvice.getCureId() != null) {
                    existingOutMedicalAdvice.setCureId(outMedicalAdvice.getCureId());
                }
                if (outMedicalAdvice.getIdNum() != null) {
                    existingOutMedicalAdvice.setIdNum(outMedicalAdvice.getIdNum());
                }
                if (outMedicalAdvice.getStartDoctor() != null) {
                    existingOutMedicalAdvice.setStartDoctor(outMedicalAdvice.getStartDoctor());
                }
                if (outMedicalAdvice.getStartDepartment() != null) {
                    existingOutMedicalAdvice.setStartDepartment(outMedicalAdvice.getStartDepartment());
                }
                if (outMedicalAdvice.getNurseDepartment() != null) {
                    existingOutMedicalAdvice.setNurseDepartment(outMedicalAdvice.getNurseDepartment());
                }
                if (outMedicalAdvice.getStartTime() != null) {
                    existingOutMedicalAdvice.setStartTime(outMedicalAdvice.getStartTime());
                }
                if (outMedicalAdvice.getStopTime() != null) {
                    existingOutMedicalAdvice.setStopTime(outMedicalAdvice.getStopTime());
                }
                if (outMedicalAdvice.getNurseConfirmation() != null) {
                    existingOutMedicalAdvice.setNurseConfirmation(outMedicalAdvice.getNurseConfirmation());
                }
                if (outMedicalAdvice.getState() != null) {
                    existingOutMedicalAdvice.setState(outMedicalAdvice.getState());
                }
                if (outMedicalAdvice.getThisSystem() != null) {
                    existingOutMedicalAdvice.setThisSystem(outMedicalAdvice.getThisSystem());
                }

                return existingOutMedicalAdvice;
            })
            .map(outMedicalAdviceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outMedicalAdvice.getId().toString())
        );
    }

    /**
     * {@code GET  /out-medical-advices} : get all the outMedicalAdvices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outMedicalAdvices in body.
     */
    @GetMapping("/out-medical-advices")
    public List<OutMedicalAdvice> getAllOutMedicalAdvices() {
        log.debug("REST request to get all OutMedicalAdvices");
        return outMedicalAdviceRepository.findAll();
    }

    /**
     * {@code GET  /out-medical-advices/:id} : get the "id" outMedicalAdvice.
     *
     * @param id the id of the outMedicalAdvice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outMedicalAdvice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/out-medical-advices/{id}")
    public ResponseEntity<OutMedicalAdvice> getOutMedicalAdvice(@PathVariable Long id) {
        log.debug("REST request to get OutMedicalAdvice : {}", id);
        Optional<OutMedicalAdvice> outMedicalAdvice = outMedicalAdviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(outMedicalAdvice);
    }

    /**
     * {@code DELETE  /out-medical-advices/:id} : delete the "id" outMedicalAdvice.
     *
     * @param id the id of the outMedicalAdvice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/out-medical-advices/{id}")
    public ResponseEntity<Void> deleteOutMedicalAdvice(@PathVariable Long id) {
        log.debug("REST request to delete OutMedicalAdvice : {}", id);
        outMedicalAdviceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
