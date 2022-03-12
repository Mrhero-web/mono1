package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleRecordDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleRecordDetails.class);
        ScheduleRecordDetails scheduleRecordDetails1 = new ScheduleRecordDetails();
        scheduleRecordDetails1.setId(1L);
        ScheduleRecordDetails scheduleRecordDetails2 = new ScheduleRecordDetails();
        scheduleRecordDetails2.setId(scheduleRecordDetails1.getId());
        assertThat(scheduleRecordDetails1).isEqualTo(scheduleRecordDetails2);
        scheduleRecordDetails2.setId(2L);
        assertThat(scheduleRecordDetails1).isNotEqualTo(scheduleRecordDetails2);
        scheduleRecordDetails1.setId(null);
        assertThat(scheduleRecordDetails1).isNotEqualTo(scheduleRecordDetails2);
    }
}
