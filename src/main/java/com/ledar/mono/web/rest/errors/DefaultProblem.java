package com.ledar.mono.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class DefaultProblem extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public DefaultProblem(String title, Status status) {
        super(ErrorConstants.DEFAULT_TYPE, title, status);
    }

    public DefaultProblem(String title, Status status, String detail) {
        super(ErrorConstants.DEFAULT_TYPE, title, status, detail);
    }
}
