package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PastStudentRecordsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PastStudentRecords.class);
        PastStudentRecords pastStudentRecords1 = new PastStudentRecords();
        pastStudentRecords1.setId(1L);
        PastStudentRecords pastStudentRecords2 = new PastStudentRecords();
        pastStudentRecords2.setId(pastStudentRecords1.getId());
        assertThat(pastStudentRecords1).isEqualTo(pastStudentRecords2);
        pastStudentRecords2.setId(2L);
        assertThat(pastStudentRecords1).isNotEqualTo(pastStudentRecords2);
        pastStudentRecords1.setId(null);
        assertThat(pastStudentRecords1).isNotEqualTo(pastStudentRecords2);
    }
}
