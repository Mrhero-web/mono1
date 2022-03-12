package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysUserDataScopeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysUserDataScope.class);
        SysUserDataScope sysUserDataScope1 = new SysUserDataScope();
        sysUserDataScope1.setId(1L);
        SysUserDataScope sysUserDataScope2 = new SysUserDataScope();
        sysUserDataScope2.setId(sysUserDataScope1.getId());
        assertThat(sysUserDataScope1).isEqualTo(sysUserDataScope2);
        sysUserDataScope2.setId(2L);
        assertThat(sysUserDataScope1).isNotEqualTo(sysUserDataScope2);
        sysUserDataScope1.setId(null);
        assertThat(sysUserDataScope1).isNotEqualTo(sysUserDataScope2);
    }
}
