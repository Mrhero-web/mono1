package com.ledar.mono.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Collection;
import java.util.function.Function;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

public final class BooleanBuilderUtil {

    private final BooleanBuilder predicate;

    public BooleanBuilderUtil() {
        this.predicate = new BooleanBuilder();
    }

    public <T> BooleanBuilderUtil notNullAnd(Function<T, BooleanExpression> expressionFunction, T value) {
        if (value != null) {
            this.predicate.and(expressionFunction.apply(value));
        }
        return this;
    }

    public <T> BooleanBuilderUtil notEmptyAnd(Function<T, BooleanExpression> expressionFunction, T value) {
        if (!ObjectUtils.isEmpty(value)) {
            this.predicate.and(expressionFunction.apply(value));
        }
        return this;
    }

    public <T> BooleanBuilderUtil notEmptyAnd(Function<Collection<T>, BooleanExpression> expressionFunction, Collection<T> collection) {
        if (!CollectionUtils.isEmpty(collection)) {
            this.predicate.and(expressionFunction.apply(collection));
        }
        return this;
    }

    public <T> BooleanBuilderUtil and(Function<T, BooleanExpression> expressionFunction, T value) {
        this.predicate.and(expressionFunction.apply(value));
        return this;
    }

    public BooleanBuilder build() {
        return this.predicate;
    }
}
