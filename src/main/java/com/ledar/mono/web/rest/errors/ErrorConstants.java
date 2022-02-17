package com.ledar.mono.web.rest.errors;

import java.net.URI;
import org.zalando.problem.Problem;

public final class ErrorConstants {

    public static final String ERR_VALIDATION = "error.validation";
    public static final URI DEFAULT_TYPE = Problem.DEFAULT_TYPE;

    private ErrorConstants() {}
}
