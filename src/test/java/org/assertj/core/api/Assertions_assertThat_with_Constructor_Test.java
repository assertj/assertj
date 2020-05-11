package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.Assertions#assertThat(Constructor)}</code>.
 *
 * @author phx
 */
public class Assertions_assertThat_with_Constructor_Test {

  @Test
  public void should_create_Assert() throws NoSuchMethodException {
    AbstractConstructorAssert<ConstructorAssert, Constructor> assertions = Assertions
      .assertThat(String.class.getDeclaredConstructor());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() throws NoSuchMethodException {
    AbstractConstructorAssert<ConstructorAssert, Constructor> assertions = Assertions
      .assertThat(String.class.getDeclaredConstructor());
    assertThat(assertions.actual.getName())
      .isSameAs(String.class.getDeclaredConstructor().getName());
  }

  @Test
  public void should_pass_isPublic() throws NoSuchMethodException {
    assertThat(Person.class.getDeclaredConstructor(String.class)).isPublic();
  }

  @Test
  public void should_pass_isPrivate() throws NoSuchMethodException {
    assertThat(Person.class.getDeclaredConstructor(char.class)).isProtected();
  }

  @Test
  public void should_pass_isProtected() throws NoSuchMethodException {
    assertThat(Person.class.getDeclaredConstructor(int.class)).isPrivate();
  }

}
