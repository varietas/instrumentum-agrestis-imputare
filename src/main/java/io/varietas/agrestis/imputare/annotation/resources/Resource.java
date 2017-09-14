/*
 * Copyright 2017 Michael Rhöse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.varietas.agrestis.imputare.annotation.resources;

import io.varietas.agrestis.imputare.contants.AnnotationConstants;
import io.varietas.agrestis.imputare.enumerations.ResourceType;
import io.varietas.agrestis.imputare.utils.containers.DynamicValue;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>Resource</h2>
 *
 * The resource annotation in agrestis imputare marks a basic value as managed. This means that a container is used to store the value and allows reloading by agrestis imputare if needed. Important:
 * If the type is {@link ResourceType#SETTING} the value type has to be {@link DynamicValue}.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 06/20/2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Resource {

    String name() default AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT;

    ResourceType type() default ResourceType.STATIC;
}
