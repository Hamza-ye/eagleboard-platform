package com.mass3d.webapi.mvc.annotation;

import com.mass3d.common.DhisApiVersion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target( { ElementType.TYPE, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface ApiVersion
{
    @AliasFor( "include" )
    DhisApiVersion[] value() default DhisApiVersion.ALL;

    @AliasFor( "value" )
    DhisApiVersion[] include() default DhisApiVersion.ALL;

    DhisApiVersion[] exclude() default {};
}
