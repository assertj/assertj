package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class ShouldMatch_create_Test {

  @Rule
  public ExpectedException thrown = none();
  
  @Test
  public void should_create_error_message_with_default_predicate_description() {
	ErrorMessageFactory factory = shouldMatch("Yoda", color -> color.equals("green"), PredicateDescription.GIVEN);
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo("[Test] \nExpecting:\n  <\"Yoda\">\nto match given predicate." + ShouldMatch.ADVICE);
  }

  @Test
  public void should_create_error_message_with_predicate_description() {
	ErrorMessageFactory factory = shouldMatch("Yoda", (String color) -> color.equals("green"),
	                                          new PredicateDescription("green light saber"));
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo("[Test] \nExpecting:\n  <\"Yoda\">\nto match 'green light saber' predicate.");
  }
  
  @Test
  public void should_fail_if_predicate_description_is_null() {
	// then
	thrown.expectNullPointerException("The predicate description must not be null");
	// when
	shouldMatch("Yoda", color -> color.equals("green"), null);
  }
}
