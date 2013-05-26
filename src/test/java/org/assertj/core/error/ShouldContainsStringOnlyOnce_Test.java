package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.*;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;


public class ShouldContainsStringOnlyOnce_Test {

  private ErrorMessageFactory factoryWithSeveralOccurences;
  private ErrorMessageFactory factoryWithNoOccurence;

  @Before
  public void setUp() {
    factoryWithSeveralOccurences = shouldContainOnlyOnce("aaamotifmotifaabbbmotifaaa", "motif", 3);
    factoryWithNoOccurence = shouldContainOnlyOnce("aaamodifmoifaabbbmotfaaa", "motif", 0);
  }

  @Test
  public void should_create_error_message_when_string_to_search_appears_several_times() {
    String message = factoryWithSeveralOccurences.create(new TestDescription("Test"));
    assertEquals(
        "[Test] \nExpecting:\n <'motif'>\nto appear only once in:\n <'aaamotifmotifaabbbmotifaaa'>\nbut it appeared 3 times ",
        message);
  }

  @Test
  public void should_create_error_message_when_string_to_search_does_not_appear() {
    String message = factoryWithNoOccurence.create(new TestDescription("Test"));
    assertEquals(
        "[Test] \nExpecting:\n <'motif'>\nto appear only once in:\n <'aaamodifmoifaabbbmotfaaa'>\nbut it did not appear ",
        message);
  }

  @Test
  public void should_create_error_message_when_string_to_search_does_not_appear_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainOnlyOnce("aaamoDifmoifaabbbmotfaaa", "MOtif", 0,
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] \nExpecting:\n <'MOtif'>\nto appear only once in:\n <'aaamoDifmoifaabbbmotfaaa'>\nbut it did not appear according to 'CaseInsensitiveStringComparator' comparator",
        message);
  }

  @Test
  public void should_create_error_message_when_string_to_search_appears_several_times_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainOnlyOnce("aaamotIFmoTifaabbbmotifaaa", "MOtif", 3,
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] \nExpecting:\n <'MOtif'>\nto appear only once in:\n <'aaamotIFmoTifaabbbmotifaaa'>\nbut it appeared 3 times according to 'CaseInsensitiveStringComparator' comparator",
        message);
  }

}
