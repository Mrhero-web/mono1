package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.SysUserDataScope;
import com.ledar.mono.domain.enumeration.CtrlType;
import com.ledar.mono.repository.SysUserDataScopeRepository;
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
 * Integration tests for the {@link SysUserDataScopeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysUserDataScopeResourceIT {

    private static final Long DEFAULT_SYS_USER_ID = 1L;
    private static final Long UPDATED_SYS_USER_ID = 2L;

    private static final CtrlType DEFAULT_CTRL_TYPE = CtrlType.COMPANY;
    private static final CtrlType UPDATED_CTRL_TYPE = CtrlType.OFFICE;

    private static final String DEFAULT_CTRL_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CTRL_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_CTRL_PERMIT = "AAAAAAAAAA";
    private static final String UPDATED_CTRL_PERMIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sys-user-data-scopes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysUserDataScopeRepository sysUserDataScopeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSysUserDataScopeMockMvc;

    private SysUserDataScope sysUserDataScope;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysUserDataScope createEntity(EntityManager em) {
        SysUserDataScope sysUserDataScope = new SysUserDataScope()
            .sysUserId(DEFAULT_SYS_USER_ID)
            .ctrlType(DEFAULT_CTRL_TYPE)
            .ctrlData(DEFAULT_CTRL_DATA)
            .ctrlPermit(DEFAULT_CTRL_PERMIT);
        return sysUserDataScope;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysUserDataScope createUpdatedEntity(EntityManager em) {
        SysUserDataScope sysUserDataScope = new SysUserDataScope()
            .sysUserId(UPDATED_SYS_USER_ID)
            .ctrlType(UPDATED_CTRL_TYPE)
            .ctrlData(UPDATED_CTRL_DATA)
            .ctrlPermit(UPDATED_CTRL_PERMIT);
        return sysUserDataScope;
    }

    @BeforeEach
    public void initTest() {
        sysUserDataScope = createEntity(em);
    }

    @Test
    @Transactional
    void createSysUserDataScope() throws Exception {
        int databaseSizeBeforeCreate = sysUserDataScopeRepository.findAll().size();
        // Create the SysUserDataScope
        restSysUserDataScopeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isCreated());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeCreate + 1);
        SysUserDataScope testSysUserDataScope = sysUserDataScopeList.get(sysUserDataScopeList.size() - 1);
        assertThat(testSysUserDataScope.getSysUserId()).isEqualTo(DEFAULT_SYS_USER_ID);
        assertThat(testSysUserDataScope.getCtrlType()).isEqualTo(DEFAULT_CTRL_TYPE);
        assertThat(testSysUserDataScope.getCtrlData()).isEqualTo(DEFAULT_CTRL_DATA);
        assertThat(testSysUserDataScope.getCtrlPermit()).isEqualTo(DEFAULT_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void createSysUserDataScopeWithExistingId() throws Exception {
        // Create the SysUserDataScope with an existing ID
        sysUserDataScope.setId(1L);

        int databaseSizeBeforeCreate = sysUserDataScopeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysUserDataScopeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysUserDataScopes() throws Exception {
        // Initialize the database
        sysUserDataScopeRepository.saveAndFlush(sysUserDataScope);

        // Get all the sysUserDataScopeList
        restSysUserDataScopeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysUserDataScope.getId().intValue())))
            .andExpect(jsonPath("$.[*].sysUserId").value(hasItem(DEFAULT_SYS_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].ctrlType").value(hasItem(DEFAULT_CTRL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ctrlData").value(hasItem(DEFAULT_CTRL_DATA)))
            .andExpect(jsonPath("$.[*].ctrlPermit").value(hasItem(DEFAULT_CTRL_PERMIT)));
    }

    @Test
    @Transactional
    void getSysUserDataScope() throws Exception {
        // Initialize the database
        sysUserDataScopeRepository.saveAndFlush(sysUserDataScope);

        // Get the sysUserDataScope
        restSysUserDataScopeMockMvc
            .perform(get(ENTITY_API_URL_ID, sysUserDataScope.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysUserDataScope.getId().intValue()))
            .andExpect(jsonPath("$.sysUserId").value(DEFAULT_SYS_USER_ID.intValue()))
            .andExpect(jsonPath("$.ctrlType").value(DEFAULT_CTRL_TYPE.toString()))
            .andExpect(jsonPath("$.ctrlData").value(DEFAULT_CTRL_DATA))
            .andExpect(jsonPath("$.ctrlPermit").value(DEFAULT_CTRL_PERMIT));
    }

    @Test
    @Transactional
    void getNonExistingSysUserDataScope() throws Exception {
        // Get the sysUserDataScope
        restSysUserDataScopeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSysUserDataScope() throws Exception {
        // Initialize the database
        sysUserDataScopeRepository.saveAndFlush(sysUserDataScope);

        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();

        // Update the sysUserDataScope
        SysUserDataScope updatedSysUserDataScope = sysUserDataScopeRepository.findById(sysUserDataScope.getId()).get();
        // Disconnect from session so that the updates on updatedSysUserDataScope are not directly saved in db
        em.detach(updatedSysUserDataScope);
        updatedSysUserDataScope
            .sysUserId(UPDATED_SYS_USER_ID)
            .ctrlType(UPDATED_CTRL_TYPE)
            .ctrlData(UPDATED_CTRL_DATA)
            .ctrlPermit(UPDATED_CTRL_PERMIT);

        restSysUserDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSysUserDataScope.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSysUserDataScope))
            )
            .andExpect(status().isOk());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
        SysUserDataScope testSysUserDataScope = sysUserDataScopeList.get(sysUserDataScopeList.size() - 1);
        assertThat(testSysUserDataScope.getSysUserId()).isEqualTo(UPDATED_SYS_USER_ID);
        assertThat(testSysUserDataScope.getCtrlType()).isEqualTo(UPDATED_CTRL_TYPE);
        assertThat(testSysUserDataScope.getCtrlData()).isEqualTo(UPDATED_CTRL_DATA);
        assertThat(testSysUserDataScope.getCtrlPermit()).isEqualTo(UPDATED_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void putNonExistingSysUserDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();
        sysUserDataScope.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysUserDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysUserDataScope.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysUserDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();
        sysUserDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysUserDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();
        sysUserDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserDataScopeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysUserDataScopeWithPatch() throws Exception {
        // Initialize the database
        sysUserDataScopeRepository.saveAndFlush(sysUserDataScope);

        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();

        // Update the sysUserDataScope using partial update
        SysUserDataScope partialUpdatedSysUserDataScope = new SysUserDataScope();
        partialUpdatedSysUserDataScope.setId(sysUserDataScope.getId());

        partialUpdatedSysUserDataScope.sysUserId(UPDATED_SYS_USER_ID).ctrlType(UPDATED_CTRL_TYPE).ctrlPermit(UPDATED_CTRL_PERMIT);

        restSysUserDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysUserDataScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysUserDataScope))
            )
            .andExpect(status().isOk());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
        SysUserDataScope testSysUserDataScope = sysUserDataScopeList.get(sysUserDataScopeList.size() - 1);
        assertThat(testSysUserDataScope.getSysUserId()).isEqualTo(UPDATED_SYS_USER_ID);
        assertThat(testSysUserDataScope.getCtrlType()).isEqualTo(UPDATED_CTRL_TYPE);
        assertThat(testSysUserDataScope.getCtrlData()).isEqualTo(DEFAULT_CTRL_DATA);
        assertThat(testSysUserDataScope.getCtrlPermit()).isEqualTo(UPDATED_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void fullUpdateSysUserDataScopeWithPatch() throws Exception {
        // Initialize the database
        sysUserDataScopeRepository.saveAndFlush(sysUserDataScope);

        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();

        // Update the sysUserDataScope using partial update
        SysUserDataScope partialUpdatedSysUserDataScope = new SysUserDataScope();
        partialUpdatedSysUserDataScope.setId(sysUserDataScope.getId());

        partialUpdatedSysUserDataScope
            .sysUserId(UPDATED_SYS_USER_ID)
            .ctrlType(UPDATED_CTRL_TYPE)
            .ctrlData(UPDATED_CTRL_DATA)
            .ctrlPermit(UPDATED_CTRL_PERMIT);

        restSysUserDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysUserDataScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysUserDataScope))
            )
            .andExpect(status().isOk());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
        SysUserDataScope testSysUserDataScope = sysUserDataScopeList.get(sysUserDataScopeList.size() - 1);
        assertThat(testSysUserDataScope.getSysUserId()).isEqualTo(UPDATED_SYS_USER_ID);
        assertThat(testSysUserDataScope.getCtrlType()).isEqualTo(UPDATED_CTRL_TYPE);
        assertThat(testSysUserDataScope.getCtrlData()).isEqualTo(UPDATED_CTRL_DATA);
        assertThat(testSysUserDataScope.getCtrlPermit()).isEqualTo(UPDATED_CTRL_PERMIT);
    }

    @Test
    @Transactional
    void patchNonExistingSysUserDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();
        sysUserDataScope.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysUserDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysUserDataScope.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysUserDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();
        sysUserDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysUserDataScope() throws Exception {
        int databaseSizeBeforeUpdate = sysUserDataScopeRepository.findAll().size();
        sysUserDataScope.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysUserDataScopeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysUserDataScope))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysUserDataScope in the database
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysUserDataScope() throws Exception {
        // Initialize the database
        sysUserDataScopeRepository.saveAndFlush(sysUserDataScope);

        int databaseSizeBeforeDelete = sysUserDataScopeRepository.findAll().size();

        // Delete the sysUserDataScope
        restSysUserDataScopeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysUserDataScope.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysUserDataScope> sysUserDataScopeList = sysUserDataScopeRepository.findAll();
        assertThat(sysUserDataScopeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
