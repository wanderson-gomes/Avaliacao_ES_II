package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Administrador;
import com.mycompany.myapp.repository.AdministradorRepository;
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
 * Integration tests for the {@link AdministradorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministradorResourceIT {

    private static final Integer DEFAULT_ID_ADMINISTRADOR = 1;
    private static final Integer UPDATED_ID_ADMINISTRADOR = 2;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CPF = 1;
    private static final Integer UPDATED_CPF = 2;

    private static final Integer DEFAULT_RG = 1;
    private static final Integer UPDATED_RG = 2;

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTATO = 1;
    private static final Integer UPDATED_CONTATO = 2;

    private static final String ENTITY_API_URL = "/api/administradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministradorMockMvc;

    private Administrador administrador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createEntity(EntityManager em) {
        Administrador administrador = new Administrador()
            .idAdministrador(DEFAULT_ID_ADMINISTRADOR)
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .rg(DEFAULT_RG)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .cidade(DEFAULT_CIDADE)
            .bairro(DEFAULT_BAIRRO)
            .contato(DEFAULT_CONTATO);
        return administrador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createUpdatedEntity(EntityManager em) {
        Administrador administrador = new Administrador()
            .idAdministrador(UPDATED_ID_ADMINISTRADOR)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cidade(UPDATED_CIDADE)
            .bairro(UPDATED_BAIRRO)
            .contato(UPDATED_CONTATO);
        return administrador;
    }

    @BeforeEach
    public void initTest() {
        administrador = createEntity(em);
    }

    @Test
    @Transactional
    void createAdministrador() throws Exception {
        int databaseSizeBeforeCreate = administradorRepository.findAll().size();
        // Create the Administrador
        restAdministradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrador)))
            .andExpect(status().isCreated());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeCreate + 1);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getIdAdministrador()).isEqualTo(DEFAULT_ID_ADMINISTRADOR);
        assertThat(testAdministrador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAdministrador.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testAdministrador.getRg()).isEqualTo(DEFAULT_RG);
        assertThat(testAdministrador.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testAdministrador.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testAdministrador.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testAdministrador.getContato()).isEqualTo(DEFAULT_CONTATO);
    }

    @Test
    @Transactional
    void createAdministradorWithExistingId() throws Exception {
        // Create the Administrador with an existing ID
        administrador.setId(1L);

        int databaseSizeBeforeCreate = administradorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrador)))
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdministradors() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrador.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAdministrador").value(hasItem(DEFAULT_ID_ADMINISTRADOR)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)));
    }

    @Test
    @Transactional
    void getAdministrador() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        // Get the administrador
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL_ID, administrador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrador.getId().intValue()))
            .andExpect(jsonPath("$.idAdministrador").value(DEFAULT_ID_ADMINISTRADOR))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.rg").value(DEFAULT_RG))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.contato").value(DEFAULT_CONTATO));
    }

    @Test
    @Transactional
    void getNonExistingAdministrador() throws Exception {
        // Get the administrador
        restAdministradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdministrador() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();

        // Update the administrador
        Administrador updatedAdministrador = administradorRepository.findById(administrador.getId()).get();
        // Disconnect from session so that the updates on updatedAdministrador are not directly saved in db
        em.detach(updatedAdministrador);
        updatedAdministrador
            .idAdministrador(UPDATED_ID_ADMINISTRADOR)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cidade(UPDATED_CIDADE)
            .bairro(UPDATED_BAIRRO)
            .contato(UPDATED_CONTATO);

        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdministrador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getIdAdministrador()).isEqualTo(UPDATED_ID_ADMINISTRADOR);
        assertThat(testAdministrador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAdministrador.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testAdministrador.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testAdministrador.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAdministrador.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testAdministrador.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testAdministrador.getContato()).isEqualTo(UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void putNonExistingAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador.nome(UPDATED_NOME).cpf(UPDATED_CPF).rg(UPDATED_RG);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getIdAdministrador()).isEqualTo(DEFAULT_ID_ADMINISTRADOR);
        assertThat(testAdministrador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAdministrador.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testAdministrador.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testAdministrador.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testAdministrador.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testAdministrador.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testAdministrador.getContato()).isEqualTo(DEFAULT_CONTATO);
    }

    @Test
    @Transactional
    void fullUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador
            .idAdministrador(UPDATED_ID_ADMINISTRADOR)
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cidade(UPDATED_CIDADE)
            .bairro(UPDATED_BAIRRO)
            .contato(UPDATED_CONTATO);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
        Administrador testAdministrador = administradorList.get(administradorList.size() - 1);
        assertThat(testAdministrador.getIdAdministrador()).isEqualTo(UPDATED_ID_ADMINISTRADOR);
        assertThat(testAdministrador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAdministrador.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testAdministrador.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testAdministrador.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testAdministrador.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testAdministrador.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testAdministrador.getContato()).isEqualTo(UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrador() throws Exception {
        int databaseSizeBeforeUpdate = administradorRepository.findAll().size();
        administrador.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(administrador))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrador() throws Exception {
        // Initialize the database
        administradorRepository.saveAndFlush(administrador);

        int databaseSizeBeforeDelete = administradorRepository.findAll().size();

        // Delete the administrador
        restAdministradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Administrador> administradorList = administradorRepository.findAll();
        assertThat(administradorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
