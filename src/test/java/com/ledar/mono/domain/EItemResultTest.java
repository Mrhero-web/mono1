package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EItemResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EItemResult.class);
        EItemResult eItemResult1 = new EItemResult();
        eItemResult1.setId(1L);
        EItemResult eItemResult2 = new EItemResult();
        eItemResult2.setId(eItemResult1.getId());
        assertThat(eItemResult1).isEqualTo(eItemResult2);
        eItemResult2.setId(2L);
        assertThat(eItemResult1).isNotEqualTo(eItemResult2);
        eItemResult1.setId(null);
        assertThat(eItemResult1).isNotEqualTo(eItemResult2);
    }
}
