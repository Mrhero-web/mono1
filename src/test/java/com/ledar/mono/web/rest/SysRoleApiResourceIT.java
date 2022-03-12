package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.SysRoleApi;
import com.ledar.mono.repository.SysRoleApiRepository;
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
 * Integration tests for the {@link SysRoleApiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysRoleApiResourceIT {

    private static final Long DEFAULT_SYS_ROLE_ID = 1L;
    private static final Long UPDATED_SYS_ROLE_ID = 2L;

    private static final Long DEFAULT_SYS_API_ID = 1L;
    private static final Long UPDATED_SYS_API_ID = 2L;

    private static final String ENTITY_API_URL = "/api/sys-role-apis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysRoleApiRepository sysRoleApiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSysRoleApiMockMvc;

    private SysRoleApi sysRoleApi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRoleApi createEntity(EntityManager em) {
        SysRoleApi sysRoleApi = new SysRoleApi().sysRoleId(DEFAULT_SYS_ROLE_ID).sysApiId(DEFAULT_SYS_API_ID);
        return sysRoleApi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysRoleApi createUpdatedEntity(EntityManager em) {
        SysRoleApi sysRoleApi = new SysRoleApi().sysRoleId(UPDATED_SYS_ROLE_ID).sysApiId(UPDATED_SYS_API_ID);
        return sysRoleApi;
    }

    @BeforeEach
    public void initTest() {
        sysRoleApi = createEntity(em);
    }

    @Test
    @Transactional
    void createSysRoleApi() throws Exception {
        int databaseSizeBeforeCreate = sysRoleApiRepository.findAll().size();
        // Create the SysRoleApi
        restSysRoleApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleApi)))
            .andExpect(status().isCreated());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeCreate + 1);
        SysRoleApi testSysRoleApi = sysRoleApiList.get(sysRoleApiList.size() - 1);
        assertThat(testSysRoleApi.getSysRoleId()).isEqualTo(DEFAULT_SYS_ROLE_ID);
        assertThat(testSysRoleApi.getSysApiId()).isEqualTo(DEFAULT_SYS_API_ID);
    }

    @Test
    @Transactional
    void createSysRoleApiWithExistingId() throws Exception {
        // Create the SysRoleApi with an existing ID
        sysRoleApi.setId(1L);

        int databaseSizeBeforeCreate = sysRoleApiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysRoleApiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleApi)))
            .andExpect(status().isBadRequest());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysRoleApis() throws Exception {
        // Initialize the database
        sysRoleApiRepository.saveAndFlush(sysRoleApi);

        // Get all the sysRoleApiList
        restSysRoleApiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysRoleApi.getId().intValue())))
            .andExpect(jsonPath("$.[*].sysRoleId").value(hasItem(DEFAULT_SYS_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].sysApiId").value(hasItem(DEFAULT_SYS_API_ID.intValue())));
    }

    @Test
    @Transactional
    void getSysRoleApi() throws Exception {
        // Initialize the database
        sysRoleApiRepository.saveAndFlush(sysRoleApi);

        // Get the sysRoleApi
        restSysRoleApiMockMvc
            .perform(get(ENTITY_API_URL_ID, sysRoleApi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysRoleApi.getId().intValue()))
            .andExpect(jsonPath("$.sysRoleId").value(DEFAULT_SYS_ROLE_ID.intValue()))
            .andExpect(jsonPath("$.sysApiId").value(DEFAULT_SYS_API_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSysRoleApi() throws Exception {
        // Get the sysRoleApi
        restSysRoleApiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSysRoleApi() throws Exception {
        // Initialize the database
        sysRoleApiRepository.saveAndFlush(sysRoleApi);

        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();

        // Update the sysRoleApi
        SysRoleApi updatedSysRoleApi = sysRoleApiRepository.findById(sysRoleApi.getId()).get();
        // Disconnect from session so that the updates on updatedSysRoleApi are not directly saved in db
        em.detach(updatedSysRoleApi);
        updatedSysRoleApi.sysRoleId(UPDATED_SYS_ROLE_ID).sysApiId(UPDATED_SYS_API_ID);

        restSysRoleApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSysRoleApi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSysRoleApi))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
        SysRoleApi testSysRoleApi = sysRoleApiList.get(sysRoleApiList.size() - 1);
        assertThat(testSysRoleApi.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleApi.getSysApiId()).isEqualTo(UPDATED_SYS_API_ID);
    }

    @Test
    @Transactional
    void putNonExistingSysRoleApi() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();
        sysRoleApi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysRoleApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysRoleApi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleApi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysRoleApi() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();
        sysRoleApi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleApiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleApi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysRoleApi() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();
        sysRoleApi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleApiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysRoleApi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysRoleApiWithPatch() throws Exception {
        // Initialize the database
        sysRoleApiRepository.saveAndFlush(sysRoleApi);

        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();

        // Update the sysRoleApi using partial update
        SysRoleApi partialUpdatedSysRoleApi = new SysRoleApi();
        partialUpdatedSysRoleApi.setId(sysRoleApi.getId());

        partialUpdatedSysRoleApi.sysRoleId(UPDATED_SYS_ROLE_ID);

        restSysRoleApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysRoleApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysRoleApi))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
        SysRoleApi testSysRoleApi = sysRoleApiList.get(sysRoleApiList.size() - 1);
        assertThat(testSysRoleApi.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleApi.getSysApiId()).isEqualTo(DEFAULT_SYS_API_ID);
    }

    @Test
    @Transactional
    void fullUpdateSysRoleApiWithPatch() throws Exception {
        // Initialize the database
        sysRoleApiRepository.saveAndFlush(sysRoleApi);

        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();

        // Update the sysRoleApi using partial update
        SysRoleApi partialUpdatedSysRoleApi = new SysRoleApi();
        partialUpdatedSysRoleApi.setId(sysRoleApi.getId());

        partialUpdatedSysRoleApi.sysRoleId(UPDATED_SYS_ROLE_ID).sysApiId(UPDATED_SYS_API_ID);

        restSysRoleApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysRoleApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysRoleApi))
            )
            .andExpect(status().isOk());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
        SysRoleApi testSysRoleApi = sysRoleApiList.get(sysRoleApiList.size() - 1);
        assertThat(testSysRoleApi.getSysRoleId()).isEqualTo(UPDATED_SYS_ROLE_ID);
        assertThat(testSysRoleApi.getSysApiId()).isEqualTo(UPDATED_SYS_API_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSysRoleApi() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();
        sysRoleApi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysRoleApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysRoleApi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleApi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysRoleApi() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();
        sysRoleApi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleApiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysRoleApi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysRoleApi() throws Exception {
        int databaseSizeBeforeUpdate = sysRoleApiRepository.findAll().size();
        sysRoleApi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysRoleApiMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sysRoleApi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysRoleApi in the database
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysRoleApi() throws Exception {
        // Initialize the database
        sysRoleApiRepository.saveAndFlush(sysRoleApi);

        int databaseSizeBeforeDelete = sysRoleApiRepository.findAll().size();

        // Delete the sysRoleApi
        restSysRoleApiMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysRoleApi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysRoleApi> sysRoleApiList = sysRoleApiRepository.findAll();
        assertThat(sysRoleApiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
