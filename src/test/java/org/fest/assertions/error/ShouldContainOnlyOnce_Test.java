package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.error.ShouldContainStringOnlyOnce.shouldContainOnlyOnce;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.description.TextDescription;
import org.fest.assertions.internal.*;
import org.fest.assertions.util.CaseInsensitiveStringComparator;

public class ShouldContainOnlyOnce_Test {

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
        "[Test] expecting:\n<'motif'>\n to appear only once in:\n<'aaamotifmotifaabbbmotifaaa'>\n but it appeared 3 times.",
        message);
  }

  @Test
  public void should_create_error_message_when_string_to_search_does_not_appear() {
    String message = factoryWithNoOccurence.create(new TestDescription("Test"));
    assertEquals(
        "[Test] expecting:\n<'motif'>\n to appear only once in:\n<'aaamodifmoifaabbbmotfaaa'>\n but it did not appear.",
        message);
  }

  @Test
  public void should_create_error_message_when_string_to_search_does_not_appear_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainOnlyOnce("aaamoDifmoifaabbbmotfaaa", "MOtif", 0,
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] expecting:\n<'MOtif'>\n to appear only once in:\n<'aaamoDifmoifaabbbmotfaaa'>\n but it did not appear according to 'CaseInsensitiveStringComparator' comparator.",
        message);
  }

  @Test
  public void should_create_error_message_when_string_to_search_appears_several_times_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainOnlyOnce("aaamotIFmoTifaabbbmotifaaa", "MOtif", 3,
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] expecting:\n<'MOtif'>\n to appear only once in:\n<'aaamotIFmoTifaabbbmotifaaa'>\n but it appeared 3 times according to 'CaseInsensitiveStringComparator' comparator.",
        message);
  }

}
