/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.introspection.CaseFormatUtils.toCamelCase;

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
      "one_more_makes_it_five,oneMoreMakesItFive",
      "one-more-makes-it-five,oneMoreMakesItFive",
      "six words can you believe it?,sixWordsCanYouBelieveIt?",
      "andNow for-something_CompletelyDifferent,andNowForSomethingCompletelyDifferent",
      "WHAT_ABOUT_ALL_CAPS,whatAboutAllCaps"
  })
  void should_convert_string_to_camel_case(String input, String expectedCamelCaseOutput) {
    // WHEN
    String result = toCamelCase(input);
    // THEN
    then(result).isEqualTo(expectedCamelCaseOutput);
  }
}
