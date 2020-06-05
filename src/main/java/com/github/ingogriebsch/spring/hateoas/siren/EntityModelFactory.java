/*-
 * #%L
 * Spring HATEOAS Siren
 * %%
 * Copyright (C) 2018 - 2020 Ingo Griebsch
 * %%
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
 * #L%
 */
package com.github.ingogriebsch.spring.hateoas.siren;

import static com.github.ingogriebsch.spring.hateoas.siren.BeanUtils.applyProperties;
import static com.github.ingogriebsch.spring.hateoas.siren.BeanUtils.instantiate;
import static org.springframework.util.Assert.isAssignable;

import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import lombok.NonNull;

/**
 * Factory to create a {@link EntityModel} instance based on the given input evaluated during deserialization.
 * 
 * @author Ingo Griebsch
 */
public interface EntityModelFactory {

    /**
     * Creates a {@link EntityModel} based on the given input.
     * <p>
     * The default implementation instantiates the concrete {@link EntityModel} based on the assumption that the
     * {@link EntityModel#EntityModel(Object, Iterable)} constructor is accessible.
     * 
     * @param type the type of the instance that should be created. Must be assignable to {@link EntityModel}. Is never
     *        {@literal null}.
     * @param links the links which should be used to instantiate the {@link EntityModel model}. Is never {@literal null}.
     * @param content the object which should be used to instantiate the {@link EntityModel model}. Is never {@literal null}.
     * @param properties the properties which should be applied on the instantiated {@link EntityModel model}. Can be
     *        {@literal null}.
     * @return the created {@link EntityModel} instance.
     */
    default EntityModel<?> create(@NonNull JavaType type, @NonNull Iterable<Link> links, @NonNull Object content,
        Map<String, Object> properties) {
        Class<?> modelType = type.getRawClass();
        isAssignable(EntityModel.class, modelType);

        // TODO check if type of object matches contained type?

        Class<?>[] types =
            new Class[] { EntityModel.class.equals(modelType) ? Object.class : content.getClass(), Iterable.class };
        Object[] args = new Object[] { content, links };
        EntityModel<?> model = (EntityModel<?>) instantiate(modelType, types, args);

        if (properties != null) {
            applyProperties(model, properties);
        }
        return model;
    }
}
