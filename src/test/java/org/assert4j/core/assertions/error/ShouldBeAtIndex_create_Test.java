package org.assert4j.core.assertions.error;

import static org.assert4j.core.assertions.data.Index.atIndex;
import static org.assert4j.core.assertions.error.ShouldBeAtIndex.shouldBeAtIndex;
import static org.assert4j.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;

import org.assert4j.core.assertions.core.TestCondition;
import org.assert4j.core.assertions.description.TextDescription;
import org.assert4j.core.assertions.error.ErrorMessageFactory;
import org.assert4j.core.assertions.error.ShouldBeAtIndex;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeAtIndex#create(org.assert4j.core.assertions.description.Description)}</code>.
 * 
 * @author Bo Gotthardt
 */
public class ShouldBeAtIndex_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldBeAtIndex(newArrayList("Yoda", "Luke"), new TestCondition<String>("red lightsaber"), atIndex(1),
        "Luke");
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<'Luke'> at index <1> to be:<red lightsaber> in:\n <['Yoda', 'Luke']>\n", message);
  }
}
