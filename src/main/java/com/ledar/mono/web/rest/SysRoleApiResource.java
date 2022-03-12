package com.ledar.mono.web.rest;

import com.ledar.mono.domain.SysRoleApi;
import com.ledar.mono.repository.SysRoleApiRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.SysRoleApi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SysRoleApiResource {

    private final Logger log = LoggerFactory.getLogger(SysRoleApiResource.class);

    private static final String ENTITY_NAME = "sysRoleApi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysRoleApiRepository sysRoleApiRepository;

    public SysRoleApiResource(SysRoleApiRepository sysRoleApiRepository) {
        this.sysRoleApiRepository = sysRoleApiRepository;
    }

    /**
     * {@code POST  /sys-role-apis} : Create a new sysRoleApi.
     *
     * @param sysRoleApi the sysRoleApi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysRoleApi, or with status {@code 400 (Bad Request)} if the sysRoleApi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-role-apis")
    public ResponseEntity<SysRoleApi> createSysRoleApi(@RequestBody SysRoleApi sysRoleApi) throws URISyntaxException {
        log.debug("REST request to save SysRoleApi : {}", sysRoleApi);
        if (sysRoleApi.getId() != null) {
            throw new BadRequestAlertException("A new sysRoleApi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysRoleApi result = sysRoleApiRepository.save(sysRoleApi);
        return ResponseEntity
            .created(new URI("/api/sys-role-apis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-role-apis/:id} : Updates an existing sysRoleApi.
     *
     * @param id the id of the sysRoleApi to save.
     * @param sysRoleApi the sysRoleApi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleApi,
     * or with status {@code 400 (Bad Request)} if the sysRoleApi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleApi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-role-apis/{id}")
    public ResponseEntity<SysRoleApi> updateSysRoleApi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysRoleApi sysRoleApi
    ) throws URISyntaxException {
        log.debug("REST request to update SysRoleApi : {}, {}", id, sysRoleApi);
        if (sysRoleApi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleApi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysRoleApiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysRoleApi result = sysRoleApiRepository.save(sysRoleApi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysRoleApi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sys-role-apis/:id} : Partial updates given fields of an existing sysRoleApi, field will ignore if it is null
     *
     * @param id the id of the sysRoleApi to save.
     * @param sysRoleApi the sysRoleApi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleApi,
     * or with status {@code 400 (Bad Request)} if the sysRoleApi is not valid,
     * or with status {@code 404 (Not Found)} if the sysRoleApi is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleApi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sys-role-apis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SysRoleApi> partialUpdateSysRoleApi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysRoleApi sysRoleApi
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysRoleApi partially : {}, {}", id, sysRoleApi);
        if (sysRoleApi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleApi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysRoleApiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysRoleApi> result = sysRoleApiRepository
            .findById(sysRoleApi.getId())
            .map(existingSysRoleApi -> {
                if (sysRoleApi.getSysRoleId() != null) {
                    existingSysRoleApi.setSysRoleId(sysRoleApi.getSysRoleId());
                }
                if (sysRoleApi.getSysApiId() != null) {
                    existingSysRoleApi.setSysApiId(sysRoleApi.getSysApiId());
                }

                return existingSysRoleApi;
            })
            .map(sysRoleApiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysRoleApi.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-role-apis} : get all the sysRoleApis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysRoleApis in body.
     */
    @GetMapping("/sys-role-apis")
    public List<SysRoleApi> getAllSysRoleApis() {
        log.debug("REST request to get all SysRoleApis");
        return sysRoleApiRepository.findAll();
    }

    /**
     * {@code GET  /sys-role-apis/:id} : get the "id" sysRoleApi.
     *
     * @param id the id of the sysRoleApi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysRoleApi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-role-apis/{id}")
    public ResponseEntity<SysRoleApi> getSysRoleApi(@PathVariable Long id) {
        log.debug("REST request to get SysRoleApi : {}", id);
        Optional<SysRoleApi> sysRoleApi = sysRoleApiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sysRoleApi);
    }

    /**
     * {@code DELETE  /sys-role-apis/:id} : delete the "id" sysRoleApi.
     *
     * @param id the id of the sysRoleApi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-role-apis/{id}")
    public ResponseEntity<Void> deleteSysRoleApi(@PathVariable Long id) {
        log.debug("REST request to delete SysRoleApi : {}", id);
        sysRoleApiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
