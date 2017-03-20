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
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ShouldHaveExactlyFields.shouldHaveExactlyFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code
 * >{@link org.assertj.core.internal.Classes#assertHasExactlyFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 *
 * @author Filip Hrisafov
 */
public class Classes_assertHasExactlyFields_Test extends ClassesBaseTest {

  @Before
  public void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasExactlyFields(someInfo(), actual);
  }

  @Test
  public void should_fail_if_no_fields_are_expected() {
    thrown.expectAssertionError(shouldHaveExactlyFields(actual,
                                                        newLinkedHashSet("publicField", "public2Field"),
                                                        Sets.<String>newLinkedHashSet(),
                                                        newLinkedHashSet("publicField", "public2Field")));
    classes.assertHasExactlyFields(someInfo(), actual);
  }

  @Test
  public void should_fail_if_not_all_public_fields_are_expected() {
    thrown.expectAssertionError(shouldHaveExactlyFields(actual,
                                                        newLinkedHashSet("publicField", "public2Field"),
                                                        Sets.<String>newLinkedHashSet(),
                                                        newLinkedHashSet("public2Field")));
    classes.assertHasExactlyFields(someInfo(), actual, "publicField");
  }

  @Test
  public void should_fail_if_public_fields_are_missing() {
    String[] expected = new String[] { "missingField", "publicField", "public2Field" };
    thrown.expectAssertionError(shouldHaveExactlyFields(actual,
                                                        newLinkedHashSet("publicField", "public2Field"),
                                                        newLinkedHashSet("missingField"),
                                                        Sets.<String>newLinkedHashSet()));
    classes.assertHasExactlyFields(someInfo(), actual, expected);
  }

  @Test
  public void should_pass_if_public_all_fields_are_expected() {
    String[] expected = new String[] { "publicField", "public2Field" };
    classes.assertHasExactlyFields(someInfo(), actual, expected);
  }

  @Test
  public void should_pass_if_public_all_fields_are_reversed_expected() {
    String[] expected = new String[] { "public2Field", "publicField" };
    classes.assertHasExactlyFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_fields_are_protected_or_private() {
    String[] expected = new String[] { "publicField", "public2Field", "protectedField", "privateField" };
    thrown.expectAssertionError(shouldHaveExactlyFields(actual,
                                                        newLinkedHashSet("publicField", "public2Field"),
                                                        newLinkedHashSet("protectedField", "privateField"),
                                                        Sets.<String>newLinkedHashSet()));
    classes.assertHasExactlyFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_fields_are_not_found_and_not_expected() {
    String[] expected = new String[] { "publicField", "protectedField", "privateField" };
    thrown.expectAssertionError(shouldHaveExactlyFields(actual,
                                                        newLinkedHashSet("publicField", "public2Field"),
                                                        newLinkedHashSet("protectedField", "privateField"),
                                                        newLinkedHashSet("public2Field")));
    classes.assertHasExactlyFields(someInfo(), actual, expected);
  }
}
