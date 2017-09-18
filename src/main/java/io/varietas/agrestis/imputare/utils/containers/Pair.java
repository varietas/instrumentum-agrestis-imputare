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
package io.varietas.agrestis.imputare.utils.containers;

import lombok.Getter;

/**
 * <h2>Pair</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/4/2016
 */
@Getter
public class Pair<VALUE_1, VALUE_2> {

    private final VALUE_1 value1;
    private final VALUE_2 value2;

    public Pair(VALUE_1 value1, VALUE_2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}
