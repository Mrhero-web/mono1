package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleRecordHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleRecordHistory.class);
        ScheduleRecordHistory scheduleRecordHistory1 = new ScheduleRecordHistory();
        scheduleRecordHistory1.setId(1L);
        ScheduleRecordHistory scheduleRecordHistory2 = new ScheduleRecordHistory();
        scheduleRecordHistory2.setId(scheduleRecordHistory1.getId());
        assertThat(scheduleRecordHistory1).isEqualTo(scheduleRecordHistory2);
        scheduleRecordHistory2.setId(2L);
        assertThat(scheduleRecordHistory1).isNotEqualTo(scheduleRecordHistory2);
        scheduleRecordHistory1.setId(null);
        assertThat(scheduleRecordHistory1).isNotEqualTo(scheduleRecordHistory2);
    }
}
