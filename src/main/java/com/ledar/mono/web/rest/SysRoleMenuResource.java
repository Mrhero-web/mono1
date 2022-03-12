package com.ledar.mono.web.rest;

import com.ledar.mono.domain.SysRoleMenu;
import com.ledar.mono.repository.SysRoleMenuRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.SysRoleMenu}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SysRoleMenuResource {

    private final Logger log = LoggerFactory.getLogger(SysRoleMenuResource.class);

    private static final String ENTITY_NAME = "sysRoleMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysRoleMenuRepository sysRoleMenuRepository;

    public SysRoleMenuResource(SysRoleMenuRepository sysRoleMenuRepository) {
        this.sysRoleMenuRepository = sysRoleMenuRepository;
    }

    /**
     * {@code POST  /sys-role-menus} : Create a new sysRoleMenu.
     *
     * @param sysRoleMenu the sysRoleMenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysRoleMenu, or with status {@code 400 (Bad Request)} if the sysRoleMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sys-role-menus")
    public ResponseEntity<SysRoleMenu> createSysRoleMenu(@RequestBody SysRoleMenu sysRoleMenu) throws URISyntaxException {
        log.debug("REST request to save SysRoleMenu : {}", sysRoleMenu);
        if (sysRoleMenu.getId() != null) {
            throw new BadRequestAlertException("A new sysRoleMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysRoleMenu result = sysRoleMenuRepository.save(sysRoleMenu);
        return ResponseEntity
            .created(new URI("/api/sys-role-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-role-menus/:id} : Updates an existing sysRoleMenu.
     *
     * @param id the id of the sysRoleMenu to save.
     * @param sysRoleMenu the sysRoleMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleMenu,
     * or with status {@code 400 (Bad Request)} if the sysRoleMenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sys-role-menus/{id}")
    public ResponseEntity<SysRoleMenu> updateSysRoleMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysRoleMenu sysRoleMenu
    ) throws URISyntaxException {
        log.debug("REST request to update SysRoleMenu : {}, {}", id, sysRoleMenu);
        if (sysRoleMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysRoleMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysRoleMenu result = sysRoleMenuRepository.save(sysRoleMenu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysRoleMenu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sys-role-menus/:id} : Partial updates given fields of an existing sysRoleMenu, field will ignore if it is null
     *
     * @param id the id of the sysRoleMenu to save.
     * @param sysRoleMenu the sysRoleMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleMenu,
     * or with status {@code 400 (Bad Request)} if the sysRoleMenu is not valid,
     * or with status {@code 404 (Not Found)} if the sysRoleMenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sys-role-menus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SysRoleMenu> partialUpdateSysRoleMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysRoleMenu sysRoleMenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysRoleMenu partially : {}, {}", id, sysRoleMenu);
        if (sysRoleMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysRoleMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sysRoleMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysRoleMenu> result = sysRoleMenuRepository
            .findById(sysRoleMenu.getId())
            .map(existingSysRoleMenu -> {
                if (sysRoleMenu.getSysRoleId() != null) {
                    existingSysRoleMenu.setSysRoleId(sysRoleMenu.getSysRoleId());
                }
                if (sysRoleMenu.getSysMenuId() != null) {
                    existingSysRoleMenu.setSysMenuId(sysRoleMenu.getSysMenuId());
                }

                return existingSysRoleMenu;
            })
            .map(sysRoleMenuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sysRoleMenu.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-role-menus} : get all the sysRoleMenus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysRoleMenus in body.
     */
    @GetMapping("/sys-role-menus")
    public List<SysRoleMenu> getAllSysRoleMenus() {
        log.debug("REST request to get all SysRoleMenus");
        return sysRoleMenuRepository.findAll();
    }

    /**
     * {@code GET  /sys-role-menus/:id} : get the "id" sysRoleMenu.
     *
     * @param id the id of the sysRoleMenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysRoleMenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-role-menus/{id}")
    public ResponseEntity<SysRoleMenu> getSysRoleMenu(@PathVariable Long id) {
        log.debug("REST request to get SysRoleMenu : {}", id);
        Optional<SysRoleMenu> sysRoleMenu = sysRoleMenuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sysRoleMenu);
    }

    /**
     * {@code DELETE  /sys-role-menus/:id} : delete the "id" sysRoleMenu.
     *
     * @param id the id of the sysRoleMenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sys-role-menus/{id}")
    public ResponseEntity<Void> deleteSysRoleMenu(@PathVariable Long id) {
        log.debug("REST request to delete SysRoleMenu : {}", id);
        sysRoleMenuRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
