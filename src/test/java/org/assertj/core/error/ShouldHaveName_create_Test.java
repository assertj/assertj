package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldHaveName#shouldHaveName(java.io.File, String)}</code>
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveName_create_Test {

  private final String expectedName = "java";

  private File actual = new FakeFile("somewhere/actual-file".replace("/", File.separator));

  @Test
  public void should_create_error_message() throws Exception {
    assertThat(createMessage()).isEqualTo(String.format("[TEST] %n" +
                                                        "Expecting%n" +
                                                        "  <" + actual + ">%n" +
                                                        "to have name:%n" +
                                                        "  <\"" + expectedName + "\">%n" +
                                                        "but had:%n" +
                                                        "  <\"actual-file\">."));
  }

  private String createMessage() {
    return shouldHaveName(actual, expectedName).create(new TestDescription("TEST"), new StandardRepresentation());
  }
}
