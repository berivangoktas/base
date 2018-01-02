package com.junited.selenium.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Contact
{
    // primary contact
    Kure value();

    // additional contacts to notify / consult
    Kure[] cc() default {};
}
