package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PlanoDeSaude;
import com.mycompany.myapp.repository.PlanoDeSaudeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PlanoDeSaude}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlanoDeSaudeResource {

    private final Logger log = LoggerFactory.getLogger(PlanoDeSaudeResource.class);

    private static final String ENTITY_NAME = "planoDeSaude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoDeSaudeRepository planoDeSaudeRepository;

    public PlanoDeSaudeResource(PlanoDeSaudeRepository planoDeSaudeRepository) {
        this.planoDeSaudeRepository = planoDeSaudeRepository;
    }

    /**
     * {@code POST  /plano-de-saudes} : Create a new planoDeSaude.
     *
     * @param planoDeSaude the planoDeSaude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoDeSaude, or with status {@code 400 (Bad Request)} if the planoDeSaude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plano-de-saudes")
    public ResponseEntity<PlanoDeSaude> createPlanoDeSaude(@RequestBody PlanoDeSaude planoDeSaude) throws URISyntaxException {
        log.debug("REST request to save PlanoDeSaude : {}", planoDeSaude);
        if (planoDeSaude.getId() != null) {
            throw new BadRequestAlertException("A new planoDeSaude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanoDeSaude result = planoDeSaudeRepository.save(planoDeSaude);
        return ResponseEntity
            .created(new URI("/api/plano-de-saudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plano-de-saudes/:id} : Updates an existing planoDeSaude.
     *
     * @param id the id of the planoDeSaude to save.
     * @param planoDeSaude the planoDeSaude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDeSaude,
     * or with status {@code 400 (Bad Request)} if the planoDeSaude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoDeSaude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plano-de-saudes/{id}")
    public ResponseEntity<PlanoDeSaude> updatePlanoDeSaude(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanoDeSaude planoDeSaude
    ) throws URISyntaxException {
        log.debug("REST request to update PlanoDeSaude : {}, {}", id, planoDeSaude);
        if (planoDeSaude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoDeSaude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoDeSaudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlanoDeSaude result = planoDeSaudeRepository.save(planoDeSaude);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoDeSaude.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plano-de-saudes/:id} : Partial updates given fields of an existing planoDeSaude, field will ignore if it is null
     *
     * @param id the id of the planoDeSaude to save.
     * @param planoDeSaude the planoDeSaude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDeSaude,
     * or with status {@code 400 (Bad Request)} if the planoDeSaude is not valid,
     * or with status {@code 404 (Not Found)} if the planoDeSaude is not found,
     * or with status {@code 500 (Internal Server Error)} if the planoDeSaude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plano-de-saudes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PlanoDeSaude> partialUpdatePlanoDeSaude(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanoDeSaude planoDeSaude
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlanoDeSaude partially : {}, {}", id, planoDeSaude);
        if (planoDeSaude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoDeSaude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoDeSaudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanoDeSaude> result = planoDeSaudeRepository
            .findById(planoDeSaude.getId())
            .map(
                existingPlanoDeSaude -> {
                    if (planoDeSaude.getIdPlano() != null) {
                        existingPlanoDeSaude.setIdPlano(planoDeSaude.getIdPlano());
                    }
                    if (planoDeSaude.getNomePlano() != null) {
                        existingPlanoDeSaude.setNomePlano(planoDeSaude.getNomePlano());
                    }
                    if (planoDeSaude.getAtivo() != null) {
                        existingPlanoDeSaude.setAtivo(planoDeSaude.getAtivo());
                    }
                    if (planoDeSaude.getCnpj() != null) {
                        existingPlanoDeSaude.setCnpj(planoDeSaude.getCnpj());
                    }
                    if (planoDeSaude.getFormaDePag() != null) {
                        existingPlanoDeSaude.setFormaDePag(planoDeSaude.getFormaDePag());
                    }

                    return existingPlanoDeSaude;
                }
            )
            .map(planoDeSaudeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoDeSaude.getId().toString())
        );
    }

    /**
     * {@code GET  /plano-de-saudes} : get all the planoDeSaudes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planoDeSaudes in body.
     */
    @GetMapping("/plano-de-saudes")
    public List<PlanoDeSaude> getAllPlanoDeSaudes() {
        log.debug("REST request to get all PlanoDeSaudes");
        return planoDeSaudeRepository.findAll();
    }

    /**
     * {@code GET  /plano-de-saudes/:id} : get the "id" planoDeSaude.
     *
     * @param id the id of the planoDeSaude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoDeSaude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plano-de-saudes/{id}")
    public ResponseEntity<PlanoDeSaude> getPlanoDeSaude(@PathVariable Long id) {
        log.debug("REST request to get PlanoDeSaude : {}", id);
        Optional<PlanoDeSaude> planoDeSaude = planoDeSaudeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(planoDeSaude);
    }

    /**
     * {@code DELETE  /plano-de-saudes/:id} : delete the "id" planoDeSaude.
     *
     * @param id the id of the planoDeSaude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plano-de-saudes/{id}")
    public ResponseEntity<Void> deletePlanoDeSaude(@PathVariable Long id) {
        log.debug("REST request to delete PlanoDeSaude : {}", id);
        planoDeSaudeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
