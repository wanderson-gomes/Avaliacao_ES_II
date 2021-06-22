package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Relatorio;
import com.mycompany.myapp.repository.RelatorioRepository;
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
 * Integration tests for the {@link RelatorioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelatorioResourceIT {

    private static final Integer DEFAULT_ID_MEDICO = 1;
    private static final Integer UPDATED_ID_MEDICO = 2;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/relatorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelatorioMockMvc;

    private Relatorio relatorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatorio createEntity(EntityManager em) {
        Relatorio relatorio = new Relatorio().idMedico(DEFAULT_ID_MEDICO).descricao(DEFAULT_DESCRICAO).diagnostico(DEFAULT_DIAGNOSTICO);
        return relatorio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatorio createUpdatedEntity(EntityManager em) {
        Relatorio relatorio = new Relatorio().idMedico(UPDATED_ID_MEDICO).descricao(UPDATED_DESCRICAO).diagnostico(UPDATED_DIAGNOSTICO);
        return relatorio;
    }

    @BeforeEach
    public void initTest() {
        relatorio = createEntity(em);
    }

    @Test
    @Transactional
    void createRelatorio() throws Exception {
        int databaseSizeBeforeCreate = relatorioRepository.findAll().size();
        // Create the Relatorio
        restRelatorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorio)))
            .andExpect(status().isCreated());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeCreate + 1);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getIdMedico()).isEqualTo(DEFAULT_ID_MEDICO);
        assertThat(testRelatorio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testRelatorio.getDiagnostico()).isEqualTo(DEFAULT_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void createRelatorioWithExistingId() throws Exception {
        // Create the Relatorio with an existing ID
        relatorio.setId(1L);

        int databaseSizeBeforeCreate = relatorioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorio)))
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRelatorios() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get all the relatorioList
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMedico").value(hasItem(DEFAULT_ID_MEDICO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].diagnostico").value(hasItem(DEFAULT_DIAGNOSTICO)));
    }

    @Test
    @Transactional
    void getRelatorio() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        // Get the relatorio
        restRelatorioMockMvc
            .perform(get(ENTITY_API_URL_ID, relatorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relatorio.getId().intValue()))
            .andExpect(jsonPath("$.idMedico").value(DEFAULT_ID_MEDICO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.diagnostico").value(DEFAULT_DIAGNOSTICO));
    }

    @Test
    @Transactional
    void getNonExistingRelatorio() throws Exception {
        // Get the relatorio
        restRelatorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRelatorio() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();

        // Update the relatorio
        Relatorio updatedRelatorio = relatorioRepository.findById(relatorio.getId()).get();
        // Disconnect from session so that the updates on updatedRelatorio are not directly saved in db
        em.detach(updatedRelatorio);
        updatedRelatorio.idMedico(UPDATED_ID_MEDICO).descricao(UPDATED_DESCRICAO).diagnostico(UPDATED_DIAGNOSTICO);

        restRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelatorio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getIdMedico()).isEqualTo(UPDATED_ID_MEDICO);
        assertThat(testRelatorio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testRelatorio.getDiagnostico()).isEqualTo(UPDATED_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void putNonExistingRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatorio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatorio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelatorioWithPatch() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();

        // Update the relatorio using partial update
        Relatorio partialUpdatedRelatorio = new Relatorio();
        partialUpdatedRelatorio.setId(relatorio.getId());

        partialUpdatedRelatorio.descricao(UPDATED_DESCRICAO);

        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getIdMedico()).isEqualTo(DEFAULT_ID_MEDICO);
        assertThat(testRelatorio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testRelatorio.getDiagnostico()).isEqualTo(DEFAULT_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void fullUpdateRelatorioWithPatch() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();

        // Update the relatorio using partial update
        Relatorio partialUpdatedRelatorio = new Relatorio();
        partialUpdatedRelatorio.setId(relatorio.getId());

        partialUpdatedRelatorio.idMedico(UPDATED_ID_MEDICO).descricao(UPDATED_DESCRICAO).diagnostico(UPDATED_DIAGNOSTICO);

        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatorio))
            )
            .andExpect(status().isOk());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
        Relatorio testRelatorio = relatorioList.get(relatorioList.size() - 1);
        assertThat(testRelatorio.getIdMedico()).isEqualTo(UPDATED_ID_MEDICO);
        assertThat(testRelatorio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testRelatorio.getDiagnostico()).isEqualTo(UPDATED_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void patchNonExistingRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relatorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatorio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelatorio() throws Exception {
        int databaseSizeBeforeUpdate = relatorioRepository.findAll().size();
        relatorio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatorioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relatorio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relatorio in the database
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelatorio() throws Exception {
        // Initialize the database
        relatorioRepository.saveAndFlush(relatorio);

        int databaseSizeBeforeDelete = relatorioRepository.findAll().size();

        // Delete the relatorio
        restRelatorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, relatorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Relatorio> relatorioList = relatorioRepository.findAll();
        assertThat(relatorioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
