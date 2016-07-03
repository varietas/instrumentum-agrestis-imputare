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
package io.varietas.test.environments.model.utilssimple;

import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import java.util.Random;

/**
 * <h1>SimpleServiceBean</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
@Configuration
public class SimpleConfigurationBean3 {

    private int intValue;

    public SimpleConfigurationBean3() {
        this.intValue = (new Random().nextInt(100));
    }

    public int getIntValue() {
        return intValue;
    }
}
