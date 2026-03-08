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

import java.util.function.Supplier;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AssertionErrorCollector;

public final class SoftStringAssert implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final AbstractStringAssert<?> stringAssert;

  public SoftStringAssert(String actual, AssertionErrorCollector errorCollector) {
    this.errorCollector = errorCollector;
    this.stringAssert = assertThat(actual);
  }

  public String actual() {
    return stringAssert.actual();
  }

  public SoftStringAssert as(String description, Object[] args) {
    stringAssert.as(description, args);
    return this;
  }

  public SoftStringAssert as(Supplier<String> descriptionSupplier) {
    stringAssert.as(descriptionSupplier);
    return this;
  }

  public SoftStringAssert isEmpty() {
    try {
      stringAssert.isEmpty();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftStringAssert containsIgnoringCase(String text) {
    try {
      stringAssert.containsIgnoringCase(text);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

}
