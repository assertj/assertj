/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;

import java.util.concurrent.Callable;

import org.assertj.core.util.Throwables;

/**
 * Assertion methods for {@link Throwable}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Throwable)}</code>.
 * </p>
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ThrowableAssert<ACTUAL extends Throwable> extends AbstractThrowableAssert<ThrowableAssert<ACTUAL>, ACTUAL> {

  public static final String VOWEL = "AEIOU";

  public interface ThrowingCallable {
    void call() throws Throwable;
  }

  public ThrowableAssert(ACTUAL actual) {
    super(actual, ThrowableAssert.class);
  }

  public <V> ThrowableAssert(Callable<V> runnable) {
    super(buildThrowableAssertFromCallable(runnable), ThrowableAssert.class);
  }

  @SuppressWarnings("unchecked")
  private static <V, THROWABLE extends Throwable> THROWABLE buildThrowableAssertFromCallable(Callable<V> callable) throws AssertionError {
    try {
      callable.call();
      // fail if the expected exception was *not* thrown
      Fail.fail("Expecting code to throw an exception.");
      // this will *never* happen...
      return null;
    } catch (AssertionError e) {
      // do not handle AssertionErrors in the next catch block!
      throw e;
    } catch (Throwable throwable) {
      // the throwable we will check
      return (THROWABLE) throwable;
    }
  }

  /**
   * Allows catching a {@link Throwable} more easily when used with Java 8 lambdas.
   * <p>
   * This caught {@link Throwable} can then be asserted.
   * <p>
   * If no throwable is thrown, this method throws an {@link AssertionError}.
   * <p>
   * If you need to assert on the real type of Throwable caught (e.g. IOException), use
   * {@link #catchThrowableOfType(Class, ThrowingCallable)}.
   * <p>
   * Example:
   * <pre><code class='java'>{@literal @}Test
   * public void testException() {
   *   // when
   *   Throwable thrown = catchThrowable(() -&gt; { throw new Exception("boom!"); });
   *
   *   // then
   *   assertThat(thrown).isInstanceOf(Exception.class)
   *                     .hasMessageContaining("boom");
   * } </code></pre>
   *
   * @param shouldRaiseThrowable The lambda with the code that should raise the throwable.
   * @return The captured throwable.
   * @throws AssertionError if shouldRaiseThrowable did not throw any throwable.
   * @see #catchThrowableOfType(Class, ThrowingCallable)
   */
  public static Throwable catchThrowable(ThrowingCallable shouldRaiseThrowable) {
    try {
      shouldRaiseThrowable.call();
    } catch (Throwable throwable) {
      return throwable;
    }
    throw new AssertionError("Expecting code to raise a Throwable");
  }

  /**
   * Allows catching a {@link Throwable} of a specific type.
   * <p>
   * If no throwable is thrown, this method throws an {@link AssertionError}.
   * <p>
   * Example:
   * <pre><code class='java'> class TextException extends Exception {
   *   int line;
   *   int column;
   *
   *   public TextException(String msg, int line, int column) {
   *     super(msg);
   *     this.line = line;
   *     this.column = column;
   *   }
   * }
   *
   * TextException textException = catchThrowableOfType(() -&gt; { throw new TextException("boom!", 1, 5); },
   *                                                    TextException.class);
   * // assertions succeed:
   * assertThat(textException).hasMessage("boom!");
   * assertThat(textException.line).isEqualTo(1);
   * assertThat(textException.column).isEqualTo(5);
   *
   * // fails as TextException is not a RuntimeException:
   * catchThrowableOfType(RuntimeException.class, () -&gt; { throw new TextException("boom!", 1, 5); });</code></pre>
   *
   * @param <THROWABLE>          the {@link Throwable} type.
   * @param shouldRaiseThrowable The lambda with the code that should raise the throwable.
   * @param type                 The type of throwable that the code is expected to raise.
   * @return The captured throwable.
   * @throws AssertionError if shouldRaiseThrowable did not throw any throwable.
   */
  @SuppressWarnings("unchecked")
  public static <THROWABLE extends Throwable> THROWABLE catchThrowableOfType(Class<THROWABLE> type,
                                                                             ThrowingCallable shouldRaiseThrowable) {
    Throwable throwable = Throwables.catchThrowable(shouldRaiseThrowable);
    if (throwable == null) {
      String typeSimpleName = type.getSimpleName();
      String article = startsWithVowel(typeSimpleName) ? "an" : "a";
      throw new AssertionError("Expecting code to raise %s %s".formatted(article, typeSimpleName));
    }
    // check exception's type
    new ThrowableAssert<>(throwable).as("Checking code thrown Throwable")
                                    .overridingErrorMessage(shouldBeInstance(throwable, type).create())
                                    .isInstanceOf(type);
    return (THROWABLE) throwable;
  }

  private static boolean startsWithVowel(String typeSimpleName) {
    return VOWEL.indexOf(typeSimpleName.charAt(0)) != -1;
  }

}
