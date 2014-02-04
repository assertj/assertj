package org.assertj.core.error;

import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldBeAtIndex.shouldBeAtIndex;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeAtIndex#create(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Bo Gotthardt
 */
public class ShouldBeAtIndex_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldBeAtIndex(newArrayList("Yoda", "Luke"), new TestCondition<String>("red lightsaber"), atIndex(1),
        "Luke");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \nExpecting:\n <\"Luke\">\nat index <1> to be:\n <red lightsaber>\nin:\n <[\"Yoda\", \"Luke\"]>\n", message);
  }
}
