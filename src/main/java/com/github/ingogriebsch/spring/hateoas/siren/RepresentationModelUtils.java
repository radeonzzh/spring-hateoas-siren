/*-
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ingogriebsch.spring.hateoas.siren;

import static com.google.common.collect.Lists.newArrayList;
import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Utility methods help to understand of which type of {@link RepresentationModel} a specific object is.
 * 
 * @author Ingo Griebsch
 */
@NoArgsConstructor(access = PRIVATE)
class RepresentationModelUtils {

    // Order on purpose!
    private static final List<Class<?>> REPRESENTATION_MODEL_TYPES =
        newArrayList(PagedModel.class, CollectionModel.class, EntityModel.class, RepresentationModel.class);

    static boolean isRepresentationModel(@NonNull Class<?> clazz) {
        for (Class<?> resourceType : REPRESENTATION_MODEL_TYPES) {
            if (resourceType.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    static boolean isRepresentationModelSubclass(@NonNull Class<?> clazz) {
        return !REPRESENTATION_MODEL_TYPES.contains(clazz) && REPRESENTATION_MODEL_TYPES.contains(clazz.getSuperclass());
    }

    @SuppressWarnings("unchecked")
    static Class<? extends RepresentationModel<?>> getRepresentationModelClass(@NonNull Class<?> clazz) {
        for (Class<?> resourceType : REPRESENTATION_MODEL_TYPES) {
            if (resourceType.isAssignableFrom(clazz)) {
                return (Class<RepresentationModel<?>>) resourceType;
            }
        }
        return null;
    }

}
