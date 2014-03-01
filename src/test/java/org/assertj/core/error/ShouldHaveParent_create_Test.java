package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.mockito.Mockito.when;

/**
 * Tests for <code>{@link ShouldHaveParent#shouldHaveParent(java.io.File, java.io.File)}</code>
 * 
 * @author Jean-Christophe Gay
 */
@RunWith(MockitoJUnitRunner.class)
public class ShouldHaveParent_create_Test {

  @Spy
  private File actual = new FakeFile("actual");

  private File expectedParent = new FakeFile("expected.parent");

  @Test
  public void should_create_error_message_when_actual_does_not_have_a_parent() {

    when(actual.getParentFile()).thenReturn(null);

    assertThat(createMessage()).isEqualTo(
        "[TEST] \n" + "Expecting file\n" + " <" + actual + ">\n" + "to have parent:\n" + " <" + expectedParent + ">\n"
            + "but does not have one.");
  }

  @Test
  public void should_create_error_message_when_actual_does_not_have_expected_parent() throws Exception {

    when(actual.getParentFile()).thenReturn(new FakeFile("not.expected.parent"));

    assertThat(createMessage()).isEqualTo(
        "[TEST] \nExpecting file\n <" + actual + ">\nto have parent:\n <" + expectedParent + ">\nbut have:\n <"
            + actual.getParentFile() + ">.");
  }

  private String createMessage() {
    return shouldHaveParent(actual, expectedParent).create(new TestDescription("TEST"), new StandardRepresentation());
  }
}
