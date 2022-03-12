package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EFormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EForm.class);
        EForm eForm1 = new EForm();
        eForm1.setId(1L);
        EForm eForm2 = new EForm();
        eForm2.setId(eForm1.getId());
        assertThat(eForm1).isEqualTo(eForm2);
        eForm2.setId(2L);
        assertThat(eForm1).isNotEqualTo(eForm2);
        eForm1.setId(null);
        assertThat(eForm1).isNotEqualTo(eForm2);
    }
}
