package com.haot.coupon.common.exceptions;

import com.haot.coupon.common.response.ResCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomCouponException extends RuntimeException{
    public ResCodeIfs resCode;
}
