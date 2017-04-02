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
package org.assertj.core.internal;

import static org.assertj.core.error.ClassModifierShouldBe.shouldBeFinal;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBeProtected;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePublic;
import static org.assertj.core.error.ClassModifierShouldBe.shouldNotBeFinal;
import static org.assertj.core.error.ShouldBeAnnotation.shouldBeAnnotation;
import static org.assertj.core.error.ShouldBeAnnotation.shouldNotBeAnnotation;
import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;
import static org.assertj.core.error.ShouldBeInterface.shouldBeInterface;
import static org.assertj.core.error.ShouldBeInterface.shouldNotBeInterface;
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveFields;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoDeclaredFields;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoPublicFields;
import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveDeclaredFields;
import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveFields;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.Arrays;

/**
 * Reusable assertions for <code>{@link Class}</code>s.
 * 
 * @author William Delanoue
 */
public class Classes {

  private static final Classes INSTANCE = new Classes();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Classes instance() {
    return INSTANCE;
  }

  private Failures failures = Failures.instance();
  private ComparisonStrategy comparisonStrategy = StandardComparisonStrategy.instance();

  /**
   * Verifies that the actual {@code Class} is assignable from all the {@code others} classes.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param others the others {@code Class} who this actual class must be assignable.
   * @throws NullPointerException if one of the {@code others} is {@code null}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not assignable from all of the {@code others} classes.
   */
  public void assertIsAssignableFrom(AssertionInfo info, Class<?> actual, Class<?>... others) {
    assertNotNull(info, actual);
    checkArgument(!Arrays.isNullOrEmpty(others), "Expecting at least one Class to be specified");

    Set<Class<?>> expected = newLinkedHashSet(others);
    Set<Class<?>> missing = new LinkedHashSet<>();
    for (Class<?> other : expected) {
      classParameterIsNotNull(other);
      if (!actual.isAssignableFrom(other)) missing.add(other);
    }

    if (!missing.isEmpty()) throw failures.failure(info, shouldBeAssignableFrom(actual, expected, missing));
  }

  /**
   * Verifies that the actual {@code Class} is not an interface.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is an interface.
   */
  public void assertIsNotInterface(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (actual.isInterface()) throw failures.failure(info, shouldNotBeInterface(actual));
  }

  /**
   * Verifies that the actual {@code Class} is an interface.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an interface.
   */
  public void assertIsInterface(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (!actual.isInterface()) throw failures.failure(info, shouldBeInterface(actual));
  }

  /**
   * Verifies that the actual {@code Class} is not an annotation.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is an annotation.
   */
  public void assertIsNotAnnotation(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (actual.isAnnotation()) throw failures.failure(info, shouldNotBeAnnotation(actual));
  }

  /**
   * Verifies that the actual {@code Class} is an annotation.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not an annotation.
   */
  public void assertIsAnnotation(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (!actual.isAnnotation()) throw failures.failure(info, shouldBeAnnotation(actual));
  }

  /**
   * Verifies that the actual {@code Class} is final.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not final.
   */
  public void assertIsFinal(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (!Modifier.isFinal(actual.getModifiers())) throw failures.failure(info, shouldBeFinal(actual));
  }

  /**
   * Verifies that the actual {@code Class} is public.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not public.
   */
  public void assertIsPublic(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (!Modifier.isPublic(actual.getModifiers())) {
      throw failures.failure(info, shouldBePublic(actual));
    }
  }

  /**
   * Verifies that the actual {@code Class} is protected.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is not protected.
   */
  public void assertIsProtected(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (!Modifier.isProtected(actual.getModifiers())) {
      throw failures.failure(info, shouldBeProtected(actual));
    }
  }

  /**
   * Verifies that the actual {@code Class} is not final.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} is final.
   */
  public void assertIsNotFinal(AssertionInfo info, Class<?> actual) {
    assertNotNull(info, actual);
    if (Modifier.isFinal(actual.getModifiers())) throw failures.failure(info, shouldNotBeFinal(actual));
  }

