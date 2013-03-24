package org.assertj.core.api;

import static org.assertj.core.util.Sets.newLinkedHashSet;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.Iterator;

import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Iterator)}</code>.
 * 
 * @author Julien Meddah
 * @author Joel Costigliola
 */
public class Assertions_assertThat_with_Iterator_Test {

  @Test
  public void should_create_Assert() {
    IterableAssert<Object> assertions = Assertions.assertThat(newLinkedHashSet());
    assertNotNull(assertions);
  }

  @Test
  public void should_initialise_actual() {
    Iterator<String> names = asList("Luke", "Leia").iterator();
    IterableAssert<String> assertions = assertThat(names);
    assertThat(assertions.actual, hasItems("Leia", "Luke"));
  }
}