package com.ledar.mono.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ledar.mono.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysRoleMenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysRoleMenu.class);
        SysRoleMenu sysRoleMenu1 = new SysRoleMenu();
        sysRoleMenu1.setId(1L);
        SysRoleMenu sysRoleMenu2 = new SysRoleMenu();
        sysRoleMenu2.setId(sysRoleMenu1.getId());
        assertThat(sysRoleMenu1).isEqualTo(sysRoleMenu2);
        sysRoleMenu2.setId(2L);
        assertThat(sysRoleMenu1).isNotEqualTo(sysRoleMenu2);
        sysRoleMenu1.setId(null);
        assertThat(sysRoleMenu1).isNotEqualTo(sysRoleMenu2);
    }
}
