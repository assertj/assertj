package org.assertj.core.presentation;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.BinaryRepresentation.BINARY_REPRESENTATION;
import static org.assertj.core.presentation.HexadecimalRepresentation.HEXA_REPRESENTATION;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;

public class CompositeRepresentation_Test extends AbstractBaseRepresentationTest {
  @Test
  void should_return_toString_of_default_representation() {
    CompositeRepresentation composite = new CompositeRepresentation();
    Object longNumber = 123L;
    assertThat(composite.toStringOf(longNumber)).isEqualTo("123L");
  }

  @Test
  void should_return_toString_of_specific_representation() {
    CompositeRepresentation composite = new CompositeRepresentation();
    Object longNumber = 123L;
    composite.addRepresentation("hex", HEXA_REPRESENTATION);
    assertThat(composite.toStringOf(longNumber, "hex")).isEqualTo("0x0000_0000_0000_007B");
    assertThat(composite.toStringOf(longNumber, "standard")).isNull();
  }

  @Test
  void should_return_null() {
    CompositeRepresentation composite = new CompositeRepresentation();
    Object nullObject = null;
    assertThat(composite.toStringOf(nullObject)).isNull();
  }


  @Test
  void should_represent_differently_according_to_the_representation() {
    Map<String, Representation> representations = new HashMap<>();
    representations.put("binary", BINARY_REPRESENTATION);
    representations.put("unicode", UNICODE_REPRESENTATION);
    CompositeRepresentation composite = new CompositeRepresentation(representations);
    Object string = "你好！";
    Object longNumber = 123L;
    assertThat(composite.toStringOf(longNumber)).isEqualTo("0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_01111011");
    assertThat(composite.toStringOf(string, "unicode")).isEqualTo("\\u4f60\\u597d\\uff01");
  }

  @Test
  void should_be_able_to_add_or_delete_representations() {
    Map<String, Representation> representations = new HashMap<>();
    representations.put("binary", BINARY_REPRESENTATION);
    representations.put("unicode", UNICODE_REPRESENTATION);
    CompositeRepresentation composite = new CompositeRepresentation(representations);
    Object longNumber = 123L;
    composite.addRepresentation("hex", HEXA_REPRESENTATION);
    assertThat(composite.getRepresentations().size()).isEqualTo(3);
    composite.deleteRepresentation("binary");
    assertThat(composite.toStringOf(longNumber, "binary")).isNull();
  }
}
