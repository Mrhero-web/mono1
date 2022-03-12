package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysRoleDataScopeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysRoleDataScope.class);
        SysRoleDataScope sysRoleDataScope1 = new SysRoleDataScope();
        sysRoleDataScope1.setId(1L);
        SysRoleDataScope sysRoleDataScope2 = new SysRoleDataScope();
        sysRoleDataScope2.setId(sysRoleDataScope1.getId());
        assertThat(sysRoleDataScope1).isEqualTo(sysRoleDataScope2);
        sysRoleDataScope2.setId(2L);
        assertThat(sysRoleDataScope1).isNotEqualTo(sysRoleDataScope2);
        sysRoleDataScope1.setId(null);
        assertThat(sysRoleDataScope1).isNotEqualTo(sysRoleDataScope2);
    }
}
