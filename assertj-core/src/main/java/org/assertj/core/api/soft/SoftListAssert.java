/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.soft;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Supplier;

import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.ListAssert;

public final class SoftListAssert<ELEMENT> implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final ListAssert<ELEMENT> listAssert;

  public SoftListAssert(List<ELEMENT> actual, AssertionErrorCollector errorCollector) {
    this.errorCollector = errorCollector;
    this.listAssert = assertThat(actual);
  }

  public List<? extends ELEMENT> actual() {
    return listAssert.actual();
  }

  public SoftListAssert<ELEMENT> as(String description, Object[] args) {
    listAssert.as(description, args);
    return this;
  }

  public SoftListAssert<ELEMENT> as(Supplier<String> descriptionSupplier) {
    listAssert.as(descriptionSupplier);
    return this;
  }

  public SoftListAssert<ELEMENT> isEmpty() {
    try {
      listAssert.isEmpty();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftListAssert<ELEMENT> contains(ELEMENT... values) {
    try {
      listAssert.contains(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

}
