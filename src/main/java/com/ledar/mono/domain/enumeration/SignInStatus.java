package com.ledar.mono.domain.enumeration;

/**
 * 签到状态
 */
public enum SignInStatus {
    /**
     * 未签到
     */
    DIDNOT,
    /**
     * 已签到
     */
    SIGNIN,
    /**
     * 请假中
     */
    ASKFORLEAVE,
    /**
     * 已请假
     */
    LEAVE,
}
