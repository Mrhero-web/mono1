package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleRecordDetailsNowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleRecordDetailsNow.class);
        ScheduleRecordDetailsNow scheduleRecordDetailsNow1 = new ScheduleRecordDetailsNow();
        scheduleRecordDetailsNow1.setId(1L);
        ScheduleRecordDetailsNow scheduleRecordDetailsNow2 = new ScheduleRecordDetailsNow();
        scheduleRecordDetailsNow2.setId(scheduleRecordDetailsNow1.getId());
        assertThat(scheduleRecordDetailsNow1).isEqualTo(scheduleRecordDetailsNow2);
        scheduleRecordDetailsNow2.setId(2L);
        assertThat(scheduleRecordDetailsNow1).isNotEqualTo(scheduleRecordDetailsNow2);
        scheduleRecordDetailsNow1.setId(null);
        assertThat(scheduleRecordDetailsNow1).isNotEqualTo(scheduleRecordDetailsNow2);
    }
}
