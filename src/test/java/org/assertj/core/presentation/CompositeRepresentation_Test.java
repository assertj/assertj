package org.assertj.core.presentation;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompositeRepresentation_Test extends AbstractBaseRepresentationTest {
  @Test
  void should_return_toString_of_Standard_representation() {
    // GIVEN
    Map<String, Representation> representations = new HashMap<>();
    representations.put("std", new StandardRepresentation());
    representations.put("hex", new HexadecimalRepresentation());
    CompositeRepresentation composite = new CompositeRepresentation(representations);
    // WHEN
    Object longNumber = 123L;
    // THEN
    then(composite.toStringOf(longNumber)).isEqualTo("123L");
  }

  @Test
  void should_return_null() {
    // GIVEN
    Map<String, Representation> representations = new HashMap<>();
    CompositeRepresentation composite = new CompositeRepresentation(representations);
    // WHEN
    Object nullObject = null;
    // THEN
    then(composite.toStringOf(nullObject)).isNull();
  }

  @Test
  void should_throw_IllegalArgumentException() {
    // GIVEN
    Map<String, Representation> representations = null;
    // WHEN and THEN
    assertThrows(IllegalArgumentException.class, () -> {
      new CompositeRepresentation(null);
    });
  }
}
