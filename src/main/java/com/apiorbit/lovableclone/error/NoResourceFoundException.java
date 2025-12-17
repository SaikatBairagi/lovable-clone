package com.apiorbit.lovableclone.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class NoResourceFoundException extends RuntimeException {
    String resourceName;
    String resourceId;
}
