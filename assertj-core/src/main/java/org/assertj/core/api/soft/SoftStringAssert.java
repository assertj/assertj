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


}
