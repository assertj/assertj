package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ObjectAssert_satisfiesEqualsAndHashCodeContracts_Test {
  private static final int ACTUAL_HASH = 1;
  private static final int NOT_EQUAL_HASH = 2;
  private static final int YET_ANOTHER_HASH = 3;
  private StubbableEqualsAndHashCode actual;
  private StubbableEqualsAndHashCode eq1;
  private StubbableEqualsAndHashCode eq2;
  private StubbableEqualsAndHashCode notEqual;
  private StubbableEqualsAndHashCode notEqualSameHashCode;

  @BeforeEach
  void setUp() {
    actual = new StubbableEqualsAndHashCode("actual");
    eq1 = new StubbableEqualsAndHashCode("eq1");
    eq2 = new StubbableEqualsAndHashCode("eq2");
    notEqual = new StubbableEqualsAndHashCode("notEqual");
    notEqualSameHashCode = new StubbableEqualsAndHashCode("notEqualSameHash");
    actual.letBeEqualTo(actual, eq1, eq2);
    actual.letBeNotEqualTo(notEqual, notEqualSameHashCode);
    actual.letHashCodeBe(ACTUAL_HASH);
    eq1.letBeEqualTo(actual, eq1, eq2);
    eq1.letBeNotEqualTo(notEqual, notEqualSameHashCode);
    eq1.letHashCodeBe(ACTUAL_HASH);
    eq2.letBeEqualTo(actual, eq1, eq2);
    eq2.letBeNotEqualTo(notEqual, notEqualSameHashCode);
    eq2.letHashCodeBe(ACTUAL_HASH);
    notEqual.letBeEqualTo(notEqual);
    notEqual.letBeNotEqualTo(actual, eq1, eq2, notEqualSameHashCode);
    notEqual.letHashCodeBe(NOT_EQUAL_HASH);
    notEqualSameHashCode.letBeNotEqualTo(notEqualSameHashCode);
    notEqualSameHashCode.letBeNotEqualTo(actual, eq1, eq2, notEqual);
    notEqualSameHashCode.letHashCodeBe(ACTUAL_HASH);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatThrownBy(() ->
        assertThat((Object) null)
          .withEqualObjects(new Object(), new Object())
          .withNotEqualObjectsHavingDifferentHashCodes(new Object())
          .satisfiesEqualsAndHashCodeContracts())
      .isInstanceOf(AssertionError.class);
  }

  @Test
  void should_throw_if_withNotEqualObjects_was_not_called() {
    assertThatThrownBy(() ->
        assertThat(actual)
          .withEqualObjects(eq1, eq2)
          .satisfiesEqualsAndHashCodeContracts())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("withNotEqualObjectsHavingDifferentHashCodes");
  }

  @Test
  void should_pass_if_object_satisfies_contracts() {
    assertThat(actual)
      .withEqualObjects(eq1, eq2)
      .withNotEqualObjectsHavingDifferentHashCodes(notEqual)
      .satisfiesEqualsAndHashCodeContracts();
  }

  @Test
  void should_pass_if_contracts_are_satisfied_and_not_equal_objects_are_allowed_to_have_equal_hash_code() {
    assertThat(actual)
      .withEqualObjects(eq1, eq2)
      .withNotEqualObjectsAllowedToHaveEqualHashCode(notEqualSameHashCode)
      .satisfiesEqualsAndHashCodeContracts();
  }

  @Test
  void should_pass_if_contracts_are_satisfied_and_both_kinds_of_not_equal_objects_are_specified() {
    assertThat(actual)
      .withEqualObjects(eq1, eq2)
      .withNotEqualObjectsHavingDifferentHashCodes(notEqual)
      .withNotEqualObjectsAllowedToHaveEqualHashCode(notEqualSameHashCode)
      .satisfiesEqualsAndHashCodeContracts();
  }

  @ParameterizedTest
  @MethodSource("possibleBugs")
  void should_fail_if_object_doesnt_satisfy_a_contract(EqualsOrHashCodeBug bug,
                                                       String expectedMessagePattern) {
    bug.introduce(this);
    assertThatThrownBy(() ->
        assertThat(actual)
          .withEqualObjects(eq1, eq2)
          .withNotEqualObjectsHavingDifferentHashCodes(notEqual)
          .withNotEqualObjectsAllowedToHaveEqualHashCode(notEqualSameHashCode)
          .satisfiesEqualsAndHashCodeContracts())
      .isInstanceOf(AssertionError.class)
      .hasMessageMatching(expectedMessagePattern);
  }

  private static Object[][] possibleBugs() {
    return new Object[][] {
      {bug("not reflexive",
        test -> test.actual.makeNotEqualToItself()), "\\Q!actual.equals(actual)\\E"},
      {bug("not symmetric: actual.equals(eq1), !eq1.equals(actual)",
        test -> test.eq1.makeNotEqualTo(test.actual)), "\\Q!eq1.equals(actual)\\E"},
      {bug("not symmetric: !actual.equals(eq1), eq1.equals(actual)",
        test -> test.actual.makeNotEqualTo(test.eq1)), "\\Q!actual.equals(eq1)\\E"},
      {bug("not symmetric: actual.equals(eq2), !eq2.equals(actual)",
        test -> test.eq2.makeNotEqualTo(test.actual)), "\\Q!eq2.equals(actual)\\E"},
      {bug("not symmetric: !actual.equals(eq2), eq2.equals(actual)",
        test -> test.actual.makeNotEqualTo(test.eq2)), "\\Q!actual.equals(eq2)\\E"},
      {bug("not symmetric: !actual.equals(notEqual), notEqual.equals(actual)",
        test -> test.notEqual.makeEqualTo(test.actual)), "\\Q(notEqual).equals(actual)\\E"},
      {bug("not symmetric: !actual.equals(notEqualSameHash), notEqualSameHash.equals(actual)",
        test -> test.notEqualSameHashCode.makeEqualTo(test.actual)), "\\Q(notEqualSameHash).equals(actual)\\E"},
      {bug("not transitive: !eq1.equals(eq2)",
        test -> test.eq1.makeNotEqualTo(test.eq2)), "\\Q!eq1.equals(eq2)\\E"},
      {bug("not transitive: !eq2.equals(eq1)",
        test -> test.eq2.makeNotEqualTo(test.eq1)), "\\Q!eq2.equals(eq1)\\E"},
      {bug("not transitive: !eq1.equals(eq2) && !eq2.equals(eq1)",
        test -> {test.eq1.makeNotEqualTo(test.eq2); test.eq2.makeNotEqualTo(test.eq1); }),
        "\\Q!eq1.equals(eq2)\\E|\\Q!eq2.equals(eq1)\\E"},
      {bug("equals notEqual",
        test -> test.actual.makeEqualTo(test.notEqual)), "\\Qactual.equals(notEqual)\\E"},
      {bug("equals notEqualSameHash",
        test -> test.actual.makeEqualTo(test.notEqualSameHashCode)), "\\Qactual.equals(notEqualSameHash)\\E"},
      {bug("equals null",
        test -> test.actual.makeEqualTo(null)), "\\Qactual.equals(null)\\E"},
      {bug("equals new Object()",
        test -> test.actual.makeEqualToAnythingOfClassObject()), "\\Qactual.equals(new Object())\\E"},
      {bug("actual.hashCode() != eq1.hashCode()",
        test -> test.eq1.letHashCodeBe(YET_ANOTHER_HASH)),
        "\\Qactual.hashCode() == " + ACTUAL_HASH + " != " + YET_ANOTHER_HASH + " == eq1.hashCode()\\E"},
      {bug("actual.hashCode() != eq2.hashCode()",
        test -> test.eq2.letHashCodeBe(YET_ANOTHER_HASH)),
        "\\Qactual.hashCode() == " + ACTUAL_HASH + " != " + YET_ANOTHER_HASH + " == eq2.hashCode()\\E"},
      {bug("actual.hashCode() == notEqual.hashCode()",
        test -> test.notEqual.letHashCodeBe(ACTUAL_HASH)),
        "\\Qactual.hashCode() == " + ACTUAL_HASH + " == (notEqual).hashCode()\\E"},
    };
  }

  private static EqualsOrHashCodeBug bug(String name,
                                         Consumer<ObjectAssert_satisfiesEqualsAndHashCodeContracts_Test> bugSetup) {
    return new EqualsOrHashCodeBug(name, bugSetup);
  }

  @Test
  void should_fail_with_message_mentioning_not_equal_object_if_one_of_them_is_equal() {
    StubbableEqualsAndHashCode brokenNotEqualObject = new StubbableEqualsAndHashCode("brokenNotEqualObject");
    brokenNotEqualObject.letBeEqualTo(brokenNotEqualObject, actual, eq1, eq2);
    brokenNotEqualObject.letBeNotEqualTo(notEqual);
    brokenNotEqualObject.letHashCodeBe(YET_ANOTHER_HASH);
    actual.makeEqualTo(brokenNotEqualObject);
    eq1.makeEqualTo(brokenNotEqualObject);
    eq2.makeEqualTo(brokenNotEqualObject);
    assertThatThrownBy(() ->
          assertThat(actual)
        .withEqualObjects(eq1, eq2)
        .withNotEqualObjectsHavingDifferentHashCodes(notEqual, brokenNotEqualObject)
        .satisfiesEqualsAndHashCodeContracts())
      .hasMessageContaining("brokenNotEqualObject")
      .hasMessageContaining("equals(");
    assertThatThrownBy(() ->
      assertThat(actual)
        .withEqualObjects(eq1, eq2)
        .withNotEqualObjectsAllowedToHaveEqualHashCode(notEqual, brokenNotEqualObject)
        .satisfiesEqualsAndHashCodeContracts())
      .hasMessageContaining("brokenNotEqualObject")
      .hasMessageContaining("equals(");
  }

  @Test
  void should_fail_with_message_mentioning_not_equal_object_if_it_has_equal_hash_code_but_not_allowed_to() {
    StubbableEqualsAndHashCode brokenNotEqualObject = new StubbableEqualsAndHashCode("brokenNotEqualObject");
    brokenNotEqualObject.letBeEqualTo(brokenNotEqualObject);
    brokenNotEqualObject.letBeNotEqualTo(actual);
    brokenNotEqualObject.letHashCodeBe(actual.hashCode());
    actual.makeNotEqualTo(brokenNotEqualObject);
    eq1.makeNotEqualTo(brokenNotEqualObject);
    eq2.makeNotEqualTo(brokenNotEqualObject);
    assertThatThrownBy(() ->
      assertThat(actual)
        .withEqualObjects(eq1, eq2)
        .withNotEqualObjectsHavingDifferentHashCodes(notEqual, brokenNotEqualObject)
        .satisfiesEqualsAndHashCodeContracts())
      .hasMessageContaining("brokenNotEqualObject")
      .hasMessageContaining("hashCode()");
  }

  private static class EqualsOrHashCodeBug {
    private final String name;
    private final Consumer<ObjectAssert_satisfiesEqualsAndHashCodeContracts_Test> bugSetup;

    EqualsOrHashCodeBug(String name, Consumer<ObjectAssert_satisfiesEqualsAndHashCodeContracts_Test> bugSetup) {
      this.bugSetup = bugSetup;
      this.name = name;
    }

    void introduce(ObjectAssert_satisfiesEqualsAndHashCodeContracts_Test instance) {
      bugSetup.accept(instance);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private static class StubbableEqualsAndHashCode {
    private final String name;
    private int stubbedHashCode;
    // can't use sets here because we deliberately break equals() and hashCode()
    private Object[] stubbedEquals = new Object[0];
    private Object[] stubbedNotEquals = new Object[0];
    private boolean equalToAnythingOfClassObject;

    StubbableEqualsAndHashCode(String name) {
      this.name = name;
    }

    void letBeEqualTo(StubbableEqualsAndHashCode... equals) {
      stubbedEquals = equals;
    }

    void letBeNotEqualTo(StubbableEqualsAndHashCode... notEquals) {
      stubbedNotEquals = notEquals;
    }

    void makeNotEqualToItself() {
      makeNotEqualTo(this);
    }

    void makeNotEqualTo(Object obj) {
      stubbedEquals = remove(stubbedEquals, obj);
      stubbedNotEquals = add(stubbedNotEquals, obj);
    }

    void makeEqualTo(Object obj) {
      stubbedEquals = add(stubbedEquals, obj);
      stubbedNotEquals = remove(stubbedNotEquals, obj);
    }

    private static Object[] remove(Object[] array, Object what) {
      return Stream.of(array).filter(it -> it != what).toArray();
    }

    private static Object[] add(Object[] array, Object what) {
      Object[] result = Arrays.copyOf(array, array.length + 1);
      result[result.length - 1] = what;
      return result;
    }

    void makeEqualToAnythingOfClassObject() {
      equalToAnythingOfClassObject = true;
    }

    void letHashCodeBe(int hashCode) {
      stubbedHashCode = hashCode;
    }

    @Override
    public boolean equals(Object obj) {
      if (contains(stubbedEquals, obj))
        return true;
      else if (contains(stubbedNotEquals, obj))
        return false;
      else if (obj != null && obj.getClass().equals(Object.class))
        return equalToAnythingOfClassObject;
      else
        return false;
    }

    private static boolean contains(Object[] array, Object obj) {
      for (Object o : array) {
        if (o == obj)
          return true;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return stubbedHashCode;
    }

    @Override
    public String toString() {
      return name;
    }
  }

}
