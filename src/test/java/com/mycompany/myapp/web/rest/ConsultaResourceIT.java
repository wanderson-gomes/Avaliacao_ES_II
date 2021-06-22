package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Consulta;
import com.mycompany.myapp.repository.ConsultaRepository;
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
 * Integration tests for the {@link ConsultaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultaResourceIT {

    private static final Integer DEFAULT_ID_PACIENTE = 1;
    private static final Integer UPDATED_ID_PACIENTE = 2;

    private static final LocalDate DEFAULT_DATA_CONSULTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CONSULTA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_HORARIO = "AAAAAAAAAA";
    private static final String UPDATED_HORARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consultas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultaMockMvc;

    private Consulta consulta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consulta createEntity(EntityManager em) {
        Consulta consulta = new Consulta().idPaciente(DEFAULT_ID_PACIENTE).dataConsulta(DEFAULT_DATA_CONSULTA).horario(DEFAULT_HORARIO);
        return consulta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consulta createUpdatedEntity(EntityManager em) {
        Consulta consulta = new Consulta().idPaciente(UPDATED_ID_PACIENTE).dataConsulta(UPDATED_DATA_CONSULTA).horario(UPDATED_HORARIO);
        return consulta;
    }

    @BeforeEach
    public void initTest() {
        consulta = createEntity(em);
    }

    @Test
    @Transactional
    void createConsulta() throws Exception {
        int databaseSizeBeforeCreate = consultaRepository.findAll().size();
        // Create the Consulta
        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consulta)))
            .andExpect(status().isCreated());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeCreate + 1);
        Consulta testConsulta = consultaList.get(consultaList.size() - 1);
        assertThat(testConsulta.getIdPaciente()).isEqualTo(DEFAULT_ID_PACIENTE);
        assertThat(testConsulta.getDataConsulta()).isEqualTo(DEFAULT_DATA_CONSULTA);
        assertThat(testConsulta.getHorario()).isEqualTo(DEFAULT_HORARIO);
    }

    @Test
    @Transactional
    void createConsultaWithExistingId() throws Exception {
        // Create the Consulta with an existing ID
        consulta.setId(1L);

        int databaseSizeBeforeCreate = consultaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consulta)))
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultas() throws Exception {
        // Initialize the database
        consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consulta.getId().intValue())))
            .andExpect(jsonPath("$.[*].idPaciente").value(hasItem(DEFAULT_ID_PACIENTE)))
            .andExpect(jsonPath("$.[*].dataConsulta").value(hasItem(DEFAULT_DATA_CONSULTA.toString())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO)));
    }

    @Test
    @Transactional
    void getConsulta() throws Exception {
        // Initialize the database
        consultaRepository.saveAndFlush(consulta);

        // Get the consulta
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL_ID, consulta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consulta.getId().intValue()))
            .andExpect(jsonPath("$.idPaciente").value(DEFAULT_ID_PACIENTE))
            .andExpect(jsonPath("$.dataConsulta").value(DEFAULT_DATA_CONSULTA.toString()))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO));
    }

    @Test
    @Transactional
    void getNonExistingConsulta() throws Exception {
        // Get the consulta
        restConsultaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConsulta() throws Exception {
        // Initialize the database
        consultaRepository.saveAndFlush(consulta);

        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();

        // Update the consulta
        Consulta updatedConsulta = consultaRepository.findById(consulta.getId()).get();
        // Disconnect from session so that the updates on updatedConsulta are not directly saved in db
        em.detach(updatedConsulta);
        updatedConsulta.idPaciente(UPDATED_ID_PACIENTE).dataConsulta(UPDATED_DATA_CONSULTA).horario(UPDATED_HORARIO);

        restConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsulta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConsulta))
            )
            .andExpect(status().isOk());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
        Consulta testConsulta = consultaList.get(consultaList.size() - 1);
        assertThat(testConsulta.getIdPaciente()).isEqualTo(UPDATED_ID_PACIENTE);
        assertThat(testConsulta.getDataConsulta()).isEqualTo(UPDATED_DATA_CONSULTA);
        assertThat(testConsulta.getHorario()).isEqualTo(UPDATED_HORARIO);
    }

    @Test
    @Transactional
    void putNonExistingConsulta() throws Exception {
        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();
        consulta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consulta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsulta() throws Exception {
        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();
        consulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsulta() throws Exception {
        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();
        consulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consulta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultaWithPatch() throws Exception {
        // Initialize the database
        consultaRepository.saveAndFlush(consulta);

        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();

        // Update the consulta using partial update
        Consulta partialUpdatedConsulta = new Consulta();
        partialUpdatedConsulta.setId(consulta.getId());

        partialUpdatedConsulta.idPaciente(UPDATED_ID_PACIENTE).horario(UPDATED_HORARIO);

        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsulta))
            )
            .andExpect(status().isOk());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
        Consulta testConsulta = consultaList.get(consultaList.size() - 1);
        assertThat(testConsulta.getIdPaciente()).isEqualTo(UPDATED_ID_PACIENTE);
        assertThat(testConsulta.getDataConsulta()).isEqualTo(DEFAULT_DATA_CONSULTA);
        assertThat(testConsulta.getHorario()).isEqualTo(UPDATED_HORARIO);
    }

    @Test
    @Transactional
    void fullUpdateConsultaWithPatch() throws Exception {
        // Initialize the database
        consultaRepository.saveAndFlush(consulta);

        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();

        // Update the consulta using partial update
        Consulta partialUpdatedConsulta = new Consulta();
        partialUpdatedConsulta.setId(consulta.getId());

        partialUpdatedConsulta.idPaciente(UPDATED_ID_PACIENTE).dataConsulta(UPDATED_DATA_CONSULTA).horario(UPDATED_HORARIO);

        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsulta))
            )
            .andExpect(status().isOk());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
        Consulta testConsulta = consultaList.get(consultaList.size() - 1);
        assertThat(testConsulta.getIdPaciente()).isEqualTo(UPDATED_ID_PACIENTE);
        assertThat(testConsulta.getDataConsulta()).isEqualTo(UPDATED_DATA_CONSULTA);
        assertThat(testConsulta.getHorario()).isEqualTo(UPDATED_HORARIO);
    }

    @Test
    @Transactional
    void patchNonExistingConsulta() throws Exception {
        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();
        consulta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsulta() throws Exception {
        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();
        consulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consulta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsulta() throws Exception {
        int databaseSizeBeforeUpdate = consultaRepository.findAll().size();
        consulta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consulta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consulta in the database
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsulta() throws Exception {
        // Initialize the database
        consultaRepository.saveAndFlush(consulta);

        int databaseSizeBeforeDelete = consultaRepository.findAll().size();

        // Delete the consulta
        restConsultaMockMvc
            .perform(delete(ENTITY_API_URL_ID, consulta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consulta> consultaList = consultaRepository.findAll();
        assertThat(consultaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
