package com.ledar.mono.web.rest;

import com.ledar.mono.domain.ListOfCourses;
import com.ledar.mono.repository.ListOfCoursesRepository;
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
 * REST controller for managing {@link com.ledar.mono.domain.ListOfCourses}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ListOfCoursesResource {

    private final Logger log = LoggerFactory.getLogger(ListOfCoursesResource.class);

    private static final String ENTITY_NAME = "listOfCourses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ListOfCoursesRepository listOfCoursesRepository;

    public ListOfCoursesResource(ListOfCoursesRepository listOfCoursesRepository) {
        this.listOfCoursesRepository = listOfCoursesRepository;
    }

    /**
     * {@code POST  /list-of-courses} : Create a new listOfCourses.
     *
     * @param listOfCourses the listOfCourses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new listOfCourses, or with status {@code 400 (Bad Request)} if the listOfCourses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/list-of-courses")
    public ResponseEntity<ListOfCourses> createListOfCourses(@RequestBody ListOfCourses listOfCourses) throws URISyntaxException {
        log.debug("REST request to save ListOfCourses : {}", listOfCourses);
        if (listOfCourses.getId() != null) {
            throw new BadRequestAlertException("A new listOfCourses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ListOfCourses result = listOfCoursesRepository.save(listOfCourses);
        return ResponseEntity
            .created(new URI("/api/list-of-courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /list-of-courses/:id} : Updates an existing listOfCourses.
     *
     * @param id the id of the listOfCourses to save.
     * @param listOfCourses the listOfCourses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listOfCourses,
     * or with status {@code 400 (Bad Request)} if the listOfCourses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the listOfCourses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/list-of-courses/{id}")
    public ResponseEntity<ListOfCourses> updateListOfCourses(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ListOfCourses listOfCourses
    ) throws URISyntaxException {
        log.debug("REST request to update ListOfCourses : {}, {}", id, listOfCourses);
        if (listOfCourses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, listOfCourses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listOfCoursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ListOfCourses result = listOfCoursesRepository.save(listOfCourses);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, listOfCourses.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /list-of-courses/:id} : Partial updates given fields of an existing listOfCourses, field will ignore if it is null
     *
     * @param id the id of the listOfCourses to save.
     * @param listOfCourses the listOfCourses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listOfCourses,
     * or with status {@code 400 (Bad Request)} if the listOfCourses is not valid,
     * or with status {@code 404 (Not Found)} if the listOfCourses is not found,
     * or with status {@code 500 (Internal Server Error)} if the listOfCourses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/list-of-courses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ListOfCourses> partialUpdateListOfCourses(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ListOfCourses listOfCourses
    ) throws URISyntaxException {
        log.debug("REST request to partial update ListOfCourses partially : {}, {}", id, listOfCourses);
        if (listOfCourses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, listOfCourses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listOfCoursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ListOfCourses> result = listOfCoursesRepository
            .findById(listOfCourses.getId())
            .map(existingListOfCourses -> {
                if (listOfCourses.getcId() != null) {
                    existingListOfCourses.setcId(listOfCourses.getcId());
                }
                if (listOfCourses.getpId() != null) {
                    existingListOfCourses.setpId(listOfCourses.getpId());
                }
                if (listOfCourses.gettId() != null) {
                    existingListOfCourses.settId(listOfCourses.gettId());
                }
                if (listOfCourses.getrId() != null) {
                    existingListOfCourses.setrId(listOfCourses.getrId());
                }
                if (listOfCourses.getSchoolTime() != null) {
                    existingListOfCourses.setSchoolTime(listOfCourses.getSchoolTime());
                }
                if (listOfCourses.getClassTime() != null) {
                    existingListOfCourses.setClassTime(listOfCourses.getClassTime());
                }
                if (listOfCourses.getlType() != null) {
                    existingListOfCourses.setlType(listOfCourses.getlType());
                }

                return existingListOfCourses;
            })
            .map(listOfCoursesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, listOfCourses.getId().toString())
        );
    }

    /**
     * {@code GET  /list-of-courses} : get all the listOfCourses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of listOfCourses in body.
     */
    @GetMapping("/list-of-courses")
    public List<ListOfCourses> getAllListOfCourses() {
        log.debug("REST request to get all ListOfCourses");
        return listOfCoursesRepository.findAll();
    }

    /**
     * {@code GET  /list-of-courses/:id} : get the "id" listOfCourses.
     *
     * @param id the id of the listOfCourses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the listOfCourses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/list-of-courses/{id}")
    public ResponseEntity<ListOfCourses> getListOfCourses(@PathVariable Long id) {
        log.debug("REST request to get ListOfCourses : {}", id);
        Optional<ListOfCourses> listOfCourses = listOfCoursesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(listOfCourses);
    }

    /**
     * {@code DELETE  /list-of-courses/:id} : delete the "id" listOfCourses.
     *
     * @param id the id of the listOfCourses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/list-of-courses/{id}")
    public ResponseEntity<Void> deleteListOfCourses(@PathVariable Long id) {
        log.debug("REST request to delete ListOfCourses : {}", id);
        listOfCoursesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