  /**
   * Verifies that the actual {@code Class} contains the given {@code Annotation}s.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param annotations annotations who must be attached to the class
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  public void assertContainsAnnotations(AssertionInfo info, Class<?> actual,
                                        @SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
    assertNotNull(info, actual);
    Set<Class<? extends Annotation>> expected = newLinkedHashSet(annotations);
    Set<Class<? extends Annotation>> missing = new LinkedHashSet<>();
    for (Class<? extends Annotation> other : expected) {
      classParameterIsNotNull(other);
      if (actual.getAnnotation(other) == null) missing.add(other);
    }

    if (!missing.isEmpty()) throw failures.failure(info, shouldHaveAnnotations(actual, expected, missing));
  }

  /**
   * Verifies that the actual {@code Class} has the {@code fields}.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param fields the fields who must be present in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   */
  public void assertHasPublicFields(AssertionInfo info, Class<?> actual, String... fields) {
    assertNotNull(info, actual);
    Set<String> expectedFieldNames = newLinkedHashSet(fields);
    Set<String> missingFieldNames = newLinkedHashSet();
    Set<String> actualFieldNames = fieldsToName(filterSyntheticMembers(actual.getFields()));
    if (expectedFieldNames.isEmpty()) {
      if (actualFieldNames.isEmpty()) return;
      throw failures.failure(info, shouldHaveNoPublicFields(actual, actualFieldNames));
    }
    if (noMissingElement(actualFieldNames, expectedFieldNames, missingFieldNames)) return;
    throw failures.failure(info, shouldHaveFields(actual, expectedFieldNames, missingFieldNames));
  }

  /**
   * Verifies that the actual {@code Class} has only the {@code fields} and nothing more. <b>in any order</b>.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param expectedFields all the fields that are expected to be in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if fields are not all the fields of the actual {@code Class}.
   */
  public void assertHasOnlyPublicFields(AssertionInfo info, Class<?> actual, String... expectedFields) {
    assertNotNull(info, actual);
    Set<String> actualFieldNames = fieldsToName(filterSyntheticMembers(actual.getFields()));
    List<String> notExpected = newArrayList(actualFieldNames);
    List<String> notFound = newArrayList(expectedFields);
    if (expectedFields.length == 0) {
      if (actualFieldNames.isEmpty()) return;
      throw failures.failure(info, shouldHaveNoPublicFields(actual, actualFieldNames));
    }

    for (String field : expectedFields) {
      if (comparisonStrategy.iterableContains(notExpected, field)) {
        comparisonStrategy.iterablesRemoveFirst(notExpected, field);
        comparisonStrategy.iterablesRemoveFirst(notFound, field);
      }
    }

    if (notExpected.isEmpty() && notFound.isEmpty()) return;
    throw failures.failure(info, shouldOnlyHaveFields(actual, newArrayList(expectedFields), notFound, notExpected));
  }

  /**
   * Checks that the {@code expectedNames} are part of the {@code actualNames}. If an {@code expectedName} is not
   * contained in the {@code actualNames}, the this method will return {@code true}. THe {@code missingNames} will
   * contain all the {@code expectedNames} that are not part of the {@code actualNames}.
   *
   * @param actualNames the names that should be used to check
   * @param expectedNames the names that should be contained in {@code actualNames}
   * @param missingNames the names that were not part of {@code expectedNames}
   * @return {@code true} if all {@code expectedNames} are part of the {@code actualNames}, {@code false} otherwise
   */
  private static boolean noMissingElement(Set<String> actualNames, Set<String> expectedNames,
                                          Set<String> missingNames) {
    for (String field : expectedNames) {
      if (!actualNames.contains(field)) missingNames.add(field);
    }
    return missingNames.isEmpty();
  }

