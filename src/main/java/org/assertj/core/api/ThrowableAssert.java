/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.util.concurrent.Callable;

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
public class ThrowableAssert extends AbstractThrowableAssert<ThrowableAssert, Throwable> {

  public interface ThrowingCallable {
    void call() throws Throwable;
  }

  public ThrowableAssert(Throwable actual) {
    super(actual, ThrowableAssert.class);
  }

  public <V> ThrowableAssert(Callable<V> runnable) {
    super(buildThrowableAssertFromCallable(runnable), ThrowableAssert.class);
  }

  private static <V> Throwable buildThrowableAssertFromCallable(Callable<V> callable) throws AssertionError {
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
      return throwable;
    }
  }

  public static Throwable catchThrowable(ThrowingCallable shouldRaiseThrowable) {
    try {
      shouldRaiseThrowable.call();
    } catch (Throwable throwable) {
      return throwable;
    }
    return null;
  }
}
