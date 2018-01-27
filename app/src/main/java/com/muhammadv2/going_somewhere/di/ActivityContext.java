package com.muhammadv2.going_somewhere.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * This @Qualifier is used to distinguish between Context objects of the
 * same type but with different instances.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContext {
}