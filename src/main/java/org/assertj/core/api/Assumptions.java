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

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.assertj.core.util.CheckReturnValue;

import java.lang.reflect.Method;
import java.util.List;

public class Assumptions {

  /**
   * Creates a new instance of <code>{@link ObjectAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @param <T> the type of the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  @SuppressWarnings("unchecked")
  public static <T> AbstractObjectAssert<?, T> assumeThat(T actual) {
    return (AbstractObjectAssert<?, T>) enhanceAsAssumption(ObjectAssert.class, Object.class, actual);
  }

  /**
   * Creates a new instance of <code>{@link StringAssert}</code> assumption.
   *
   * @param actual the actual value.
   * @return the created assumption for assertion object.
   */
  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, String> assumeThat(String actual) {
    return enhanceAsAssumption(StringAssert.class, String.class, actual);
  }

  @SuppressWarnings("unchecked")
  private static <ASSERTION, ACTUAL> ASSERTION enhanceAsAssumption(Class<ASSERTION> assertionType,
                                                                   Class<ACTUAL> actualType,
                                                                   Object actual) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(assertionType);
    enhancer.setCallback(new MethodInterceptor() {
      @Override
      public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        try {
          Object result = methodProxy.invokeSuper(o, args);
          if (result != o && result instanceof AbstractAssert) {
            return enhanceAsAssumption(result);
          }
          return result;
        } catch (AssertionError e) {
          throw createAssumptionViolatedException(e);
        }
      }
    });

    return (ASSERTION) enhancer.create(new Class[] { actualType }, new Object[] { actual });
  }

  private static RuntimeException createAssumptionViolatedException(AssertionError e) throws ReflectiveOperationException {
    try {
      return createAssumptionViolatedException(Class.forName("org.junit.AssumptionViolatedException"), e);
    } catch (ClassNotFoundException junitClassNotFound) {
      try {
        return createAssumptionViolatedException(Class.forName("org.testng.SkipException"), e);
      } catch (ClassNotFoundException testngClassNotFound) {
        throw new IllegalStateException("Assumptions require JUnit or TestNG in the classpath");
      }
    }
  }

  private static RuntimeException createAssumptionViolatedException(Class<?> exceptionClass, AssertionError e)
    throws ReflectiveOperationException {
    return (RuntimeException) exceptionClass.getConstructor(String.class, Throwable.class)
      .newInstance("assumption failed due to assertion error", e);
  }

  private static Object enhanceAsAssumption(Object assertion) {
    if (assertion instanceof StringAssert) {
      return enhanceAsAssumption(StringAssert.class, String.class, ((StringAssert) assertion).actual);
    }
    if (assertion instanceof ListAssert) {
      return enhanceAsAssumption(ListAssert.class, List.class, ((ListAssert) assertion).actual);
    }

    throw new IllegalArgumentException("Unsupported assumption creation for " + assertion.getClass());
  }

}
