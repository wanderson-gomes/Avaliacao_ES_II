package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.repository.PacienteRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Paciente}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PacienteResource {

    private final Logger log = LoggerFactory.getLogger(PacienteResource.class);

    private static final String ENTITY_NAME = "paciente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacienteRepository pacienteRepository;

    public PacienteResource(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * {@code POST  /pacientes} : Create a new paciente.
     *
     * @param paciente the paciente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paciente, or with status {@code 400 (Bad Request)} if the paciente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente) throws URISyntaxException {
        log.debug("REST request to save Paciente : {}", paciente);
        if (paciente.getId() != null) {
            throw new BadRequestAlertException("A new paciente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paciente result = pacienteRepository.save(paciente);
        return ResponseEntity
            .created(new URI("/api/pacientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pacientes/:id} : Updates an existing paciente.
     *
     * @param id the id of the paciente to save.
     * @param paciente the paciente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paciente,
     * or with status {@code 400 (Bad Request)} if the paciente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paciente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> updatePaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Paciente paciente
    ) throws URISyntaxException {
        log.debug("REST request to update Paciente : {}, {}", id, paciente);
        if (paciente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paciente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Paciente result = pacienteRepository.save(paciente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paciente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pacientes/:id} : Partial updates given fields of an existing paciente, field will ignore if it is null
     *
     * @param id the id of the paciente to save.
     * @param paciente the paciente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paciente,
     * or with status {@code 400 (Bad Request)} if the paciente is not valid,
     * or with status {@code 404 (Not Found)} if the paciente is not found,
     * or with status {@code 500 (Internal Server Error)} if the paciente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pacientes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Paciente> partialUpdatePaciente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Paciente paciente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Paciente partially : {}, {}", id, paciente);
        if (paciente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paciente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Paciente> result = pacienteRepository
            .findById(paciente.getId())
            .map(
                existingPaciente -> {
                    if (paciente.getIdUsuario() != null) {
                        existingPaciente.setIdUsuario(paciente.getIdUsuario());
                    }
                    if (paciente.getNome() != null) {
                        existingPaciente.setNome(paciente.getNome());
                    }
                    if (paciente.getCpf() != null) {
                        existingPaciente.setCpf(paciente.getCpf());
                    }
                    if (paciente.getRg() != null) {
                        existingPaciente.setRg(paciente.getRg());
                    }
                    if (paciente.getDataNascimento() != null) {
                        existingPaciente.setDataNascimento(paciente.getDataNascimento());
                    }
                    if (paciente.getCidade() != null) {
                        existingPaciente.setCidade(paciente.getCidade());
                    }
                    if (paciente.getBairro() != null) {
                        existingPaciente.setBairro(paciente.getBairro());
                    }
                    if (paciente.getContato() != null) {
                        existingPaciente.setContato(paciente.getContato());
                    }

                    return existingPaciente;
                }
            )
            .map(pacienteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paciente.getId().toString())
        );
    }

    /**
     * {@code GET  /pacientes} : get all the pacientes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacientes in body.
     */
    @GetMapping("/pacientes")
    public List<Paciente> getAllPacientes() {
        log.debug("REST request to get all Pacientes");
        return pacienteRepository.findAll();
    }

    /**
     * {@code GET  /pacientes/:id} : get the "id" paciente.
     *
     * @param id the id of the paciente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paciente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> getPaciente(@PathVariable Long id) {
        log.debug("REST request to get Paciente : {}", id);
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paciente);
    }

    /**
     * {@code DELETE  /pacientes/:id} : delete the "id" paciente.
     *
     * @param id the id of the paciente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        log.debug("REST request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
