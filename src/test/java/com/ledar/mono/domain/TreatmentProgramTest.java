package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TreatmentProgramTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TreatmentProgram.class);
        TreatmentProgram treatmentProgram1 = new TreatmentProgram();
        treatmentProgram1.setId(1L);
        TreatmentProgram treatmentProgram2 = new TreatmentProgram();
        treatmentProgram2.setId(treatmentProgram1.getId());
        assertThat(treatmentProgram1).isEqualTo(treatmentProgram2);
        treatmentProgram2.setId(2L);
        assertThat(treatmentProgram1).isNotEqualTo(treatmentProgram2);
        treatmentProgram1.setId(null);
        assertThat(treatmentProgram1).isNotEqualTo(treatmentProgram2);
    }
}
