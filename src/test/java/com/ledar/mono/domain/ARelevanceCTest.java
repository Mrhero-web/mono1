package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ARelevanceCTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ARelevanceC.class);
        ARelevanceC aRelevanceC1 = new ARelevanceC();
        aRelevanceC1.setId(1L);
        ARelevanceC aRelevanceC2 = new ARelevanceC();
        aRelevanceC2.setId(aRelevanceC1.getId());
        assertThat(aRelevanceC1).isEqualTo(aRelevanceC2);
        aRelevanceC2.setId(2L);
        assertThat(aRelevanceC1).isNotEqualTo(aRelevanceC2);
        aRelevanceC1.setId(null);
        assertThat(aRelevanceC1).isNotEqualTo(aRelevanceC2);
    }
}
