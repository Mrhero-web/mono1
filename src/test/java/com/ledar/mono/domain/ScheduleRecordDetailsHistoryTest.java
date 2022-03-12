package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleRecordDetailsHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleRecordDetailsHistory.class);
        ScheduleRecordDetailsHistory scheduleRecordDetailsHistory1 = new ScheduleRecordDetailsHistory();
        scheduleRecordDetailsHistory1.setId(1L);
        ScheduleRecordDetailsHistory scheduleRecordDetailsHistory2 = new ScheduleRecordDetailsHistory();
        scheduleRecordDetailsHistory2.setId(scheduleRecordDetailsHistory1.getId());
        assertThat(scheduleRecordDetailsHistory1).isEqualTo(scheduleRecordDetailsHistory2);
        scheduleRecordDetailsHistory2.setId(2L);
        assertThat(scheduleRecordDetailsHistory1).isNotEqualTo(scheduleRecordDetailsHistory2);
        scheduleRecordDetailsHistory1.setId(null);
        assertThat(scheduleRecordDetailsHistory1).isNotEqualTo(scheduleRecordDetailsHistory2);
    }
}
