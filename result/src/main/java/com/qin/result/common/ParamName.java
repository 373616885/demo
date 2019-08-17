package com.qin.result.common;

import java.lang.annotation.*;


@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamName {

    /**
     * The name of the request parameter to bind to.
     */
    String value();
}
