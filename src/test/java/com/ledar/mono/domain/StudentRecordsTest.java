package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentRecordsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentRecords.class);
        StudentRecords studentRecords1 = new StudentRecords();
        studentRecords1.setId(1L);
        StudentRecords studentRecords2 = new StudentRecords();
        studentRecords2.setId(studentRecords1.getId());
        assertThat(studentRecords1).isEqualTo(studentRecords2);
        studentRecords2.setId(2L);
        assertThat(studentRecords1).isNotEqualTo(studentRecords2);
        studentRecords1.setId(null);
        assertThat(studentRecords1).isNotEqualTo(studentRecords2);
    }
}
