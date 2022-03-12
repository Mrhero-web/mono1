package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutMedicalAdviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutMedicalAdvice.class);
        OutMedicalAdvice outMedicalAdvice1 = new OutMedicalAdvice();
        outMedicalAdvice1.setId(1L);
        OutMedicalAdvice outMedicalAdvice2 = new OutMedicalAdvice();
        outMedicalAdvice2.setId(outMedicalAdvice1.getId());
        assertThat(outMedicalAdvice1).isEqualTo(outMedicalAdvice2);
        outMedicalAdvice2.setId(2L);
        assertThat(outMedicalAdvice1).isNotEqualTo(outMedicalAdvice2);
        outMedicalAdvice1.setId(null);
        assertThat(outMedicalAdvice1).isNotEqualTo(outMedicalAdvice2);
    }
}
