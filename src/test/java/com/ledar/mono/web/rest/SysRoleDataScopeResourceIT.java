package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.SysRoleDataScope;
import com.ledar.mono.domain.enumeration.CtrlType;
import com.ledar.mono.repository.SysRoleDataScopeRepository;
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
 * Integration tests for the {@link SysRoleDataScopeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysRoleDataScopeResourceIT {

    private static final Long DEFAULT_SYS_ROLE_ID = 1L;
    private static final Long UPDATED_SYS_ROLE_ID = 2L;

    private static final CtrlType DEFAULT_CTRL_TYPE = CtrlType.COMPANY;
    private static final CtrlType UPDATED_CTRL_TYPE = CtrlType.OFFICE;

    private static final String DEFAULT_CTRL_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CTRL_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_CTRL_PERMIT = "AAAAAAAAAA";
    private static final String UPDATED_CTRL_PERMIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sys-role-data-scopes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysRoleDataScopeRepository sysRoleDataScopeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSysRoleDataScopeMockMvc;

    private SysRoleDataScope sysRoleDataScope;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRoleDataScope createEntity(EntityManager em) {
        SysRoleDataScope sysRoleDataScope = new SysRoleDataScope()
            .sysRoleId(DEFAULT_SYS_ROLE_ID)
            .ctrlType(DEFAULT_CTRL_TYPE)
            .ctrlData(DEFAULT_CTRL_DATA)
            .ctrlPermit(DEFAULT_CTRL_PERMIT);
        return sysRoleDataScope;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRoleDataScope createUpdatedEntity(EntityManager em) {
        SysRoleDataScope sysRoleDataScope = new SysRoleDataScope()
            .sysRoleId(UPDATED_SYS_ROLE_ID)
            .ctrlType(UPDATED_CTRL_TYPE)
            .ctrlData(UPDATED_CTRL_DATA)
            .ctrlPermit(UPDATED_CTRL_PERMIT);
        return sysRoleDataScope;
    }

    @BeforeEach
    public void initTest() {
        sysRoleDataScope = createEntity(em);
    }

    @Test
    @Transactional
    void createSysRoleDataScope() throws Exception {
        int databaseSizeBeforeCreate = sysRoleDataScopeRepository.findAll().size();
        // Create the SysRoleDataScope
        restSysRoleDataScopeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isCreated());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeCreate + 1);
        SysRoleDataScope testSysRoleDataScope = sysRoleDataScopeList.get(sysRoleDataScopeList.size() - 1);
        assertThat(testSysRoleDataScope.getSysRoleId()).isEqualTo(DEFAULT_SYS_ROLE_ID);
        assertThat(testSysRoleDataScope.getCtrlType()).isEqualTo(DEFAULT_CTRL_TYPE);
        assertThat(testSysRoleDataScope.getCtrlData()).isEqualTo(DEFAULT_CTRL_DATA);
        assertThat(testSysRoleDataScope.getCtrlPermit()).isEqualTo(DEFAULT_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void createSysRoleDataScopeWithExistingId() throws Exception {
        // Create the SysRoleDataScope with an existing ID
        sysRoleDataScope.setId(1L);

        int databaseSizeBeforeCreate = sysRoleDataScopeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysRoleDataScopeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysRoleDataScopes() throws Exception {
        // Initialize the database
        sysRoleDataScopeRepository.saveAndFlush(sysRoleDataScope);

        // Get all the sysRoleDataScopeList
        restSysRoleDataScopeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysRoleDataScope.getId().intValue())))
            .andExpect(jsonPath("$.[*].sysRoleId").value(hasItem(DEFAULT_SYS_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].ctrlType").value(hasItem(DEFAULT_CTRL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ctrlData").value(hasItem(DEFAULT_CTRL_DATA)))
            .andExpect(jsonPath("$.[*].ctrlPermit").value(hasItem(DEFAULT_CTRL_PERMIT)));
    }

    @Test
    @Transactional
    void getSysRoleDataScope() throws Exception {
        // Initialize the database
        sysRoleDataScopeRepository.saveAndFlush(sysRoleDataScope);

        // Get the sysRoleDataScope
        restSysRoleDataScopeMockMvc
            .perform(get(ENTITY_API_URL_ID, sysRoleDataScope.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysRoleDataScope.getId().intValue()))
            .andExpect(jsonPath("$.sysRoleId").value(DEFAULT_SYS_ROLE_ID.intValue()))
            .andExpect(jsonPath("$.ctrlType").value(DEFAULT_CTRL_TYPE.toString()))
            .andExpect(jsonPath("$.ctrlData").value(DEFAULT_CTRL_DATA))
            .andExpect(jsonPath("$.ctrlPermit").value(DEFAULT_CTRL_PERMIT));
    }

    @Test
    @Transactional
    void getNonExistingSysRoleDataScope() throws Exception {
        // Get the sysRoleDataScope
        restSysRoleDataScopeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSysRoleDataScope() throws Exception {
        // Initialize the database
        sysRoleDataScopeRepository.saveAndFlush(sysRoleDataScope);

        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();

        // Update the sysRoleDataScope
        SysRoleDataScope updatedSysRoleDataScope = sysRoleDataScopeRepository.findById(sysRoleDataScope.getId()).get();
        // Disconnect from session so that the updates on updatedSysRoleDataScope are not directly saved in db
        em.detach(updatedSysRoleDataScope);
        updatedSysRoleDataScope
            .sysRoleId(UPDATED_SYS_ROLE_ID)
            .ctrlType(UPDATED_CTRL_TYPE)
            .ctrlData(UPDATED_CTRL_DATA)
            .ctrlPermit(UPDATED_CTRL_PERMIT);

        restSysRoleDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSysRoleDataScope.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSysRoleDataScope))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
        SysRoleDataScope testSysRoleDataScope = sysRoleDataScopeList.get(sysRoleDataScopeList.size() - 1);
        assertThat(testSysRoleDataScope.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleDataScope.getCtrlType()).isEqualTo(UPDATED_CTRL_TYPE);
        assertThat(testSysRoleDataScope.getCtrlData()).isEqualTo(UPDATED_CTRL_DATA);
        assertThat(testSysRoleDataScope.getCtrlPermit()).isEqualTo(UPDATED_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void putNonExistingSysRoleDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();
        sysRoleDataScope.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysRoleDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysRoleDataScope.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysRoleDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();
        sysRoleDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysRoleDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();
        sysRoleDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysRoleDataScopeWithPatch() throws Exception {
        // Initialize the database
        sysRoleDataScopeRepository.saveAndFlush(sysRoleDataScope);

        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();

        // Update the sysRoleDataScope using partial update
        SysRoleDataScope partialUpdatedSysRoleDataScope = new SysRoleDataScope();
        partialUpdatedSysRoleDataScope.setId(sysRoleDataScope.getId());

        partialUpdatedSysRoleDataScope.sysRoleId(UPDATED_SYS_ROLE_ID).ctrlType(UPDATED_CTRL_TYPE);

        restSysRoleDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysRoleDataScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysRoleDataScope))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
        SysRoleDataScope testSysRoleDataScope = sysRoleDataScopeList.get(sysRoleDataScopeList.size() - 1);
        assertThat(testSysRoleDataScope.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleDataScope.getCtrlType()).isEqualTo(UPDATED_CTRL_TYPE);
        assertThat(testSysRoleDataScope.getCtrlData()).isEqualTo(DEFAULT_CTRL_DATA);
        assertThat(testSysRoleDataScope.getCtrlPermit()).isEqualTo(DEFAULT_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void fullUpdateSysRoleDataScopeWithPatch() throws Exception {
        // Initialize the database
        sysRoleDataScopeRepository.saveAndFlush(sysRoleDataScope);

        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();

        // Update the sysRoleDataScope using partial update
        SysRoleDataScope partialUpdatedSysRoleDataScope = new SysRoleDataScope();
        partialUpdatedSysRoleDataScope.setId(sysRoleDataScope.getId());

        partialUpdatedSysRoleDataScope
            .sysRoleId(UPDATED_SYS_ROLE_ID)
            .ctrlType(UPDATED_CTRL_TYPE)
            .ctrlData(UPDATED_CTRL_DATA)
            .ctrlPermit(UPDATED_CTRL_PERMIT);

        restSysRoleDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysRoleDataScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysRoleDataScope))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
        SysRoleDataScope testSysRoleDataScope = sysRoleDataScopeList.get(sysRoleDataScopeList.size() - 1);
        assertThat(testSysRoleDataScope.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleDataScope.getCtrlType()).isEqualTo(UPDATED_CTRL_TYPE);
        assertThat(testSysRoleDataScope.getCtrlData()).isEqualTo(UPDATED_CTRL_DATA);
        assertThat(testSysRoleDataScope.getCtrlPermit()).isEqualTo(UPDATED_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void patchNonExistingSysRoleDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();
        sysRoleDataScope.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysRoleDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysRoleDataScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysRoleDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();
        sysRoleDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysRoleDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleDataScopeRepository.findAll().size();
        sysRoleDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleDataScope))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysRoleDataScope in the database
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysRoleDataScope() throws Exception {
        // Initialize the database
        sysRoleDataScopeRepository.saveAndFlush(sysRoleDataScope);

        int databaseSizeBeforeDelete = sysRoleDataScopeRepository.findAll().size();

        // Delete the sysRoleDataScope
        restSysRoleDataScopeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysRoleDataScope.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysRoleDataScope> sysRoleDataScopeList = sysRoleDataScopeRepository.findAll();
        assertThat(sysRoleDataScopeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
