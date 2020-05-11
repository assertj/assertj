package org.assertj.core.api;

import java.lang.reflect.Constructor;

/**
 * Assertion methods for {@code Constructor}.
 * <p>
 * To create a new instance of this Constructor, invoke <code>{@link
 * org.assertj.core.api.Assertions#assertThat(Constructor)}</code>
 * </p>
 *
 * @author phx
 */
public class ConstructorAssert extends AbstractConstructorAssert<ConstructorAssert, Constructor> {

  public ConstructorAssert(Constructor actual) {
    super(actual, ConstructorAssert.class);
  }

  public final ConstructorAssert isPublic() {
    return super.isPublic();
  }

  public final ConstructorAssert isPrivate() {
    return super.isPrivate();
  }

  public final ConstructorAssert isProtected() {
    return super.isProtected();
  }

  public final ConstructorAssert hasArguments(Class<?>... arguments) {
    return super.hasArguments(arguments);
  }


}
