package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeAnnotation.shouldBeAnnotation;
import static org.assertj.core.error.ShouldBeAnnotation.shouldNotBeAnnotation;
import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;
import static org.assertj.core.error.ShouldBeInterface.shouldBeInterface;
import static org.assertj.core.error.ShouldBeInterface.shouldNotBeInterface;
import static org.assertj.core.error.ShouldHaveAnnotation.shouldHaveAnnotation;
import static org.assertj.core.error.ShouldHaveField.shouldHaveDeclaredField;
import static org.assertj.core.error.ShouldHaveField.shouldHaveField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.Sets;
import org.assertj.core.util.VisibleForTesting;

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

  @VisibleForTesting
  Failures failures = Failures.instance();

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

    Set<Class<?>> expected = Sets.newLinkedHashSet(others);
    Set<Class<?>> missing = new LinkedHashSet<Class<?>>();
    for (Class<?> other : expected) {
      classParameterIsNotNull(other);
      if (!actual.isAssignableFrom(other)) {
        missing.add(other);
      }
    }

    if (missing.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldBeAssignableFrom(actual, expected, missing));
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

    if (!actual.isInterface()) {
      return;
    }
    throw failures.failure(info, shouldNotBeInterface(actual));
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

    if (actual.isInterface()) {
      return;
    }
    throw failures.failure(info, shouldBeInterface(actual));
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

    if (!actual.isAnnotation()) {
      return;
    }
    throw failures.failure(info, shouldNotBeAnnotation(actual));
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

    if (actual.isAnnotation()) {
      return;
    }
    throw failures.failure(info, shouldBeAnnotation(actual));
  }

  /**
   * Verifies that the actual {@code Class} contains {@code Annotation}.
   * 
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Class}.
   * @param annotations annotations who must be attached to the class
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Class} doesn't contains all of these annotations.
   */
  public void assertContainsAnnotation(AssertionInfo info, Class<?> actual, Class<? extends Annotation>... annotations) {
    assertNotNull(info, actual);
    Set<Class<? extends Annotation>> expected = Sets.newLinkedHashSet(annotations);
    Set<Class<? extends Annotation>> missing = new LinkedHashSet<Class<? extends Annotation>>();
    for (Class<? extends Annotation> other : expected) {
      classParameterIsNotNull(other);
      if (actual.getAnnotation(other) == null) {
        missing.add(other);
      }
    }

    if (missing.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldHaveAnnotation(actual, expected, missing));
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
  public void assertHasFields(AssertionInfo info, Class<?> actual, String... fields) {
    assertNotNull(info, actual);
    Set<String> expected = Sets.newLinkedHashSet(fields);
    Set<String> missing = new LinkedHashSet<String>();
    Set<String> fieldNames = fieldsToName(actual.getFields());
    for (String field : expected) {
      if (!fieldNames.contains(field)) {
        missing.add(field);
      }
    }

    if (missing.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldHaveField(actual, expected, missing));
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
    Set<String> expected = Sets.newLinkedHashSet(fields);
    Set<String> missing = new LinkedHashSet<String>();
    Set<String> fieldNames = fieldsToName(actual.getDeclaredFields());
    for (String field : expected) {
      if (!fieldNames.contains(field)) {
        missing.add(field);
      }
    }

    if (missing.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldHaveDeclaredField(actual, expected, missing));
  }

  private static Set<String> fieldsToName(Field[] fields) {
    Set<String> fieldsName = new LinkedHashSet<String>();
    for (Field field : fields) {
      fieldsName.add(field.getName());
    }
    return fieldsName;
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
    if (clazz == null)
      throw new NullPointerException("The class to compare actual with should not be null");
  }

}
