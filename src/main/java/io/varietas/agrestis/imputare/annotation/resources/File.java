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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>File</h2>
 *
 * This annotation provides all information about a settings file. The settings file contains a map of identifier/value pairs.
 * 
 * @author Michael Rhöse
 * @version 1.0.0, 07/20/2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface File {

    /**
     * Name of the settings file which has to be loaded.
     *
     * @return File name with extension.
     */
    String file();
    
    /**
     * Path to the settings file. If the file is in the class path the path isn't required.
     * 
     * @return Path to the settings file.
     */
    String path() default "CLASSPATH";
}
