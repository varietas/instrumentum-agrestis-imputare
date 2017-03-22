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

import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import java.lang.reflect.Field;
import java8.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>IllegalAnnotationException</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 6/30/2016
 */
@Slf4j
public class IllegalAnnotationException extends RuntimeException {

    private final Integer code;

    public IllegalAnnotationException(String message) {
        this(MethodMetaDataExtractionUtils.AnnotationCodes.NONE, message);
    }

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

        StringBuilder builder = new StringBuilder();

        if (this.getLocalizedMessage().isEmpty()) {
            builder.append("An annotation error occured. ");
        } else {
            builder.append(this.getLocalizedMessage()).append(" ");
        }

        try {
            Object annotationCodesInstance = MethodMetaDataExtractionUtils.AnnotationCodes.class.newInstance();
            Field[] annotationCodesFields = MethodMetaDataExtractionUtils.AnnotationCodes.class.getDeclaredFields();

            for (int index = 1; index < annotationCodesFields.length; ++index) {
                Integer code = (Integer) annotationCodesFields[index].get(annotationCodesInstance);

                if (Objects.equals(this.code, code)) {
                    builder.append("Error code ").append(this.code).append(" [").append(annotationCodesFields[index].getName()).append("] located.");
                    break;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
            LOGGER.error("Error on field access at message building process for IllegalAnnotationException. Thrown exception: " + ex.getLocalizedMessage());
        }

        return builder.toString();
    }

}
