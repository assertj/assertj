package org.assertj.core.assertions.error;

import static org.assertj.core.assertions.data.Index.atIndex;
import static org.assertj.core.assertions.error.ShouldBeAtIndex.shouldBeAtIndex;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;

import org.assertj.core.assertions.core.TestCondition;
import org.assertj.core.assertions.description.TextDescription;
import org.assertj.core.assertions.error.ErrorMessageFactory;
import org.assertj.core.assertions.error.ShouldBeAtIndex;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeAtIndex#create(org.assertj.core.assertions.description.Description)}</code>.
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
