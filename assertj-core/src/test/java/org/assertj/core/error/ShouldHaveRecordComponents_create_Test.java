package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveRecordComponents;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Sets.set;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveRecordComponents_create_Test {

  @Test
  void should_create_error_message() {
    // WHEN
    String message = shouldHaveRecordComponents(Object.class, set("component1", "component2"), set("component2")).create(new TestDescription("TEST"),
                                                                                                      STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.Object%n" +
                                   "to have the following record components:\n" +
                                   "  [\"component1\", \"component2\"]\n" +
                                   "but it doesn't have:\n" +
                                   "  [\"component2\"]"));
  }

}
