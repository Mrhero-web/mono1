package com.ledar.mono.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BadRequestProblem extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public BadRequestProblem(String title) {
        super(ErrorConstants.DEFAULT_TYPE, title, Status.BAD_REQUEST);
    }

    public BadRequestProblem(String title, String detail) {
        super(ErrorConstants.DEFAULT_TYPE, title, Status.BAD_REQUEST, detail);
    }
}
