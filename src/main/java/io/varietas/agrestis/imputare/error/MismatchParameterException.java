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
package io.varietas.agrestis.imputare.error;

import java.lang.reflect.Executable;
import java.util.Objects;

/**
 * <h2>MismatchParameterException</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 09/18/2017
 */
public class MismatchParameterException extends RuntimeException {

    final Integer identifierCount;
    final Executable executable;

    public MismatchParameterException(Integer identifierCount, Executable executable) {
        this.identifierCount = identifierCount;
        this.executable = executable;
    }

    public MismatchParameterException(Integer identifierCount, Executable executable, String message) {
        super(message);
        this.identifierCount = identifierCount;
        this.executable = executable;
    }

    public MismatchParameterException(Integer identifierCount, Executable executable, String message, Throwable cause) {
        super(message, cause);
        this.identifierCount = identifierCount;
        this.executable = executable;
    }

    public MismatchParameterException(Integer identifierCount, Executable executable, Throwable cause) {
        super(cause);
        this.identifierCount = identifierCount;
        this.executable = executable;
    }

    public MismatchParameterException(Integer identifierCount, Executable executable, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.identifierCount = identifierCount;
        this.executable = executable;
    }

    @Override
    public String getLocalizedMessage() {
        return "Parameter count (" + this.executable.getParameterCount() + ") doesn't match identifier count (" + this.identifierCount + ") for method '" + this.executable.getName() + "'. "
            + ((Objects.nonNull(super.getLocalizedMessage())) ? super.getLocalizedMessage() : "");
    }
}
