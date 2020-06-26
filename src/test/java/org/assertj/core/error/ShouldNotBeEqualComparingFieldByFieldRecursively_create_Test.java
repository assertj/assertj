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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldNotBeEqualComparingFieldByFieldRecursively.shouldNotBeEqualComparingFieldByFieldRecursively;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.Test;

public class ShouldNotBeEqualComparingFieldByFieldRecursively_create_Test {
  private static final TextDescription TEST_DESCRIPTION = new TextDescription("Test");
  private static final Representation REPRESENTATION = CONFIGURATION_PROVIDER.representation();

  @Test
  public void should_show_error_message() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    String message = shouldNotBeEqualComparingFieldByFieldRecursively("Yoda", "Luke",
                                                                      recursiveComparisonConfiguration,
                                                                      REPRESENTATION).create(TEST_DESCRIPTION, REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting:%n" +
                                   "  <\"Yoda\">%n" +
                                   "not to be equal to:%n" +
                                   "  <\"Luke\">%n" +
                                   "when recursively comparing field by field" +
                                   "%n" +
                                   "The recursive comparison was performed with this configuration:%n%s",
                                   CONFIGURATION_PROVIDER.representation().toStringOf(recursiveComparisonConfiguration)));

  }

  @Test
  public void should_show_error_message_with_custom_comparison_configuration() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(String.class);
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    String message = shouldNotBeEqualComparingFieldByFieldRecursively("Yoda", "Luke",
                                                                      recursiveComparisonConfiguration,
                                                                      REPRESENTATION).create(TEST_DESCRIPTION, REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting:%n" +
                                   "  <\"Yoda\">%n" +
                                   "not to be equal to:%n" +
                                   "  <\"Luke\">%n" +
                                   "when recursively comparing field by field" +
                                   "%n" +
                                   "The recursive comparison was performed with this configuration:%n%s",
                                   CONFIGURATION_PROVIDER.representation().toStringOf(recursiveComparisonConfiguration)));
  }

  @Test
  public void should_show_two_null_object_error_message() {
    // GIVEN
    String message = shouldNotBeEqualComparingFieldByFieldRecursively(null).create(TEST_DESCRIPTION, REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual not to be equal to other but both are null."));
  }

  @Test
  public void should_show_same_object_error_message() {
    // GIVEN
    String message = shouldNotBeEqualComparingFieldByFieldRecursively("Luke").create(TEST_DESCRIPTION, REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual not to be equal to other but both refer to the same object (actual == other):%n"
                                   +
                                   "  <\"Luke\">%n"));
  }
}
