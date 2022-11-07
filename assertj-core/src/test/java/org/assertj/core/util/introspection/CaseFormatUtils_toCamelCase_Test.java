package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CaseFormatUtils_toCamelCase_Test {

  @ParameterizedTest
  @CsvSource({
      "'',''",
      "word,word",
      "WORD,word",
      "wOrd,wOrd",
      "_word,word",
      "word_,word",
      "two_words,twoWords",
      "threeWordsNow,threeWordsNow",
      "AndEvenFourWords,andEvenFourWords",
      "one-more-makes-it-five,oneMoreMakesItFive",
      "six words can you believe it?,sixWordsCanYouBelieveIt?",
      "andNow for-something_CompletelyDifferent,andNowForSomethingCompletelyDifferent",
      "WHAT_ABOUT_ALL_CAPS,whatAboutAllCaps"
  })
  void toCamelCase(String input, String expectedOutput) {
    assertThat(CaseFormatUtils.toCamelCase(input)).isEqualTo(expectedOutput);
  }
}