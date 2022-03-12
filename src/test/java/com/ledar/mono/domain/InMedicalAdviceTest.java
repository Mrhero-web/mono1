package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InMedicalAdviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InMedicalAdvice.class);
        InMedicalAdvice inMedicalAdvice1 = new InMedicalAdvice();
        inMedicalAdvice1.setId(1L);
        InMedicalAdvice inMedicalAdvice2 = new InMedicalAdvice();
        inMedicalAdvice2.setId(inMedicalAdvice1.getId());
        assertThat(inMedicalAdvice1).isEqualTo(inMedicalAdvice2);
        inMedicalAdvice2.setId(2L);
        assertThat(inMedicalAdvice1).isNotEqualTo(inMedicalAdvice2);
        inMedicalAdvice1.setId(null);
        assertThat(inMedicalAdvice1).isNotEqualTo(inMedicalAdvice2);
    }
}
