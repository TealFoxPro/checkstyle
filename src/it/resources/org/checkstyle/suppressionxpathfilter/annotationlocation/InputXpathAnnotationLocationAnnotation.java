package org.checkstyle.suppressionxpathfilter.annotationlocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Annotation("bar") @interface InputXpathAnnotationLocationAnnotation {//warn

}

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@interface Annotation  {

    String value() default "";

}
