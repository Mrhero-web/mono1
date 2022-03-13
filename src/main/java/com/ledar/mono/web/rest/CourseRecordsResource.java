package com.ledar.mono.web.rest;

import com.ledar.mono.common.querydsl.OptionalBooleanBuilder;
import com.ledar.mono.common.response.PaginationUtil;
import com.ledar.mono.domain.CourseRecords;
import com.ledar.mono.domain.Patient;
import com.ledar.mono.domain.QCourseRecords;
import com.ledar.mono.repository.CourseRecordsRepository;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ledar.mono.domain.CourseRecords}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CourseRecordsResource {

    private final Logger log = LoggerFactory.getLogger(CourseRecordsResource.class);

    private static final String ENTITY_NAME = "courseRecords";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final JPAQueryFactory queryFactory;
    private final CourseRecordsRepository courseRecordsRepository;
    private final QCourseRecords qCourseRecords = QCourseRecords.courseRecords;


    public CourseRecordsResource(JPAQueryFactory queryFactory, CourseRecordsRepository courseRecordsRepository) {
        this.queryFactory = queryFactory;
        this.courseRecordsRepository = courseRecordsRepository;
    }

    /**
     * {@code POST  /course-records} : Create a new courseRecords.
     *
     * @param courseRecords the courseRecords to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseRecords, or with status {@code 400 (Bad Request)} if the courseRecords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-records")
    public ResponseEntity<CourseRecords> createCourseRecords(@RequestBody CourseRecords courseRecords) throws URISyntaxException {
        log.debug("REST request to save CourseRecords : {}", courseRecords);
        if (courseRecords.getId() != null) {
            throw new BadRequestAlertException("A new courseRecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseRecords result = courseRecordsRepository.save(courseRecords);
        return ResponseEntity
            .created(new URI("/api/course-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-records/:id} : Updates an existing courseRecords.
     *
     * @param id the id of the courseRecords to save.
     * @param courseRecords the courseRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseRecords,
     * or with status {@code 400 (Bad Request)} if the courseRecords is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-records/{id}")
    public ResponseEntity<CourseRecords> updateCourseRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseRecords courseRecords
    ) throws URISyntaxException {
        log.debug("REST request to update CourseRecords : {}, {}", id, courseRecords);
        if (courseRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseRecords result = courseRecordsRepository.save(courseRecords);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseRecords.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-records/:id} : Partial updates given fields of an existing courseRecords, field will ignore if it is null
     *
     * @param id the id of the courseRecords to save.
     * @param courseRecords the courseRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseRecords,
     * or with status {@code 400 (Bad Request)} if the courseRecords is not valid,
     * or with status {@code 404 (Not Found)} if the courseRecords is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseRecords> partialUpdateCourseRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourseRecords courseRecords
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseRecords partially : {}, {}", id, courseRecords);
        if (courseRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseRecords> result = courseRecordsRepository
            .findById(courseRecords.getId())
            .map(existingCourseRecords -> {
                if (courseRecords.getcId() != null) {
                    existingCourseRecords.setcId(courseRecords.getcId());
                }
                if (courseRecords.gettId() != null) {
                    existingCourseRecords.settId(courseRecords.gettId());
                }
                if (courseRecords.getrId() != null) {
                    existingCourseRecords.setrId(courseRecords.getrId());
                }
                if (courseRecords.getClassDate() != null) {
                    existingCourseRecords.setClassDate(courseRecords.getClassDate());
                }
                if (courseRecords.getSchoolTime() != null) {
                    existingCourseRecords.setSchoolTime(courseRecords.getSchoolTime());
                }
                if (courseRecords.getClassTime() != null) {
                    existingCourseRecords.setClassTime(courseRecords.getClassTime());
                }
                if (courseRecords.getcStatus() != null) {
                    existingCourseRecords.setcStatus(courseRecords.getcStatus());
                }
                if (courseRecords.getModified() != null) {
                    existingCourseRecords.setModified(courseRecords.getModified());
                }

                return existingCourseRecords;
            })
            .map(courseRecordsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseRecords.getId().toString())
        );
    }

    /**
     * {@code GET  /course-records} : get all the courseRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseRecords in body.
     */
    @GetMapping("/course-records")
    public List<CourseRecords> getAllCourseRecords() {
        log.debug("REST request to get all CourseRecords");
        return courseRecordsRepository.findAll();
    }

    @Operation(summary ="患儿课程记录列表", description="作者：田春晓")
    @GetMapping("/course-records/courseRrecordsList")
    public ResponseEntity<List<CourseRecords>>courseRecordsList(@RequestParam(required = false)Instant classDate,
                                                                @RequestParam(required = false)Instant schoolTime,
                                                                @RequestParam(required = false)Instant classTime,
                                                                Pageable pageable) {
        OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
            //Instant是精确到秒，LocalDate 是日期
            //大于上课日期 & 时间 ，小于下课时间
            .notEmptyAnd(qCourseRecords.classDate::goe, classDate)
            .notEmptyAnd(qCourseRecords.schoolTime::goe,schoolTime)
            .notEmptyAnd(qCourseRecords.classTime::loe,classTime);

        JPAQuery<CourseRecords> jpaQuery = queryFactory.selectFrom(qCourseRecords)
            .where(predicate.build())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset());
        Page<CourseRecords> page = new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-records/:id} : get the "id" courseRecords.
     *
     * @param id the id of the courseRecords to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseRecords, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-records/{id}")
    public ResponseEntity<CourseRecords> getCourseRecords(@PathVariable Long id) {
        log.debug("REST request to get CourseRecords : {}", id);
        Optional<CourseRecords> courseRecords = courseRecordsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(courseRecords);
    }

    /**
     * {@code DELETE  /course-records/:id} : delete the "id" courseRecords.
     *
     * @param id the id of the courseRecords to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-records/{id}")
    public ResponseEntity<Void> deleteCourseRecords(@PathVariable Long id) {
        log.debug("REST request to delete CourseRecords : {}", id);
        courseRecordsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
