package org.assertj.core.api;

import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.util.Objects;

/**
 * Assertion that checks Object.equals() and Object.hashCode() contracts
 * <p>
 * Instances of this class should be created through
 * <pre><code class='java'>assertThat(actual).withEqualObjects(eq1, eq2)</code></pre>
 * @param <SELF> the "self" type (this class or a specific descendant)
 * @param <ACTUAL> the type of the "actual" value
 */
public class EqualsAndHashCodeAssert<SELF extends EqualsAndHashCodeAssert<SELF, ACTUAL>, ACTUAL>
    extends AbstractObjectAssert<EqualsAndHashCodeAssert<SELF, ACTUAL>, ACTUAL> {
  private final Object eq1;
  private final Object eq2;
  private Object[] notEqualObjectsHavingDifferentHashCodes = new Object[0];
  private Object[] notEqualObjectsAllowedToHaveEqualHashCode = new Object[0];

  EqualsAndHashCodeAssert(ACTUAL o, ACTUAL eq1, ACTUAL eq2) {
    super(o, EqualsAndHashCodeAssert.class);
    this.eq1 = eq1;
    this.eq2 = eq2;
  }

  /**
   * Specifies objects not equal to actual and having different hash codes.
   *
   * @param notEqualObjectsHavingDifferentHashCodes objects to test for inequality and hash code difference
   * @return this assertion
   */
  public EqualsAndHashCodeAssert<SELF, ACTUAL> withNotEqualObjectsHavingDifferentHashCodes(
      Object... notEqualObjectsHavingDifferentHashCodes) {
    Objects.requireNonNull(notEqualObjectsHavingDifferentHashCodes);
    this.notEqualObjectsHavingDifferentHashCodes = notEqualObjectsHavingDifferentHashCodes;
    return this;
  }

  /**
   * Specifies objects not equal to actual that may or may not have different hash codes.
   * @param notEqualObjectsAllowedToHaveEqualHashCode objects to test for inequality
   * @return this assertion
   */
  public EqualsAndHashCodeAssert<SELF, ACTUAL> withNotEqualObjectsAllowedToHaveEqualHashCode(
      Object... notEqualObjectsAllowedToHaveEqualHashCode) {
    Objects.requireNonNull(notEqualObjectsAllowedToHaveEqualHashCode);
    this.notEqualObjectsAllowedToHaveEqualHashCode = notEqualObjectsAllowedToHaveEqualHashCode;
    return this;
  }

  /**
   * Asserts that the actual object satisfies Object.equals() and Object.hashCode() contracts.
   * <p>
   * What is verified:
   * <ul>
   *     <li>Reflexivity. Fails if the actual value is not equal to itself.</li>
   *     <li>Symmetry. Fails if the actual value is not equal to {@code eq1} or {@code eq2} passed to
   *     {@link AbstractObjectAssert#withEqualObjects(Object, Object) withEqualObjects} or vice versa.</li>
   *     <li>Transitivity. Fails if {@code eq1} is not equal to {@code eq2} or vice versa.</li>
   *     <li>Inequality to {@code null}.</li>
   *     <li>Inequality to {@code new Object()}.</li>
   *     <li>Equality of the hash codes of equal objects.</li>
   *     <li>Inequality of the hash codes of unequal objects
   *     (it's up to the client to provide unequal objects with actually different hash codes).</li>
   * </ul>
   * <p>
   * Only the consistency part of the contracts is not verified because
   * there is no reliable way to test it, and it is rather unlikely to be broken
   * under normal circumstances, and circumstances when it can be broken
   * obviously need special test code anyway.
   */
  public void satisfiesEqualsAndHashCodeContracts() {
    if (notEqualObjectsHavingDifferentHashCodes.length == 0 && notEqualObjectsAllowedToHaveEqualHashCode.length == 0)
      throw new IllegalStateException("withNotEqualObjectsHavingDifferentHashCodes()" +
        " or withNotEqualObjectsAllowedToHaveEqualHashCode() (or both)" +
        " should have been called first");
    isNotNull();
    assertReflexive();
    assertSymmetric();
    assertTransitive();
    assertNotEqualsNull();
    assertNotEqualsAnyObject();
    assertEqualObjectsHaveSameHashCode();
    assertNotEqualObjectsHaveDifferentHashCodes();
  }

  private void assertReflexive() {
    if (!actual.equals(actual))
      fail("!actual.equals(actual)");
  }

  private void assertSymmetric() {
    if (!actual.equals(eq1))
      fail("!actual.equals(eq1)");
    if (!eq1.equals(actual))
      fail("!eq1.equals(actual)");
    if (!actual.equals(eq2))
      fail("!actual.equals(eq2)");
    if (!eq2.equals(actual))
      fail("!eq2.equals(actual)");
    Arrays.stream(notEqualObjectsHavingDifferentHashCodes)
      .forEach(this::assertSymmetricallyNotEqual);
    Arrays.stream(notEqualObjectsAllowedToHaveEqualHashCode)
      .forEach(this::assertSymmetricallyNotEqual);
  }

  private void assertSymmetricallyNotEqual(Object notEqual) {
    if (actual.equals(notEqual)) {
      fail("actual.equals(" + notEqual + ")");
    }
    if (notEqual.equals(actual)) {
      fail("(" + notEqual + ").equals(actual)");
    }
  }

  private void assertTransitive() {
    if (!eq1.equals(eq2))
      fail("!eq1.equals(eq2)");
    if (!eq2.equals(eq1))
      fail("!eq2.equals(eq1)");
  }

  private void assertNotEqualsNull() {
    if (actual.equals(null))
      fail("actual.equals(null)");
  }

  private void assertNotEqualsAnyObject() {
    if (actual.equals(new Object()))
      fail("actual.equals(new Object())");
  }

  private void assertEqualObjectsHaveSameHashCode() {
    if (actual.hashCode() != eq1.hashCode())
      fail("actual.hashCode() == " + actual.hashCode()
            + " != " + eq1.hashCode() + " == eq1.hashCode()");
    if (actual.hashCode() != eq2.hashCode())
      fail("actual.hashCode() == " + actual.hashCode()
        + " != " + eq2.hashCode() + " == eq2.hashCode()");
  }

  private void assertNotEqualObjectsHaveDifferentHashCodes() {
    int actualHashCode = actual.hashCode();
    for (Object notEqual : notEqualObjectsHavingDifferentHashCodes) {
      if (actualHashCode == notEqual.hashCode()) {
        fail("actual.hashCode() == " + actualHashCode + " == (" + notEqual + ").hashCode()");
      }
    }
  }
}
