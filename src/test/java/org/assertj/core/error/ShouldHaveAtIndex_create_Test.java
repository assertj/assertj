package org.assertj.core.error;


import org.assertj.core.api.TestCondition;
import org.assertj.core.data.Index;
import org.assertj.core.description.*;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldHaveAtIndex;
import org.junit.Test;

import static org.assertj.core.error.ShouldHaveAtIndex.shouldHaveAtIndex;
import static org.assertj.core.util.Lists.newArrayList;
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
    assertEquals("[Test] \nExpecting:\n <'Luke'>\nat index <1> to have:\n <red lightsaber>\nin:\n" +
                 " <['Yoda', 'Luke']>\n", message);
  }
}