  /**
   * Verifies that the actual {@code Class} has the declared {@code fields}.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param fields the fields who must be declared in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the field.
   */
  public void assertHasDeclaredFields(AssertionInfo info, Class<?> actual, String... fields) {
    assertNotNull(info, actual);
    Set<String> expectedFieldNames = newLinkedHashSet(fields);
    Set<String> missingFieldNames = newLinkedHashSet();
    Set<String> actualFieldNames = fieldsToName(filterSyntheticMembers(actual.getDeclaredFields()));
    if (expectedFieldNames.isEmpty()) {
      if (actualFieldNames.isEmpty()) return;
      throw failures.failure(info, shouldHaveNoDeclaredFields(actual, actualFieldNames));
    }
    if (noMissingElement(actualFieldNames, expectedFieldNames, missingFieldNames)) return;
    throw failures.failure(info, shouldHaveDeclaredFields(actual, expectedFieldNames, missingFieldNames));
  }

  /**
   * Verifies that the actual {@code Class} has the exactly the {@code fields} and nothing more. <b>in any order</b>.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param expectedFields all the fields that are expected to be in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if fields are not all the fields of the actual {@code Class}.
   */
  public void assertHasOnlyDeclaredFields(AssertionInfo info, Class<?> actual, String... expectedFields) {
    assertNotNull(info, actual);
    Set<String> actualFieldNames = fieldsToName(filterSyntheticMembers(actual.getDeclaredFields()));
    List<String> notExpected = newArrayList(actualFieldNames);
    List<String> notFound = newArrayList(expectedFields);

    if (expectedFields.length == 0) {
      if (actualFieldNames.isEmpty()) return;
      throw failures.failure(info, shouldHaveNoDeclaredFields(actual, actualFieldNames));
    }

    for (String field : expectedFields) {
      if (comparisonStrategy.iterableContains(notExpected, field)) {
        comparisonStrategy.iterablesRemoveFirst(notExpected, field);
        comparisonStrategy.iterablesRemoveFirst(notFound, field);
      }
    }

    if (notExpected.isEmpty() && notFound.isEmpty()) return;
    throw failures.failure(info,
                           shouldOnlyHaveDeclaredFields(actual, newArrayList(expectedFields), notFound, notExpected));
  }

  private static Set<String> fieldsToName(Set<Field> fields) {
    Set<String> fieldsName = new LinkedHashSet<>();
    for (Field field : fields) {
      fieldsName.add(field.getName());
    }
    return fieldsName;
  }

  /**
   * Verifies that the actual {@code Class} has the {@code methods}.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param methods the methods who must be present in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the methods.
   */
  public void assertHasMethods(AssertionInfo info, Class<?> actual, String... methods) {
    assertNotNull(info, actual);
    doAssertHasMethods(info, actual, filterSyntheticMembers(getAllMethods(actual)), false, methods);
  }

  /**
   * Verifies that the actual {@code Class} has the declared {@code methods}.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param methods the methods who must be declared in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the methods.
   */
  public void assertHasDeclaredMethods(AssertionInfo info, Class<?> actual, String... methods) {
    assertNotNull(info, actual);
    doAssertHasMethods(info, actual, filterSyntheticMembers(actual.getDeclaredMethods()), true, methods);
  }

  private void doAssertHasMethods(AssertionInfo info, Class<?> actual, Set<Method> actualMethods, boolean declared,
                                  String... expectedMethods) {
    SortedSet<String> expectedMethodNames = newTreeSet(expectedMethods);
    SortedSet<String> missingMethodNames = newTreeSet();
    SortedSet<String> actualMethodNames = methodsToName(actualMethods);

    if (expectedMethods.length == 0) {
      if (actualMethods.isEmpty()) return;
      throw failures.failure(info, shouldNotHaveMethods(actual, declared, getMethodsWithModifier(actualMethods,
                                                                                                 Modifier.methodModifiers())));
    }

    if (!noMissingElement(actualMethodNames, expectedMethodNames, missingMethodNames)) {
      throw failures.failure(info, shouldHaveMethods(actual, declared, expectedMethodNames, missingMethodNames));
    }
  }

