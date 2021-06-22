package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Retorno;
import com.mycompany.myapp.repository.RetornoRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Retorno}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RetornoResource {

    private final Logger log = LoggerFactory.getLogger(RetornoResource.class);

    private static final String ENTITY_NAME = "retorno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetornoRepository retornoRepository;

    public RetornoResource(RetornoRepository retornoRepository) {
        this.retornoRepository = retornoRepository;
    }

    /**
     * {@code POST  /retornos} : Create a new retorno.
     *
     * @param retorno the retorno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retorno, or with status {@code 400 (Bad Request)} if the retorno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retornos")
    public ResponseEntity<Retorno> createRetorno(@RequestBody Retorno retorno) throws URISyntaxException {
        log.debug("REST request to save Retorno : {}", retorno);
        if (retorno.getId() != null) {
            throw new BadRequestAlertException("A new retorno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Retorno result = retornoRepository.save(retorno);
        return ResponseEntity
            .created(new URI("/api/retornos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retornos/:id} : Updates an existing retorno.
     *
     * @param id the id of the retorno to save.
     * @param retorno the retorno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retorno,
     * or with status {@code 400 (Bad Request)} if the retorno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retorno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retornos/{id}")
    public ResponseEntity<Retorno> updateRetorno(@PathVariable(value = "id", required = false) final Long id, @RequestBody Retorno retorno)
        throws URISyntaxException {
        log.debug("REST request to update Retorno : {}, {}", id, retorno);
        if (retorno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retorno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retornoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Retorno result = retornoRepository.save(retorno);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retorno.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retornos/:id} : Partial updates given fields of an existing retorno, field will ignore if it is null
     *
     * @param id the id of the retorno to save.
     * @param retorno the retorno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retorno,
     * or with status {@code 400 (Bad Request)} if the retorno is not valid,
     * or with status {@code 404 (Not Found)} if the retorno is not found,
     * or with status {@code 500 (Internal Server Error)} if the retorno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retornos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Retorno> partialUpdateRetorno(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Retorno retorno
    ) throws URISyntaxException {
        log.debug("REST request to partial update Retorno partially : {}, {}", id, retorno);
        if (retorno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retorno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retornoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Retorno> result = retornoRepository
            .findById(retorno.getId())
            .map(
                existingRetorno -> {
                    if (retorno.getIdMedico() != null) {
                        existingRetorno.setIdMedico(retorno.getIdMedico());
                    }
                    if (retorno.getIdPaciente() != null) {
                        existingRetorno.setIdPaciente(retorno.getIdPaciente());
                    }
                    if (retorno.getDataRetorno() != null) {
                        existingRetorno.setDataRetorno(retorno.getDataRetorno());
                    }

                    return existingRetorno;
                }
            )
            .map(retornoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retorno.getId().toString())
        );
    }

    /**
     * {@code GET  /retornos} : get all the retornos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retornos in body.
     */
    @GetMapping("/retornos")
    public List<Retorno> getAllRetornos() {
        log.debug("REST request to get all Retornos");
        return retornoRepository.findAll();
    }

    /**
     * {@code GET  /retornos/:id} : get the "id" retorno.
     *
     * @param id the id of the retorno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retorno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retornos/{id}")
    public ResponseEntity<Retorno> getRetorno(@PathVariable Long id) {
        log.debug("REST request to get Retorno : {}", id);
        Optional<Retorno> retorno = retornoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(retorno);
    }

    /**
     * {@code DELETE  /retornos/:id} : delete the "id" retorno.
     *
     * @param id the id of the retorno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retornos/{id}")
    public ResponseEntity<Void> deleteRetorno(@PathVariable Long id) {
        log.debug("REST request to delete Retorno : {}", id);
        retornoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
