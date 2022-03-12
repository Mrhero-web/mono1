package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EItem2Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EItem2.class);
        EItem2 eItem21 = new EItem2();
        eItem21.setId(1L);
        EItem2 eItem22 = new EItem2();
        eItem22.setId(eItem21.getId());
        assertThat(eItem21).isEqualTo(eItem22);
        eItem22.setId(2L);
        assertThat(eItem21).isNotEqualTo(eItem22);
        eItem21.setId(null);
        assertThat(eItem21).isNotEqualTo(eItem22);
    }
}
