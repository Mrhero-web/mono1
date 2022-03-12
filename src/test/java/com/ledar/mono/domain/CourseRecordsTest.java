package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseRecordsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseRecords.class);
        CourseRecords courseRecords1 = new CourseRecords();
        courseRecords1.setId(1L);
        CourseRecords courseRecords2 = new CourseRecords();
        courseRecords2.setId(courseRecords1.getId());
        assertThat(courseRecords1).isEqualTo(courseRecords2);
        courseRecords2.setId(2L);
        assertThat(courseRecords1).isNotEqualTo(courseRecords2);
        courseRecords1.setId(null);
        assertThat(courseRecords1).isNotEqualTo(courseRecords2);
    }
}
