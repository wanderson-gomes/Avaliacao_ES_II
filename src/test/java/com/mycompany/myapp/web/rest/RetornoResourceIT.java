package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Retorno;
import com.mycompany.myapp.repository.RetornoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link RetornoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RetornoResourceIT {

    private static final Integer DEFAULT_ID_MEDICO = 1;
    private static final Integer UPDATED_ID_MEDICO = 2;

    private static final Integer DEFAULT_ID_PACIENTE = 1;
    private static final Integer UPDATED_ID_PACIENTE = 2;

    private static final LocalDate DEFAULT_DATA_RETORNO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_RETORNO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/retornos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetornoRepository retornoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetornoMockMvc;

    private Retorno retorno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retorno createEntity(EntityManager em) {
        Retorno retorno = new Retorno().idMedico(DEFAULT_ID_MEDICO).idPaciente(DEFAULT_ID_PACIENTE).dataRetorno(DEFAULT_DATA_RETORNO);
        return retorno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retorno createUpdatedEntity(EntityManager em) {
        Retorno retorno = new Retorno().idMedico(UPDATED_ID_MEDICO).idPaciente(UPDATED_ID_PACIENTE).dataRetorno(UPDATED_DATA_RETORNO);
        return retorno;
    }

    @BeforeEach
    public void initTest() {
        retorno = createEntity(em);
    }

    @Test
    @Transactional
    void createRetorno() throws Exception {
        int databaseSizeBeforeCreate = retornoRepository.findAll().size();
        // Create the Retorno
        restRetornoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retorno)))
            .andExpect(status().isCreated());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeCreate + 1);
        Retorno testRetorno = retornoList.get(retornoList.size() - 1);
        assertThat(testRetorno.getIdMedico()).isEqualTo(DEFAULT_ID_MEDICO);
        assertThat(testRetorno.getIdPaciente()).isEqualTo(DEFAULT_ID_PACIENTE);
        assertThat(testRetorno.getDataRetorno()).isEqualTo(DEFAULT_DATA_RETORNO);
    }

    @Test
    @Transactional
    void createRetornoWithExistingId() throws Exception {
        // Create the Retorno with an existing ID
        retorno.setId(1L);

        int databaseSizeBeforeCreate = retornoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetornoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retorno)))
            .andExpect(status().isBadRequest());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRetornos() throws Exception {
        // Initialize the database
        retornoRepository.saveAndFlush(retorno);

        // Get all the retornoList
        restRetornoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retorno.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMedico").value(hasItem(DEFAULT_ID_MEDICO)))
            .andExpect(jsonPath("$.[*].idPaciente").value(hasItem(DEFAULT_ID_PACIENTE)))
            .andExpect(jsonPath("$.[*].dataRetorno").value(hasItem(DEFAULT_DATA_RETORNO.toString())));
    }

    @Test
    @Transactional
    void getRetorno() throws Exception {
        // Initialize the database
        retornoRepository.saveAndFlush(retorno);

        // Get the retorno
        restRetornoMockMvc
            .perform(get(ENTITY_API_URL_ID, retorno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retorno.getId().intValue()))
            .andExpect(jsonPath("$.idMedico").value(DEFAULT_ID_MEDICO))
            .andExpect(jsonPath("$.idPaciente").value(DEFAULT_ID_PACIENTE))
            .andExpect(jsonPath("$.dataRetorno").value(DEFAULT_DATA_RETORNO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRetorno() throws Exception {
        // Get the retorno
        restRetornoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRetorno() throws Exception {
        // Initialize the database
        retornoRepository.saveAndFlush(retorno);

        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();

        // Update the retorno
        Retorno updatedRetorno = retornoRepository.findById(retorno.getId()).get();
        // Disconnect from session so that the updates on updatedRetorno are not directly saved in db
        em.detach(updatedRetorno);
        updatedRetorno.idMedico(UPDATED_ID_MEDICO).idPaciente(UPDATED_ID_PACIENTE).dataRetorno(UPDATED_DATA_RETORNO);

        restRetornoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRetorno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRetorno))
            )
            .andExpect(status().isOk());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
        Retorno testRetorno = retornoList.get(retornoList.size() - 1);
        assertThat(testRetorno.getIdMedico()).isEqualTo(UPDATED_ID_MEDICO);
        assertThat(testRetorno.getIdPaciente()).isEqualTo(UPDATED_ID_PACIENTE);
        assertThat(testRetorno.getDataRetorno()).isEqualTo(UPDATED_DATA_RETORNO);
    }

    @Test
    @Transactional
    void putNonExistingRetorno() throws Exception {
        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();
        retorno.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetornoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retorno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retorno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetorno() throws Exception {
        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();
        retorno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetornoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retorno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetorno() throws Exception {
        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();
        retorno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetornoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retorno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetornoWithPatch() throws Exception {
        // Initialize the database
        retornoRepository.saveAndFlush(retorno);

        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();

        // Update the retorno using partial update
        Retorno partialUpdatedRetorno = new Retorno();
        partialUpdatedRetorno.setId(retorno.getId());

        partialUpdatedRetorno.idMedico(UPDATED_ID_MEDICO).idPaciente(UPDATED_ID_PACIENTE);

        restRetornoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetorno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetorno))
            )
            .andExpect(status().isOk());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
        Retorno testRetorno = retornoList.get(retornoList.size() - 1);
        assertThat(testRetorno.getIdMedico()).isEqualTo(UPDATED_ID_MEDICO);
        assertThat(testRetorno.getIdPaciente()).isEqualTo(UPDATED_ID_PACIENTE);
        assertThat(testRetorno.getDataRetorno()).isEqualTo(DEFAULT_DATA_RETORNO);
    }

    @Test
    @Transactional
    void fullUpdateRetornoWithPatch() throws Exception {
        // Initialize the database
        retornoRepository.saveAndFlush(retorno);

        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();

        // Update the retorno using partial update
        Retorno partialUpdatedRetorno = new Retorno();
        partialUpdatedRetorno.setId(retorno.getId());

        partialUpdatedRetorno.idMedico(UPDATED_ID_MEDICO).idPaciente(UPDATED_ID_PACIENTE).dataRetorno(UPDATED_DATA_RETORNO);

        restRetornoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetorno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetorno))
            )
            .andExpect(status().isOk());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
        Retorno testRetorno = retornoList.get(retornoList.size() - 1);
        assertThat(testRetorno.getIdMedico()).isEqualTo(UPDATED_ID_MEDICO);
        assertThat(testRetorno.getIdPaciente()).isEqualTo(UPDATED_ID_PACIENTE);
        assertThat(testRetorno.getDataRetorno()).isEqualTo(UPDATED_DATA_RETORNO);
    }

    @Test
    @Transactional
    void patchNonExistingRetorno() throws Exception {
        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();
        retorno.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetornoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retorno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retorno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetorno() throws Exception {
        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();
        retorno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetornoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retorno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetorno() throws Exception {
        int databaseSizeBeforeUpdate = retornoRepository.findAll().size();
        retorno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetornoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(retorno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retorno in the database
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetorno() throws Exception {
        // Initialize the database
        retornoRepository.saveAndFlush(retorno);

        int databaseSizeBeforeDelete = retornoRepository.findAll().size();

        // Delete the retorno
        restRetornoMockMvc
            .perform(delete(ENTITY_API_URL_ID, retorno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Retorno> retornoList = retornoRepository.findAll();
        assertThat(retornoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
