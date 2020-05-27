package com.github.ingogriebsch.spring.hateoas.siren;

import static java.util.Collections.singletonMap;

import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

class CollectionModelFactoryTest {

    @Test
    void create_should_throw_exception_if_input_is_null() {
        CollectionModelFactory factory = new CollectionModelFactory() {
        };

        assertThatThrownBy(() -> factory.create(null, null, null, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_should_throw_exception_if_type_is_not_matching() {
        CollectionModelFactory factory = new CollectionModelFactory() {
        };

        JavaType type = defaultInstance().constructSimpleType(String.class, null);
        assertThatThrownBy(() -> factory.create(type, newArrayList(), newArrayList(), null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_should_return_collection_model_containing_given_links() {
        CollectionModelFactory factory = new CollectionModelFactory() {
        };

        JavaType type = defaultInstance().constructParametricType(CollectionModel.class, String.class);
        Iterable<Link> links = newArrayList(new Link("href1"), new Link("href2"));
        CollectionModel<?> model = factory.create(type, links, newArrayList(), null);

        assertThat(model).isNotNull();
        assertThat(model.getLinks()).isNotNull();
        assertThat(model.getLinks().toList()).isEqualTo(links);
    }

    @Test
    void create_should_return_collection_model_having_given_properties() {
        CollectionModelFactory factory = new CollectionModelFactory() {
        };

        JavaType type = defaultInstance().constructSimpleType(Country.class, null);
        Map<String, Object> properties = singletonMap("name", "America");
        CollectionModel<?> model = factory.create(type, newArrayList(), newArrayList(), properties);

        assertThat(model).isNotNull().asInstanceOf(type(Country.class)).hasFieldOrPropertyWithValue("name", "America");
    }

    @Test
    void create_should_return_collection_model_not_having_properties_if_properties_not_given() {
        CollectionModelFactory factory = new CollectionModelFactory() {
        };

        JavaType type = defaultInstance().constructSimpleType(Country.class, null);
        CollectionModel<?> model = factory.create(type, newArrayList(), newArrayList(), null);

        assertThat(model).isNotNull().asInstanceOf(type(Country.class)).hasAllNullFieldsOrPropertiesExcept("content", "links");
    }
}