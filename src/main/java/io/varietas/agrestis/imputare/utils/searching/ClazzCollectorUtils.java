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
package io.varietas.agrestis.imputare.utils.searching;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import io.varietas.agrestis.imputare.annotation.Component;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.agrestis.imputare.annotation.Controller;
import io.varietas.agrestis.imputare.annotation.Repository;
import io.varietas.agrestis.imputare.annotation.Service;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ClazzCollectorUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 12/1/2016
 */
@Slf4j
public class ClazzCollectorUtils {

    public static List<Class<?>> loadClazzes() throws IOException {

        ScanResult res = new FastClasspathScanner().scan();

        ///< Load all classes from the given package
        return res.classNamesToClassRefs(res.getNamesOfClassesWithAnnotationsAnyOf(Repository.class, Service.class, Controller.class, Component.class, Configuration.class));
    }
}
