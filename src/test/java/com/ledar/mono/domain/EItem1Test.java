package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EItem1Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EItem1.class);
        EItem1 eItem11 = new EItem1();
        eItem11.setId(1L);
        EItem1 eItem12 = new EItem1();
        eItem12.setId(eItem11.getId());
        assertThat(eItem11).isEqualTo(eItem12);
        eItem12.setId(2L);
        assertThat(eItem11).isNotEqualTo(eItem12);
        eItem11.setId(null);
        assertThat(eItem11).isNotEqualTo(eItem12);
    }
}