  /**
   * Verifies that the actual {@code Class} has the public {@code methods}.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param methods the public methods who must be present in the class.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of the public methods.
   */
  public void assertHasPublicMethods(AssertionInfo info, Class<?> actual, String... methods) {
    assertNotNull(info, actual);
    Method[] actualMethods = actual.getMethods();
    SortedSet<String> expectedMethodNames = newTreeSet(methods);
    SortedSet<String> missingMethodNames = newTreeSet();
    Map<String, Integer> methodNamesWithModifier = methodsToNameAndModifier(actualMethods);

    if (methods.length == 0 && hasPublicMethods(actualMethods)) {
      throw failures.failure(info,
                             shouldNotHaveMethods(actual, Modifier.toString(Modifier.PUBLIC), false,
                                                  getMethodsWithModifier(newLinkedHashSet(actualMethods),
                                                                         Modifier.PUBLIC)));
    }

    if (!noMissingElement(methodNamesWithModifier.keySet(), expectedMethodNames, missingMethodNames)) {
      throw failures.failure(info, shouldHaveMethods(actual, false, expectedMethodNames, missingMethodNames));
    }
    Map<String, String> nonMatchingModifiers = new LinkedHashMap<>();
    if (!noNonMatchingModifier(expectedMethodNames, methodNamesWithModifier, nonMatchingModifiers, Modifier.PUBLIC)) {
      throw failures.failure(info, shouldHaveMethods(actual, false, expectedMethodNames,
                                                     Modifier.toString(Modifier.PUBLIC), nonMatchingModifiers));
    }
  }

  private static SortedSet<String> getMethodsWithModifier(Set<Method> methods, int modifier) {
    SortedSet<String> methodsWithModifier = newTreeSet();
    for (Method method : methods) {
      if ((method.getModifiers() & modifier) != 0) {
        methodsWithModifier.add(method.getName());
      }
    }
    return methodsWithModifier;
  }

  private static boolean noNonMatchingModifier(Set<String> expectedMethodNames, Map<String, Integer> methodsModifier,
                                               Map<String, String> nonMatchingModifiers, int modifier) {
    for (String method : methodsModifier.keySet()) {
      if (expectedMethodNames.contains(method) && (methodsModifier.get(method) & modifier) == 0) {
        nonMatchingModifiers.put(method, Modifier.toString(methodsModifier.get(method)));
      }
    }
    return nonMatchingModifiers.isEmpty();
  }

  private static boolean hasPublicMethods(Method[] methods) {
    for (Method method : methods) {
      if (Modifier.isPublic(method.getModifiers())) {
        return true;
      }
    }
    return false;
  }

  private static SortedSet<String> methodsToName(Set<Method> methods) {
    SortedSet<String> methodsName = newTreeSet();
    for (Method method : methods) {
      methodsName.add(method.getName());
    }
    return methodsName;
  }

  private static Map<String, Integer> methodsToNameAndModifier(Method[] methods) {
    Map<String, Integer> methodMap = new LinkedHashMap<>(methods.length);
    for (Method method : methods) {
      methodMap.put(method.getName(), method.getModifiers());
    }
    return methodMap;
  }

  private static Method[] getAllMethods(Class<?> actual) {
    Set<Method> allMethods = newLinkedHashSet();
    Method[] declaredMethods = actual.getDeclaredMethods();
    allMethods.addAll(newLinkedHashSet(declaredMethods));
    Class<?> superclass = actual.getSuperclass();
    if (superclass != null) {
      allMethods.addAll(newLinkedHashSet(getAllMethods(superclass)));
    }
    return allMethods.toArray(new Method[allMethods.size()]);
  }

  private static <M extends Member> Set<M> filterSyntheticMembers(M[] members) {
    Set<M> filteredMembers = newLinkedHashSet();
    for (M member : members) {
      if (!member.isSynthetic()) {
        filteredMembers.add(member);
      }
    }
    return filteredMembers;
  }

  private static void assertNotNull(AssertionInfo info, Class<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * used to check that the class to compare is not null, in that case throws a {@link NullPointerException} with an
   * explicit message.
   * 
   * @param clazz the date to check
   * @throws NullPointerException with an explicit message if the given class is null
   */
  private static void classParameterIsNotNull(Class<?> clazz) {
    checkNotNull(clazz, "The class to compare actual with should not be null");
  }
}
