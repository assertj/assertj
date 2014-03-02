package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;

/**
 * Tests for <code>{@link ShouldHaveExtension#shouldHaveExtension(java.io.File, String, String)}</code>
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveExtension_create_Test {

  private final String expectedExtension = "java";

  private File actual = new FakeFile("actual-file.png");

  @Test
  public void should_create_error_message() throws Exception {
    assertThat(createMessage("png")).isEqualTo(
        "[TEST] \nExpecting\n <" + actual + ">\nto have extension:\n <\"" + expectedExtension
            + "\">\nbut have:\n <\"png\">.");
  }

  @Test
  public void should_create_error_message_when_actual_does_not_have_extension() throws Exception {
    assertThat(createMessage(null)).isEqualTo(
        "[TEST] \nExpecting\n <" + actual + ">\nto have extension:\n <\"" + expectedExtension
            + "\">\nbut current file does not have extension.");
  }

  private String createMessage(String actualExtension) {
    return shouldHaveExtension(actual, actualExtension, expectedExtension).create(new TestDescription("TEST"),
        new StandardRepresentation());
  }
}
