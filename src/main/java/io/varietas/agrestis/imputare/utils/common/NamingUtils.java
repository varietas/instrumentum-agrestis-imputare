/*
 * Copyright 2016 varietas.io
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
package io.varietas.agrestis.imputare.utils.common;

import io.varietas.agrestis.imputare.contant.AnnotationConstants;

/**
 * <h2>NamingUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/4/2016
 */
public class NamingUtils {

    /**
     * Formats the identifier of a bean to the internal required format. This format follows the java naming conventions.
     *
     * @param identifier Custom or default name loads from an annotation.
     * @param defaultName Required class/method name if the identifier is the default.
     * @return
     */
    public static String formatIdentifier(String identifier, final String defaultName) {
        if (!identifier.equals(AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT)) {
            return identifier.substring(0, 1).toLowerCase() + identifier.substring(1);
        }
        return defaultName.substring(0, 1).toLowerCase() + defaultName.substring(1);
    }
}
