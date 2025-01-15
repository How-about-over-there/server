package com.haot.reservation.infrastructure.config;

import java.util.List;
import java.util.function.Function;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class QueryDslPageableResolver extends PageableHandlerMethodArgumentResolver {

  private static final List<Integer> ALLOWED_PAGE_SIZES = List.of(10, 30, 50);
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

  @Override
  public Pageable resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    return validatePageable(pageable);
  }

  private Pageable validatePageable(Pageable pageable) {
    int validatedPageNumber = validate(pageable.getPageNumber(), this::validatePageNumber);
    int validatedPageSize = validate(pageable.getPageSize(), this::validatePageSize);
    Sort validatedSort = validate(pageable.getSort(), this::validateSort);

    return PageRequest.of(validatedPageNumber, validatedPageSize, validatedSort);
  }

  private <T> T validate(T value, Function<T, T> validator) {
    return validator.apply(value);
  }

  private int validatePageNumber(int pageNumber) {
    return Math.max(pageNumber, 0);
  }

  private int validatePageSize(int pageSize) {
    return ALLOWED_PAGE_SIZES.contains(pageSize) ? pageSize : DEFAULT_PAGE_SIZE;
  }

  private Sort validateSort(Sort sort) {
    return sort.isSorted() ? sort : DEFAULT_SORT;
  }
}
