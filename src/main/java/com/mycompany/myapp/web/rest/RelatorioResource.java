package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Relatorio;
import com.mycompany.myapp.repository.RelatorioRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Relatorio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RelatorioResource {

    private final Logger log = LoggerFactory.getLogger(RelatorioResource.class);

    private static final String ENTITY_NAME = "relatorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelatorioRepository relatorioRepository;

    public RelatorioResource(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    /**
     * {@code POST  /relatorios} : Create a new relatorio.
     *
     * @param relatorio the relatorio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatorio, or with status {@code 400 (Bad Request)} if the relatorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/relatorios")
    public ResponseEntity<Relatorio> createRelatorio(@RequestBody Relatorio relatorio) throws URISyntaxException {
        log.debug("REST request to save Relatorio : {}", relatorio);
        if (relatorio.getId() != null) {
            throw new BadRequestAlertException("A new relatorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Relatorio result = relatorioRepository.save(relatorio);
        return ResponseEntity
            .created(new URI("/api/relatorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /relatorios/:id} : Updates an existing relatorio.
     *
     * @param id the id of the relatorio to save.
     * @param relatorio the relatorio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatorio,
     * or with status {@code 400 (Bad Request)} if the relatorio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatorio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/relatorios/{id}")
    public ResponseEntity<Relatorio> updateRelatorio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Relatorio relatorio
    ) throws URISyntaxException {
        log.debug("REST request to update Relatorio : {}, {}", id, relatorio);
        if (relatorio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatorio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Relatorio result = relatorioRepository.save(relatorio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatorio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /relatorios/:id} : Partial updates given fields of an existing relatorio, field will ignore if it is null
     *
     * @param id the id of the relatorio to save.
     * @param relatorio the relatorio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatorio,
     * or with status {@code 400 (Bad Request)} if the relatorio is not valid,
     * or with status {@code 404 (Not Found)} if the relatorio is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatorio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/relatorios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Relatorio> partialUpdateRelatorio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Relatorio relatorio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Relatorio partially : {}, {}", id, relatorio);
        if (relatorio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatorio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Relatorio> result = relatorioRepository
            .findById(relatorio.getId())
            .map(
                existingRelatorio -> {
                    if (relatorio.getIdMedico() != null) {
                        existingRelatorio.setIdMedico(relatorio.getIdMedico());
                    }
                    if (relatorio.getDescricao() != null) {
                        existingRelatorio.setDescricao(relatorio.getDescricao());
                    }
                    if (relatorio.getDiagnostico() != null) {
                        existingRelatorio.setDiagnostico(relatorio.getDiagnostico());
                    }

                    return existingRelatorio;
                }
            )
            .map(relatorioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatorio.getId().toString())
        );
    }

    /**
     * {@code GET  /relatorios} : get all the relatorios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatorios in body.
     */
    @GetMapping("/relatorios")
    public List<Relatorio> getAllRelatorios() {
        log.debug("REST request to get all Relatorios");
        return relatorioRepository.findAll();
    }

    /**
     * {@code GET  /relatorios/:id} : get the "id" relatorio.
     *
     * @param id the id of the relatorio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatorio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/relatorios/{id}")
    public ResponseEntity<Relatorio> getRelatorio(@PathVariable Long id) {
        log.debug("REST request to get Relatorio : {}", id);
        Optional<Relatorio> relatorio = relatorioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(relatorio);
    }

    /**
     * {@code DELETE  /relatorios/:id} : delete the "id" relatorio.
     *
     * @param id the id of the relatorio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/relatorios/{id}")
    public ResponseEntity<Void> deleteRelatorio(@PathVariable Long id) {
        log.debug("REST request to delete Relatorio : {}", id);
        relatorioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
