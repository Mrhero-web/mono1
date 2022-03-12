package com.ledar.mono.web.rest;

import com.ledar.mono.domain.Api;
import com.ledar.mono.repository.ApiRepository;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ledar.mono.domain.Api}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApiResource {

    private final Logger log = LoggerFactory.getLogger(ApiResource.class);

    private static final String ENTITY_NAME = "api";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiRepository apiRepository;

    public ApiResource(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    /**
     * {@code POST  /apis} : Create a new api.
     *
     * @param api the api to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new api, or with status {@code 400 (Bad Request)} if the api has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apis")
    public ResponseEntity<Api> createApi(@RequestBody Api api) throws URISyntaxException {
        log.debug("REST request to save Api : {}", api);
        if (api.getId() != null) {
            throw new BadRequestAlertException("A new api cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Api result = apiRepository.save(api);
        return ResponseEntity
            .created(new URI("/api/apis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apis/:id} : Updates an existing api.
     *
     * @param id the id of the api to save.
     * @param api the api to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated api,
     * or with status {@code 400 (Bad Request)} if the api is not valid,
     * or with status {@code 500 (Internal Server Error)} if the api couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apis/{id}")
    public ResponseEntity<Api> updateApi(@PathVariable(value = "id", required = false) final Long id, @RequestBody Api api)
        throws URISyntaxException {
        log.debug("REST request to update Api : {}, {}", id, api);
        if (api.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, api.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Api result = apiRepository.save(api);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, api.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apis/:id} : Partial updates given fields of an existing api, field will ignore if it is null
     *
     * @param id the id of the api to save.
     * @param api the api to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated api,
     * or with status {@code 400 (Bad Request)} if the api is not valid,
     * or with status {@code 404 (Not Found)} if the api is not found,
     * or with status {@code 500 (Internal Server Error)} if the api couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Api> partialUpdateApi(@PathVariable(value = "id", required = false) final Long id, @RequestBody Api api)
        throws URISyntaxException {
        log.debug("REST request to partial update Api partially : {}, {}", id, api);
        if (api.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, api.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Api> result = apiRepository
            .findById(api.getId())
            .map(existingApi -> {
                if (api.getName() != null) {
                    existingApi.setName(api.getName());
                }
                if (api.getRequestMethod() != null) {
                    existingApi.setRequestMethod(api.getRequestMethod());
                }
                if (api.getUrl() != null) {
                    existingApi.setUrl(api.getUrl());
                }

                return existingApi;
            })
            .map(apiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, api.getId().toString())
        );
    }

    /**
     * {@code GET  /apis} : get all the apis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apis in body.
     */
    @GetMapping("/apis")
    public List<Api> getAllApis() {
        log.debug("REST request to get all Apis");
        return apiRepository.findAll();
    }

    /**
     * {@code GET  /apis/:id} : get the "id" api.
     *
     * @param id the id of the api to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the api, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apis/{id}")
    public ResponseEntity<Api> getApi(@PathVariable Long id) {
        log.debug("REST request to get Api : {}", id);
        Optional<Api> api = apiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(api);
    }

    /**
     * {@code DELETE  /apis/:id} : delete the "id" api.
     *
     * @param id the id of the api to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apis/{id}")
    public ResponseEntity<Void> deleteApi(@PathVariable Long id) {
        log.debug("REST request to delete Api : {}", id);
        apiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
