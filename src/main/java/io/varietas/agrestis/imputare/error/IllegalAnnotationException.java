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
 * <h1>IllegalAnnotationException</h1>
 *
 * @author Michael Rhöse
 * @since Do, Jun 30, 2016
 */
public class IllegalAnnotationException extends RuntimeException {

    private final Integer code;

    public IllegalAnnotationException(Integer code) {
        this.code = code;
    }

    public IllegalAnnotationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public IllegalAnnotationException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public IllegalAnnotationException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public IllegalAnnotationException(Integer code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    @Override
    public String getLocalizedMessage() {
        // TODO: implement code message
        return super.getLocalizedMessage();
    }

}
