package org.fest.assertions.error;

import static org.fest.assertions.error.ShouldBeAtIndex.shouldBeAtIndex;
import static org.fest.util.Collections.list;
import static org.junit.Assert.assertEquals;

import org.fest.assertions.core.TestCondition;
import org.fest.assertions.data.Index;
import org.fest.assertions.description.TextDescription;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeAtIndex#create(org.fest.assertions.description.Description)}</code>.
 *
 * @author Bo Gotthardt
 */
public class ShouldBeAtIndex_create_Test {

  @Test public void should_create_error_message() {
    ErrorMessageFactory factory = shouldBeAtIndex(list("Yoda", "Luke"), new TestCondition<String>("red lightsaber"), Index.atIndex(1), "Luke");
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<'Luke'> at index <1> to be:<red lightsaber> in:\n" +
        " <['Yoda', 'Luke']>\n", message);
  }
}
