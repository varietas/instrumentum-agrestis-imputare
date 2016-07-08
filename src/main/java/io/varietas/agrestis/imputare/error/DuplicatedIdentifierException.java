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
package io.varietas.agrestis.imputare.error;

/**
 * <h1>DuplicatedIdentifierException</h1>
 *
 * @author Michael Rhöse
 * @since Do, Jul 7, 2016
 */
public class DuplicatedIdentifierException extends RuntimeException {

    public DuplicatedIdentifierException() {
    }

    public DuplicatedIdentifierException(String message) {
        super(message);
    }

    public DuplicatedIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedIdentifierException(Throwable cause) {
        super(cause);
    }

    public DuplicatedIdentifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}