package com.ledar.mono.web.rest;

import com.ledar.mono.domain.SysRoleDataScope;
import com.ledar.mono.repository.SysRoleDataScopeRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.SysRoleDataScope}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SysRoleDataScopeResource {

    private final Logger log = LoggerFactory.getLogger(SysRoleDataScopeResource.class);

    private static final String ENTITY_NAME = "sysRoleDataScope";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysRoleDataScopeRepository sysRoleDataScopeRepository;

    public SysRoleDataScopeResource(SysRoleDataScopeRepository sysRoleDataScopeRepository) {
        this.sysRoleDataScopeRepository = sysRoleDataScopeRepository;
    }

    /**
     * {@code POST  /sys-role-data-scopes} : Create a new sysRoleDataScope.
     *
     * @param sysRoleDataScope the sysRoleDataScope to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysRoleDataScope, or with status {@code 400 (Bad Request)} if the sysRoleDataScope has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-role-data-scopes")
    public ResponseEntity<SysRoleDataScope> createSysRoleDataScope(@RequestBody SysRoleDataScope sysRoleDataScope)
        throws URISyntaxException {
        log.debug("REST request to save SysRoleDataScope : {}", sysRoleDataScope);
        if (sysRoleDataScope.getId() != null) {
            throw new BadRequestAlertException("A new sysRoleDataScope cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysRoleDataScope result = sysRoleDataScopeRepository.save(sysRoleDataScope);
        return ResponseEntity
            .created(new URI("/api/sys-role-data-scopes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-role-data-scopes/:id} : Updates an existing sysRoleDataScope.
     *
     * @param id the id of the sysRoleDataScope to save.
     * @param sysRoleDataScope the sysRoleDataScope to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleDataScope,
     * or with status {@code 400 (Bad Request)} if the sysRoleDataScope is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleDataScope couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-role-data-scopes/{id}")
    public ResponseEntity<SysRoleDataScope> updateSysRoleDataScope(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysRoleDataScope sysRoleDataScope
    ) throws URISyntaxException {
        log.debug("REST request to update SysRoleDataScope : {}, {}", id, sysRoleDataScope);
        if (sysRoleDataScope.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleDataScope.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysRoleDataScopeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysRoleDataScope result = sysRoleDataScopeRepository.save(sysRoleDataScope);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysRoleDataScope.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sys-role-data-scopes/:id} : Partial updates given fields of an existing sysRoleDataScope, field will ignore if it is null
     *
     * @param id the id of the sysRoleDataScope to save.
     * @param sysRoleDataScope the sysRoleDataScope to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleDataScope,
     * or with status {@code 400 (Bad Request)} if the sysRoleDataScope is not valid,
     * or with status {@code 404 (Not Found)} if the sysRoleDataScope is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleDataScope couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sys-role-data-scopes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SysRoleDataScope> partialUpdateSysRoleDataScope(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysRoleDataScope sysRoleDataScope
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysRoleDataScope partially : {}, {}", id, sysRoleDataScope);
        if (sysRoleDataScope.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleDataScope.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysRoleDataScopeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysRoleDataScope> result = sysRoleDataScopeRepository
            .findById(sysRoleDataScope.getId())
            .map(existingSysRoleDataScope -> {
                if (sysRoleDataScope.getSysRoleId() != null) {
                    existingSysRoleDataScope.setSysRoleId(sysRoleDataScope.getSysRoleId());
                }
                if (sysRoleDataScope.getCtrlType() != null) {
                    existingSysRoleDataScope.setCtrlType(sysRoleDataScope.getCtrlType());
                }
                if (sysRoleDataScope.getCtrlData() != null) {
                    existingSysRoleDataScope.setCtrlData(sysRoleDataScope.getCtrlData());
                }
                if (sysRoleDataScope.getCtrlPermit() != null) {
                    existingSysRoleDataScope.setCtrlPermit(sysRoleDataScope.getCtrlPermit());
                }

                return existingSysRoleDataScope;
            })
            .map(sysRoleDataScopeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysRoleDataScope.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-role-data-scopes} : get all the sysRoleDataScopes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysRoleDataScopes in body.
     */
    @GetMapping("/sys-role-data-scopes")
    public List<SysRoleDataScope> getAllSysRoleDataScopes() {
        log.debug("REST request to get all SysRoleDataScopes");
        return sysRoleDataScopeRepository.findAll();
    }

    /**
     * {@code GET  /sys-role-data-scopes/:id} : get the "id" sysRoleDataScope.
     *
     * @param id the id of the sysRoleDataScope to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysRoleDataScope, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-role-data-scopes/{id}")
    public ResponseEntity<SysRoleDataScope> getSysRoleDataScope(@PathVariable Long id) {
        log.debug("REST request to get SysRoleDataScope : {}", id);
        Optional<SysRoleDataScope> sysRoleDataScope = sysRoleDataScopeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sysRoleDataScope);
    }

    /**
     * {@code DELETE  /sys-role-data-scopes/:id} : delete the "id" sysRoleDataScope.
     *
     * @param id the id of the sysRoleDataScope to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-role-data-scopes/{id}")
    public ResponseEntity<Void> deleteSysRoleDataScope(@PathVariable Long id) {
        log.debug("REST request to delete SysRoleDataScope : {}", id);
        sysRoleDataScopeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
