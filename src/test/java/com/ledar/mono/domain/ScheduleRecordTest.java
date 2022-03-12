package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleRecord.class);
        ScheduleRecord scheduleRecord1 = new ScheduleRecord();
        scheduleRecord1.setId(1L);
        ScheduleRecord scheduleRecord2 = new ScheduleRecord();
        scheduleRecord2.setId(scheduleRecord1.getId());
        assertThat(scheduleRecord1).isEqualTo(scheduleRecord2);
        scheduleRecord2.setId(2L);
        assertThat(scheduleRecord1).isNotEqualTo(scheduleRecord2);
        scheduleRecord1.setId(null);
        assertThat(scheduleRecord1).isNotEqualTo(scheduleRecord2);
    }
}
