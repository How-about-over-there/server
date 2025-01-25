package com.haot.lodge.infrastructure.config;


import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class QueryDslPageableResolver extends PageableHandlerMethodArgumentResolver {

    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt", "updatedAt");
    public static final int MAXIMUM_SIZE = 50;

    @Override
    public @NotNull Pageable resolveArgument(
            @NotNull MethodParameter methodParameter,
            ModelAndViewContainer mavContainer,
            @NotNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        return validate(pageable, this::validatePageNumber, this::validatePageSize, this::validateSort);
    }

    private Pageable validate(
            Pageable pageable,
            Function<Integer, Integer> pageNumberValidator,
            Function<Integer, Integer> pageSizeValidator,
            Function<Sort, Sort> sortValidator
    ) {
        int pageNumber = pageNumberValidator.apply(pageable.getPageNumber());
        int pageSize = pageSizeValidator.apply(pageable.getPageSize());
        Sort sort = sortValidator.apply(pageable.getSort());

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private Integer validatePageNumber(int pageNumber) {
        return Math.max(pageNumber, 0);
    }

    private Integer validatePageSize(int pageSize) {
        return Math.min(pageSize, MAXIMUM_SIZE);
    }

    private Sort validateSort(Sort sort) {
        return sort.isSorted() ? sort : DEFAULT_SORT;
    }

}
