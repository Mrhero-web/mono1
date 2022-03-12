package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.SysRoleMenu;
import com.ledar.mono.repository.SysRoleMenuRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SysRoleMenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysRoleMenuResourceIT {

    private static final Long DEFAULT_SYS_ROLE_ID = 1L;
    private static final Long UPDATED_SYS_ROLE_ID = 2L;

    private static final Long DEFAULT_SYS_MENU_ID = 1L;
    private static final Long UPDATED_SYS_MENU_ID = 2L;

    private static final String ENTITY_API_URL = "/api/sys-role-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSysRoleMenuMockMvc;

    private SysRoleMenu sysRoleMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRoleMenu createEntity(EntityManager em) {
        SysRoleMenu sysRoleMenu = new SysRoleMenu().sysRoleId(DEFAULT_SYS_ROLE_ID).sysMenuId(DEFAULT_SYS_MENU_ID);
        return sysRoleMenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRoleMenu createUpdatedEntity(EntityManager em) {
        SysRoleMenu sysRoleMenu = new SysRoleMenu().sysRoleId(UPDATED_SYS_ROLE_ID).sysMenuId(UPDATED_SYS_MENU_ID);
        return sysRoleMenu;
    }

    @BeforeEach
    public void initTest() {
        sysRoleMenu = createEntity(em);
    }

    @Test
    @Transactional
    void createSysRoleMenu() throws Exception {
        int databaseSizeBeforeCreate = sysRoleMenuRepository.findAll().size();
        // Create the SysRoleMenu
        restSysRoleMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleMenu)))
            .andExpect(status().isCreated());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeCreate + 1);
        SysRoleMenu testSysRoleMenu = sysRoleMenuList.get(sysRoleMenuList.size() - 1);
        assertThat(testSysRoleMenu.getSysRoleId()).isEqualTo(DEFAULT_SYS_ROLE_ID);
        assertThat(testSysRoleMenu.getSysMenuId()).isEqualTo(DEFAULT_SYS_MENU_ID);
    }

    @Test
    @Transactional
    void createSysRoleMenuWithExistingId() throws Exception {
        // Create the SysRoleMenu with an existing ID
        sysRoleMenu.setId(1L);

        int databaseSizeBeforeCreate = sysRoleMenuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysRoleMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleMenu)))
            .andExpect(status().isBadRequest());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysRoleMenus() throws Exception {
        // Initialize the database
        sysRoleMenuRepository.saveAndFlush(sysRoleMenu);

        // Get all the sysRoleMenuList
        restSysRoleMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysRoleMenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].sysRoleId").value(hasItem(DEFAULT_SYS_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].sysMenuId").value(hasItem(DEFAULT_SYS_MENU_ID.intValue())));
    }

    @Test
    @Transactional
    void getSysRoleMenu() throws Exception {
        // Initialize the database
        sysRoleMenuRepository.saveAndFlush(sysRoleMenu);

        // Get the sysRoleMenu
        restSysRoleMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, sysRoleMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysRoleMenu.getId().intValue()))
            .andExpect(jsonPath("$.sysRoleId").value(DEFAULT_SYS_ROLE_ID.intValue()))
            .andExpect(jsonPath("$.sysMenuId").value(DEFAULT_SYS_MENU_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSysRoleMenu() throws Exception {
        // Get the sysRoleMenu
        restSysRoleMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSysRoleMenu() throws Exception {
        // Initialize the database
        sysRoleMenuRepository.saveAndFlush(sysRoleMenu);

        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();

        // Update the sysRoleMenu
        SysRoleMenu updatedSysRoleMenu = sysRoleMenuRepository.findById(sysRoleMenu.getId()).get();
        // Disconnect from session so that the updates on updatedSysRoleMenu are not directly saved in db
        em.detach(updatedSysRoleMenu);
        updatedSysRoleMenu.sysRoleId(UPDATED_SYS_ROLE_ID).sysMenuId(UPDATED_SYS_MENU_ID);

        restSysRoleMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSysRoleMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSysRoleMenu))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
        SysRoleMenu testSysRoleMenu = sysRoleMenuList.get(sysRoleMenuList.size() - 1);
        assertThat(testSysRoleMenu.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleMenu.getSysMenuId()).isEqualTo(UPDATED_SYS_MENU_ID);
    }

    @Test
    @Transactional
    void putNonExistingSysRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();
        sysRoleMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysRoleMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysRoleMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();
        sysRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();
        sysRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleMenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysRoleMenuWithPatch() throws Exception {
        // Initialize the database
        sysRoleMenuRepository.saveAndFlush(sysRoleMenu);

        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();

        // Update the sysRoleMenu using partial update
        SysRoleMenu partialUpdatedSysRoleMenu = new SysRoleMenu();
        partialUpdatedSysRoleMenu.setId(sysRoleMenu.getId());

        partialUpdatedSysRoleMenu.sysMenuId(UPDATED_SYS_MENU_ID);

        restSysRoleMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysRoleMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysRoleMenu))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
        SysRoleMenu testSysRoleMenu = sysRoleMenuList.get(sysRoleMenuList.size() - 1);
        assertThat(testSysRoleMenu.getSysRoleId()).isEqualTo(DEFAULT_SYS_ROLE_ID);
        assertThat(testSysRoleMenu.getSysMenuId()).isEqualTo(UPDATED_SYS_MENU_ID);
    }

    @Test
    @Transactional
    void fullUpdateSysRoleMenuWithPatch() throws Exception {
        // Initialize the database
        sysRoleMenuRepository.saveAndFlush(sysRoleMenu);

        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();

        // Update the sysRoleMenu using partial update
        SysRoleMenu partialUpdatedSysRoleMenu = new SysRoleMenu();
        partialUpdatedSysRoleMenu.setId(sysRoleMenu.getId());

        partialUpdatedSysRoleMenu.sysRoleId(UPDATED_SYS_ROLE_ID).sysMenuId(UPDATED_SYS_MENU_ID);

        restSysRoleMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysRoleMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysRoleMenu))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
        SysRoleMenu testSysRoleMenu = sysRoleMenuList.get(sysRoleMenuList.size() - 1);
        assertThat(testSysRoleMenu.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleMenu.getSysMenuId()).isEqualTo(UPDATED_SYS_MENU_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSysRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();
        sysRoleMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysRoleMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysRoleMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();
        sysRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysRoleMenu() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleMenuRepository.findAll().size();
        sysRoleMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleMenuMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sysRoleMenu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysRoleMenu in the database
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysRoleMenu() throws Exception {
        // Initialize the database
        sysRoleMenuRepository.saveAndFlush(sysRoleMenu);

        int databaseSizeBeforeDelete = sysRoleMenuRepository.findAll().size();

        // Delete the sysRoleMenu
        restSysRoleMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysRoleMenu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuRepository.findAll();
        assertThat(sysRoleMenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
