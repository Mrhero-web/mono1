package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysRoleApiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysRoleApi.class);
        SysRoleApi sysRoleApi1 = new SysRoleApi();
        sysRoleApi1.setId(1L);
        SysRoleApi sysRoleApi2 = new SysRoleApi();
        sysRoleApi2.setId(sysRoleApi1.getId());
        assertThat(sysRoleApi1).isEqualTo(sysRoleApi2);
        sysRoleApi2.setId(2L);
        assertThat(sysRoleApi1).isNotEqualTo(sysRoleApi2);
        sysRoleApi1.setId(null);
        assertThat(sysRoleApi1).isNotEqualTo(sysRoleApi2);
    }
}
