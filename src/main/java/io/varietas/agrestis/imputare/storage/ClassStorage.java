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
package io.varietas.agrestis.imputare.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * <h1>ClassStorage</h1>
 *
 * This class represents a container to store all located, annotated classes. Additionally there are a number of useful methods.
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
public class ClassStorage implements UnsortedStorage<Class<?>> {

    private final List<Class<?>> clazzes;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassStorage() {
        this(0);
    }

    public ClassStorage(int listSize) {
        this.clazzes = new ArrayList<>(listSize);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Loads the next class from the storage. Important is that this class will be removed from the storage.
     *
     * @return Next class from the storage.
     */
    @Override
    public Optional<Class<?>> next() {
        if (this.clazzes.isEmpty()) {
            return Optional.empty();
        }
        Class<?> res = this.clazzes.get(this.clazzes.size() - 1);
        this.clazzes.remove(res);
        return Optional.of(res);
    }

    /**
     * Stores a class in the storage. Returns -1 if the class is not stored otherwise the current number of stored classes will be returned.
     *
     * @param clazz Class to be stored.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int store(final Class<?> clazz) {
        if (!this.clazzes.add(clazz)) {
            return this.clazzes.size();
        } else {
            return -1;
        }
    }

    /**
     * Stores all classes from a given collection in the storage. Returns -1 if the classes are not stored otherwise the current number of stored classes will be returned.
     *
     * @param clazzes Classes to be stored.
     * @return Number of stored classes or -1 for an error.
     */
    public int storeAll(Collection<Class<?>> clazzes) {
        if (!this.clazzes.addAll(clazzes)) {
            return this.clazzes.size();
        } else {
            return -1;
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * All stored classes as list.
     *
     * @return
     */
    @Override
    public List<Class<?>> getStorage() {
        return clazzes;
    }

}
