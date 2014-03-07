package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.mockito.Mockito.when;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldHaveNoParent#shouldHaveNoParent(java.io.File)}</code>
 * 
 * @author Jean-Christophe Gay
 */
@RunWith(MockitoJUnitRunner.class)
public class ShouldHaveNoParent_create_Test {

  @Mock
  private File actual;

  @Test
  public void should_create_error_message_when_actual_does_not_have_a_parent() {

    when(actual.getParentFile()).thenReturn(new FakeFile("unexpected.parent"));

    assertThat(createMessage()).isEqualTo("[TEST] \n" +
                                          "Expecting file (or directory) without parent, but parent was:\n" +
                                          "  <" + actual.getParentFile() + ">");
  }

  private String createMessage() {
    return shouldHaveNoParent(actual).create(new TestDescription("TEST"), new StandardRepresentation());
  }
}
