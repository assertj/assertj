package org.fest.assertions.error;

import org.fest.assertions.core.TestCondition;
import org.fest.assertions.data.Index;
import org.fest.assertions.description.*;
import org.junit.Test;

import static org.fest.assertions.error.ShouldHaveAtIndex.shouldHaveAtIndex;
import static org.fest.util.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Tests for <code>{@link ShouldHaveAtIndex#create(Description)}</code>.
 *
 * @author Bo Gotthardt
 */
public class ShouldHaveAtIndex_create_Test {

  @Test public void should_create_error_message() {
    ErrorMessageFactory factory = shouldHaveAtIndex(newArrayList("Yoda", "Luke"), new TestCondition<String>("red lightsaber"), Index.atIndex(1), "Luke");
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<'Luke'> at index <1> to have:<red lightsaber> in:\n" +
                 " <['Yoda', 'Luke']>\n", message);
  }
}
