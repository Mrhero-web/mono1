package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleRecordNowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleRecordNow.class);
        ScheduleRecordNow scheduleRecordNow1 = new ScheduleRecordNow();
        scheduleRecordNow1.setId(1L);
        ScheduleRecordNow scheduleRecordNow2 = new ScheduleRecordNow();
        scheduleRecordNow2.setId(scheduleRecordNow1.getId());
        assertThat(scheduleRecordNow1).isEqualTo(scheduleRecordNow2);
        scheduleRecordNow2.setId(2L);
        assertThat(scheduleRecordNow1).isNotEqualTo(scheduleRecordNow2);
        scheduleRecordNow1.setId(null);
        assertThat(scheduleRecordNow1).isNotEqualTo(scheduleRecordNow2);
    }
}
