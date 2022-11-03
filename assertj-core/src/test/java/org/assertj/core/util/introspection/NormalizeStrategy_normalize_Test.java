package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NormalizeStrategy_normalize_Test {

  @ParameterizedTest
  @CsvSource({
          "duration,   duration",
          "firstName,  firstName",
          "_age,       age",
          "title_,     title",
          "_author_,   author",
          "address1,   address1",
          "address_2,  address2",
          "_address_3, address3",
          "profileURL, profileUrl",
  })
  void normalize(String input, String expected) {
    assertThat(NormalizeStrategy.normalize(input)).isEqualTo(expected);
  }
}