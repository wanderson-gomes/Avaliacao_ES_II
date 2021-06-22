package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PlanoDeSaude;
import com.mycompany.myapp.domain.enumeration.FormaDePag;
import com.mycompany.myapp.repository.PlanoDeSaudeRepository;
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
 * Integration tests for the {@link PlanoDeSaudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanoDeSaudeResourceIT {

    private static final Integer DEFAULT_ID_PLANO = 1;
    private static final Integer UPDATED_ID_PLANO = 2;

    private static final String DEFAULT_NOME_PLANO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PLANO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Integer DEFAULT_CNPJ = 1;
    private static final Integer UPDATED_CNPJ = 2;

    private static final FormaDePag DEFAULT_FORMA_DE_PAG = FormaDePag.Dinheiro;
    private static final FormaDePag UPDATED_FORMA_DE_PAG = FormaDePag.Cartao;

    private static final String ENTITY_API_URL = "/api/plano-de-saudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanoDeSaudeRepository planoDeSaudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanoDeSaudeMockMvc;

    private PlanoDeSaude planoDeSaude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoDeSaude createEntity(EntityManager em) {
        PlanoDeSaude planoDeSaude = new PlanoDeSaude()
            .idPlano(DEFAULT_ID_PLANO)
            .nomePlano(DEFAULT_NOME_PLANO)
            .ativo(DEFAULT_ATIVO)
            .cnpj(DEFAULT_CNPJ)
            .formaDePag(DEFAULT_FORMA_DE_PAG);
        return planoDeSaude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoDeSaude createUpdatedEntity(EntityManager em) {
        PlanoDeSaude planoDeSaude = new PlanoDeSaude()
            .idPlano(UPDATED_ID_PLANO)
            .nomePlano(UPDATED_NOME_PLANO)
            .ativo(UPDATED_ATIVO)
            .cnpj(UPDATED_CNPJ)
            .formaDePag(UPDATED_FORMA_DE_PAG);
        return planoDeSaude;
    }

    @BeforeEach
    public void initTest() {
        planoDeSaude = createEntity(em);
    }

    @Test
    @Transactional
    void createPlanoDeSaude() throws Exception {
        int databaseSizeBeforeCreate = planoDeSaudeRepository.findAll().size();
        // Create the PlanoDeSaude
        restPlanoDeSaudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planoDeSaude)))
            .andExpect(status().isCreated());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeCreate + 1);
        PlanoDeSaude testPlanoDeSaude = planoDeSaudeList.get(planoDeSaudeList.size() - 1);
        assertThat(testPlanoDeSaude.getIdPlano()).isEqualTo(DEFAULT_ID_PLANO);
        assertThat(testPlanoDeSaude.getNomePlano()).isEqualTo(DEFAULT_NOME_PLANO);
        assertThat(testPlanoDeSaude.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testPlanoDeSaude.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testPlanoDeSaude.getFormaDePag()).isEqualTo(DEFAULT_FORMA_DE_PAG);
    }

    @Test
    @Transactional
    void createPlanoDeSaudeWithExistingId() throws Exception {
        // Create the PlanoDeSaude with an existing ID
        planoDeSaude.setId(1L);

        int databaseSizeBeforeCreate = planoDeSaudeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoDeSaudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planoDeSaude)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlanoDeSaudes() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        // Get all the planoDeSaudeList
        restPlanoDeSaudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoDeSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].idPlano").value(hasItem(DEFAULT_ID_PLANO)))
            .andExpect(jsonPath("$.[*].nomePlano").value(hasItem(DEFAULT_NOME_PLANO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].formaDePag").value(hasItem(DEFAULT_FORMA_DE_PAG.toString())));
    }

    @Test
    @Transactional
    void getPlanoDeSaude() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        // Get the planoDeSaude
        restPlanoDeSaudeMockMvc
            .perform(get(ENTITY_API_URL_ID, planoDeSaude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planoDeSaude.getId().intValue()))
            .andExpect(jsonPath("$.idPlano").value(DEFAULT_ID_PLANO))
            .andExpect(jsonPath("$.nomePlano").value(DEFAULT_NOME_PLANO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.formaDePag").value(DEFAULT_FORMA_DE_PAG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlanoDeSaude() throws Exception {
        // Get the planoDeSaude
        restPlanoDeSaudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlanoDeSaude() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();

        // Update the planoDeSaude
        PlanoDeSaude updatedPlanoDeSaude = planoDeSaudeRepository.findById(planoDeSaude.getId()).get();
        // Disconnect from session so that the updates on updatedPlanoDeSaude are not directly saved in db
        em.detach(updatedPlanoDeSaude);
        updatedPlanoDeSaude
            .idPlano(UPDATED_ID_PLANO)
            .nomePlano(UPDATED_NOME_PLANO)
            .ativo(UPDATED_ATIVO)
            .cnpj(UPDATED_CNPJ)
            .formaDePag(UPDATED_FORMA_DE_PAG);

        restPlanoDeSaudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlanoDeSaude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlanoDeSaude))
            )
            .andExpect(status().isOk());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
        PlanoDeSaude testPlanoDeSaude = planoDeSaudeList.get(planoDeSaudeList.size() - 1);
        assertThat(testPlanoDeSaude.getIdPlano()).isEqualTo(UPDATED_ID_PLANO);
        assertThat(testPlanoDeSaude.getNomePlano()).isEqualTo(UPDATED_NOME_PLANO);
        assertThat(testPlanoDeSaude.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPlanoDeSaude.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testPlanoDeSaude.getFormaDePag()).isEqualTo(UPDATED_FORMA_DE_PAG);
    }

    @Test
    @Transactional
    void putNonExistingPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();
        planoDeSaude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoDeSaudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planoDeSaude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planoDeSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();
        planoDeSaude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoDeSaudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planoDeSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();
        planoDeSaude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoDeSaudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planoDeSaude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanoDeSaudeWithPatch() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();

        // Update the planoDeSaude using partial update
        PlanoDeSaude partialUpdatedPlanoDeSaude = new PlanoDeSaude();
        partialUpdatedPlanoDeSaude.setId(planoDeSaude.getId());

        partialUpdatedPlanoDeSaude.ativo(UPDATED_ATIVO).cnpj(UPDATED_CNPJ);

        restPlanoDeSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanoDeSaude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanoDeSaude))
            )
            .andExpect(status().isOk());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
        PlanoDeSaude testPlanoDeSaude = planoDeSaudeList.get(planoDeSaudeList.size() - 1);
        assertThat(testPlanoDeSaude.getIdPlano()).isEqualTo(DEFAULT_ID_PLANO);
        assertThat(testPlanoDeSaude.getNomePlano()).isEqualTo(DEFAULT_NOME_PLANO);
        assertThat(testPlanoDeSaude.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPlanoDeSaude.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testPlanoDeSaude.getFormaDePag()).isEqualTo(DEFAULT_FORMA_DE_PAG);
    }

    @Test
    @Transactional
    void fullUpdatePlanoDeSaudeWithPatch() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();

        // Update the planoDeSaude using partial update
        PlanoDeSaude partialUpdatedPlanoDeSaude = new PlanoDeSaude();
        partialUpdatedPlanoDeSaude.setId(planoDeSaude.getId());

        partialUpdatedPlanoDeSaude
            .idPlano(UPDATED_ID_PLANO)
            .nomePlano(UPDATED_NOME_PLANO)
            .ativo(UPDATED_ATIVO)
            .cnpj(UPDATED_CNPJ)
            .formaDePag(UPDATED_FORMA_DE_PAG);

        restPlanoDeSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanoDeSaude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanoDeSaude))
            )
            .andExpect(status().isOk());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
        PlanoDeSaude testPlanoDeSaude = planoDeSaudeList.get(planoDeSaudeList.size() - 1);
        assertThat(testPlanoDeSaude.getIdPlano()).isEqualTo(UPDATED_ID_PLANO);
        assertThat(testPlanoDeSaude.getNomePlano()).isEqualTo(UPDATED_NOME_PLANO);
        assertThat(testPlanoDeSaude.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPlanoDeSaude.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testPlanoDeSaude.getFormaDePag()).isEqualTo(UPDATED_FORMA_DE_PAG);
    }

    @Test
    @Transactional
    void patchNonExistingPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();
        planoDeSaude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoDeSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planoDeSaude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planoDeSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();
        planoDeSaude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoDeSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planoDeSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanoDeSaude() throws Exception {
        int databaseSizeBeforeUpdate = planoDeSaudeRepository.findAll().size();
        planoDeSaude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoDeSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(planoDeSaude))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanoDeSaude in the database
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanoDeSaude() throws Exception {
        // Initialize the database
        planoDeSaudeRepository.saveAndFlush(planoDeSaude);

        int databaseSizeBeforeDelete = planoDeSaudeRepository.findAll().size();

        // Delete the planoDeSaude
        restPlanoDeSaudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, planoDeSaude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanoDeSaude> planoDeSaudeList = planoDeSaudeRepository.findAll();
        assertThat(planoDeSaudeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
