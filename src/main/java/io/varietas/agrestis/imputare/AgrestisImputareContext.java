/*
 * Copyright 2016 Michael Rhöse.
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
package io.varietas.agrestis.imputare;

import java8.util.Optional;

/**
 * <h2>AgrestisImputareContext</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 5/7/2016
 */
public interface AgrestisImputareContext {

    public <T> Optional<T> getBean(Class<T> beanClazz);

    public <T> Optional<T> getBean(String beanIdentifier, Class<T> targetType);

    public Optional<AgrestisImputareContext> getContext();
}
