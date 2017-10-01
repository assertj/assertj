/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error.uri;

import java.util.List;
import java.util.Set;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveParameter extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_PARAMETER_BUT_WAS_MISSING = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nbut was missing";
  private static final String SHOULD_HAVE_PARAMETER_WITHOUT_VALUE_BUT_PARAMETER_WAS_MISSING = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith no value, but parameter was missing";
  private static final String SHOULD_HAVE_PARAMETER_WITH_VALUE_BUT_PARAMETER_WAS_MISSING = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut parameter was missing";

  private static final String SHOULD_HAVE_PARAMETER_WITHOUT_VALUE_BUT_HAD_VALUE = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith no value, but parameter had value:%n  <%s>";
  private static final String SHOULD_HAVE_PARAMETER_WITHOUT_VALUE_BUT_HAD_VALUES = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith no value, but parameter had values:%n  <%s>";
  private static final String SHOULD_HAVE_PARAMETER_WITH_VALUE_BUT_HAD_NO_VALUE = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut parameter had no value";

  private static final String SHOULD_HAVE_PARAMETER_VALUE_BUT_HAD_WRONG_VALUE = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut had value:%n  <%s>";
  private static final String SHOULD_HAVE_PARAMETER_VALUE_BUT_HAD_WRONG_VALUES = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut had values:%n  <%s>";

  private static final String SHOULD_HAVE_NO_PARAMETER_BUT_HAD_ONE_WITHOUT_VALUE = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nbut parameter was present with no value";
  private static final String SHOULD_HAVE_NO_PARAMETER_BUT_HAD_ONE_VALUE = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nbut parameter was present with value:%n  <%s>";
  private static final String SHOULD_HAVE_NO_PARAMETER_BUT_HAD_MULTIPLE_VALUES = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nbut parameter was present with values:%n  <%s>";

  private static final String SHOULD_HAVE_NO_PARAMETER_WITHOUT_VALUE_BUT_FOUND_ONE = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nwith no value, but did";
  private static final String SHOULD_HAVE_NO_PARAMETER_WITH_GIVEN_VALUE_BUT_FOUND_ONE = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nwith value:%n  <%s>%nbut did";

  private static final String SHOULD_HAVE_NO_PARAMETERS = "%nExpecting:%n  <%s>%nnot to have any parameters but found:%n  <%s>";

  public static ErrorMessageFactory shouldHaveParameter(Object actual, String name) {
    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_BUT_WAS_MISSING, actual, name);
  }

  public static ErrorMessageFactory shouldHaveParameter(Object actual, String name, String expectedValue) {
    if (expectedValue == null)
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_WITHOUT_VALUE_BUT_PARAMETER_WAS_MISSING, actual, name);
    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_WITH_VALUE_BUT_PARAMETER_WAS_MISSING, actual, name,
                                   expectedValue);
  }

  public static ErrorMessageFactory shouldHaveParameter(Object actual, String name, String expectedValue,
                                                        List<String> actualValues) {
    if (expectedValue == null)
      return new ShouldHaveParameter(multipleValues(actualValues) ? SHOULD_HAVE_PARAMETER_WITHOUT_VALUE_BUT_HAD_VALUES
          : SHOULD_HAVE_PARAMETER_WITHOUT_VALUE_BUT_HAD_VALUE, actual, name, valueDescription(actualValues));

    if (noValueIn(actualValues))
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_WITH_VALUE_BUT_HAD_NO_VALUE, actual, name, expectedValue);

    return new ShouldHaveParameter(multipleValues(actualValues) ? SHOULD_HAVE_PARAMETER_VALUE_BUT_HAD_WRONG_VALUES
        : SHOULD_HAVE_PARAMETER_VALUE_BUT_HAD_WRONG_VALUE, actual, name, expectedValue, valueDescription(actualValues));
  }

  public static ErrorMessageFactory shouldHaveNoParameters(Object actual, Set<String> parameterNames) {
    String parametersDescription = parameterNames.size() == 1 ? parameterNames.iterator().next()
        : parameterNames.toString();
    return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETERS, actual, parametersDescription);
  }

  public static ErrorMessageFactory shouldHaveNoParameter(Object actual, String name, List<String> actualValues) {
    return noValueIn(actualValues)
        ? new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_BUT_HAD_ONE_WITHOUT_VALUE, actual, name)
        : new ShouldHaveParameter(multipleValues(actualValues) ? SHOULD_HAVE_NO_PARAMETER_BUT_HAD_MULTIPLE_VALUES
            : SHOULD_HAVE_NO_PARAMETER_BUT_HAD_ONE_VALUE, actual, name, valueDescription(actualValues));
  }

  public static ErrorMessageFactory shouldHaveNoParameter(Object actual, String name, String unwantedValue,
                                                          List<String> actualValues) {
    if (noValueIn(actualValues))
      return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_WITHOUT_VALUE_BUT_FOUND_ONE, actual, name);

    return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_WITH_GIVEN_VALUE_BUT_FOUND_ONE, actual, name,
                                   unwantedValue);
  }

  private static boolean noValueIn(List<String> actualValues) {
    return actualValues == null || (actualValues.size() == 1 && actualValues.contains(null));
  }

  private static String valueDescription(List<String> actualValues) {
    return multipleValues(actualValues) ? actualValues.toString() : actualValues.get(0);
  }

  private static boolean multipleValues(List<String> values) {
    return values.size() > 1;
  }

  private ShouldHaveParameter(String format, Object... arguments) {
    super(format, arguments);
  }

}
