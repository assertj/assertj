/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.recursive.assertion;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.internal.Objects.getDeclaredFieldsIncludingInherited;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.EXTRACTION;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class RecursiveAssertionAssert_withIntrospectionStrategy_Test {

  @Test
  void should_honor_introspection_strategy() {
    // GIVEN
    Object testObject = objectGraphWithNullDateValue();
    RecursiveAssertionIntrospectionStrategy introspectionStrategyIgnoringDates = new IntrospectionStrategyIgnoringDates();
    // WHEN/THEN
    thenNoException().isThrownBy(() -> assertThat(testObject).usingRecursiveAssertion()
                                                             .withIntrospectionStrategy(introspectionStrategyIgnoringDates)
                                                             .hasNoNullFields());
  }

  static class IntrospectionStrategyIgnoringDates implements RecursiveAssertionIntrospectionStrategy {

    @Override
    public List<RecursiveAssertionNode> getChildNodesOf(Object object) {
      return getDeclaredFieldsIncludingInherited(object.getClass()).stream()
                                                                   .map(field -> toNode(field, object))
                                                                   // ignore date!
                                                                   .filter(node -> !node.type.equals(Date.class))
                                                                   .collect(toList());
    }

    private static RecursiveAssertionNode toNode(Field field, Object node) {
      String fieldName = field.getName();
      Object fieldValue = EXTRACTION.getSimpleValue(fieldName, node);
      Class<?> fieldType = getFieldType(fieldValue, fieldName, node);
      return new RecursiveAssertionNode(fieldValue, fieldName, fieldType);
    }

    private static Class<?> getFieldType(Object fieldValue, String fieldName, Object targetObject) {
      return fieldValue != null ? fieldValue.getClass() : getFieldType(fieldName, targetObject.getClass());
    }

    private static Class<?> getFieldType(String fieldName, Class<?> objectClass) {
      try {
        Optional<Field> potentialField = stream(objectClass.getDeclaredFields()).filter(field -> fieldName.equals(field.getName()))
                                                                                .findAny();
        if (potentialField.isPresent()) return potentialField.get().getType();
        Class<?> superclass = objectClass.getSuperclass();
        if (superclass != null) return getFieldType(fieldName, superclass);
        throw new NoSuchFieldException();
      } catch (NoSuchFieldException | SecurityException e) {
        throw new IllegalStateException(format("Could not find field %s on class %s, even though its name was retrieved from the class earlier",
                                               fieldName, objectClass.getCanonicalName()),
                                        e);
      }
    }

    @Override
    public String getDescription() {
      return "introspection ignoring Date fields";
    }
  }

  Object objectGraphWithNullDateValue() {
    Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
    Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
    Author kentBeck = new Author("Kent Beck", "k.beck@recursive.test");

    Book noSqlDistilled = new Book("NoSql Distilled", array(pramodSadalage, martinFowler));
    pramodSadalage.books.add(noSqlDistilled);
    martinFowler.books.add(noSqlDistilled);

    Book refactoring = new Book("Refactoring", array(martinFowler, kentBeck));
    martinFowler.books.add(refactoring);
    kentBeck.books.add(refactoring);

    return pramodSadalage;
  }

  class Author {
    String name;
    String email;
    List<Book> books = new ArrayList<>();

    Author(String name, String email) {
      this.name = name;
      this.email = email;
    }
  }

  class Book {
    String title;
    Date publicationDate = null;
    Author[] authors;

    Book(String title, Author[] authors) {
      this.title = title;
      this.authors = authors;
    }
  }

}
