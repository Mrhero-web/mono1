package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PastCourseRecordsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PastCourseRecords.class);
        PastCourseRecords pastCourseRecords1 = new PastCourseRecords();
        pastCourseRecords1.setId(1L);
        PastCourseRecords pastCourseRecords2 = new PastCourseRecords();
        pastCourseRecords2.setId(pastCourseRecords1.getId());
        assertThat(pastCourseRecords1).isEqualTo(pastCourseRecords2);
        pastCourseRecords2.setId(2L);
        assertThat(pastCourseRecords1).isNotEqualTo(pastCourseRecords2);
        pastCourseRecords1.setId(null);
        assertThat(pastCourseRecords1).isNotEqualTo(pastCourseRecords2);
    }
}
