package com.haot.coupon.infrastructure.config;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class QueryDslPageableResolver extends PageableHandlerMethodArgumentResolver {

    public static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 30, 50);
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter,
                                    ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest,
                                    WebDataBinderFactory binderFactory) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        return validate(pageable, this::validatePageNumber, this::validatePageSize, this::validateSort);
    }

    private Pageable validate(Pageable pageable,
                              Function<Integer, Integer> pageNumberValidator,
                              Function<Integer, Integer> pageSizeValidator,
                              Function<Sort, Sort> sortValidator) {
        int pageNumber = pageNumberValidator.apply(pageable.getPageNumber());
        int pageSize = pageSizeValidator.apply(pageable.getPageSize());
        Sort sort = sortValidator.apply(pageable.getSort());

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    // 음수가 오면 0이 기본값
    private Integer validatePageNumber(int pageNumber) {
        return Math.max(pageNumber, 0);
    }

    // 10, 30, 50이 아니면 -> 기본값 10
    private Integer validatePageSize(int pageSize) {
        return ALLOWED_PAGE_SIZES.contains(pageSize) ? pageSize : DEFAULT_PAGE_SIZE;
    }

    // sort 객체가 없으면 createdAt desc
    private Sort validateSort(Sort sort) {
        return sort.isSorted() ? sort : DEFAULT_SORT;
    }
}

