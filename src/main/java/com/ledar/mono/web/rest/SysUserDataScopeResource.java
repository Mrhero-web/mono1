package com.ledar.mono.web.rest;

import com.ledar.mono.domain.SysUserDataScope;
import com.ledar.mono.repository.SysUserDataScopeRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.SysUserDataScope}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SysUserDataScopeResource {

    private final Logger log = LoggerFactory.getLogger(SysUserDataScopeResource.class);

    private static final String ENTITY_NAME = "sysUserDataScope";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysUserDataScopeRepository sysUserDataScopeRepository;

    public SysUserDataScopeResource(SysUserDataScopeRepository sysUserDataScopeRepository) {
        this.sysUserDataScopeRepository = sysUserDataScopeRepository;
    }

    /**
     * {@code POST  /sys-user-data-scopes} : Create a new sysUserDataScope.
     *
     * @param sysUserDataScope the sysUserDataScope to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysUserDataScope, or with status {@code 400 (Bad Request)} if the sysUserDataScope has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-user-data-scopes")
    public ResponseEntity<SysUserDataScope> createSysUserDataScope(@RequestBody SysUserDataScope sysUserDataScope)
        throws URISyntaxException {
        log.debug("REST request to save SysUserDataScope : {}", sysUserDataScope);
        if (sysUserDataScope.getId() != null) {
            throw new BadRequestAlertException("A new sysUserDataScope cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysUserDataScope result = sysUserDataScopeRepository.save(sysUserDataScope);
        return ResponseEntity
            .created(new URI("/api/sys-user-data-scopes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-user-data-scopes/:id} : Updates an existing sysUserDataScope.
     *
     * @param id the id of the sysUserDataScope to save.
     * @param sysUserDataScope the sysUserDataScope to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysUserDataScope,
     * or with status {@code 400 (Bad Request)} if the sysUserDataScope is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysUserDataScope couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-user-data-scopes/{id}")
    public ResponseEntity<SysUserDataScope> updateSysUserDataScope(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysUserDataScope sysUserDataScope
    ) throws URISyntaxException {
        log.debug("REST request to update SysUserDataScope : {}, {}", id, sysUserDataScope);
        if (sysUserDataScope.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysUserDataScope.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysUserDataScopeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysUserDataScope result = sysUserDataScopeRepository.save(sysUserDataScope);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysUserDataScope.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sys-user-data-scopes/:id} : Partial updates given fields of an existing sysUserDataScope, field will ignore if it is null
     *
     * @param id the id of the sysUserDataScope to save.
     * @param sysUserDataScope the sysUserDataScope to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysUserDataScope,
     * or with status {@code 400 (Bad Request)} if the sysUserDataScope is not valid,
     * or with status {@code 404 (Not Found)} if the sysUserDataScope is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysUserDataScope couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sys-user-data-scopes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SysUserDataScope> partialUpdateSysUserDataScope(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysUserDataScope sysUserDataScope
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysUserDataScope partially : {}, {}", id, sysUserDataScope);
        if (sysUserDataScope.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysUserDataScope.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysUserDataScopeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysUserDataScope> result = sysUserDataScopeRepository
            .findById(sysUserDataScope.getId())
            .map(existingSysUserDataScope -> {
                if (sysUserDataScope.getSysUserId() != null) {
                    existingSysUserDataScope.setSysUserId(sysUserDataScope.getSysUserId());
                }
                if (sysUserDataScope.getCtrlType() != null) {
                    existingSysUserDataScope.setCtrlType(sysUserDataScope.getCtrlType());
                }
                if (sysUserDataScope.getCtrlData() != null) {
                    existingSysUserDataScope.setCtrlData(sysUserDataScope.getCtrlData());
                }
                if (sysUserDataScope.getCtrlPermit() != null) {
                    existingSysUserDataScope.setCtrlPermit(sysUserDataScope.getCtrlPermit());
                }

                return existingSysUserDataScope;
            })
            .map(sysUserDataScopeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysUserDataScope.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-user-data-scopes} : get all the sysUserDataScopes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysUserDataScopes in body.
     */
    @GetMapping("/sys-user-data-scopes")
    public List<SysUserDataScope> getAllSysUserDataScopes() {
        log.debug("REST request to get all SysUserDataScopes");
        return sysUserDataScopeRepository.findAll();
    }

    /**
     * {@code GET  /sys-user-data-scopes/:id} : get the "id" sysUserDataScope.
     *
     * @param id the id of the sysUserDataScope to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysUserDataScope, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-user-data-scopes/{id}")
    public ResponseEntity<SysUserDataScope> getSysUserDataScope(@PathVariable Long id) {
        log.debug("REST request to get SysUserDataScope : {}", id);
        Optional<SysUserDataScope> sysUserDataScope = sysUserDataScopeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sysUserDataScope);
    }

    /**
     * {@code DELETE  /sys-user-data-scopes/:id} : delete the "id" sysUserDataScope.
     *
     * @param id the id of the sysUserDataScope to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-user-data-scopes/{id}")
    public ResponseEntity<Void> deleteSysUserDataScope(@PathVariable Long id) {
        log.debug("REST request to delete SysUserDataScope : {}", id);
        sysUserDataScopeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
