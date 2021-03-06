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
package de.ingogriebsch.spring.hateoas.siren;

import static de.ingogriebsch.spring.hateoas.siren.SirenActionFieldType.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SirenActionTest {

    @Nested
    class Builder {

        @Test
        void should_throw_exception_if_href_is_not_given() {
            assertThatThrownBy(() -> SirenAction.builder().name("name").build()).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void should_throw_exception_if_name_is_not_given() {
            assertThatThrownBy(() -> SirenAction.builder().href("/api").build()).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void should_have_http_method_even_if_not_defined_explicitely() {
            SirenAction action = SirenAction.builder().name("name").href("/api").build();
            assertThat(action).isNotNull();
            assertThat(action.getMethod()).isEqualTo(GET);
        }

        @Test
        void should_return_instance_having_null_or_empty_or_default_references_if_not_defined_explicitely() {
            SirenAction action = SirenAction.builder().name("name").href("/api").build();
            assertThat(action.getClasses()).isNull();
            assertThat(action.getFields()).isEmpty();
            assertThat(action.getHref()).isNotNull();
            assertThat(action.getTitle()).isNull();
            assertThat(action.getType()).isNull();
        }
    }

    @Nested
    class TitleResolvable {

        @Test
        void should_return_codes_respecting_given_name() {
            SirenAction.TitleResolvable titleResolvable = SirenAction.TitleResolvable.of("name");
            assertThat(titleResolvable.getCodes()).containsExactly("_action.name.title", "_action.default.title");
        }
    }

    @Nested
    class Field {

        @Nested
        class Builder {

            @Test
            void should_throw_exception_if_name_is_not_given() {
                assertThatThrownBy(() -> SirenAction.Field.builder().build()).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void should_return_instance_having_null_or_empty_or_default_references_if_not_defined_explicitely() {
                SirenAction.Field field = SirenAction.Field.builder().name("name").build();
                assertThat(field.getClasses()).isNull();
                assertThat(field.getTitle()).isNull();
                assertThat(field.getType()).isEqualTo(TEXT.getKeyword());
                assertThat(field.getValue()).isNull();
            }
        }

        @Nested
        class TitleResolvable {

            @Test
            void should_return_codes_respecting_given_name() {
                SirenAction.Field.TitleResolvable titleResolvable = SirenAction.Field.TitleResolvable.of("name");
                assertThat(titleResolvable.getCodes()).containsExactly("_field.name.title", "_field.default.title");
            }
        }

    }

}
