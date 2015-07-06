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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import java.io.InputStream;

import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsException;
import org.assertj.core.util.VisibleForTesting;


/**
 * Base class for all implementations of assertions for {@link InputStream}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 */
public abstract class AbstractInputStreamAssert<S extends AbstractInputStreamAssert<S, A>, A extends InputStream> extends AbstractAssert<S, A> {

	@VisibleForTesting
	InputStreams inputStreams = InputStreams.instance();

	protected AbstractInputStreamAssert(A actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the content of the actual {@code InputStream} is equal to the content of the given one.
	 *
	 * @param expected the given {@code InputStream} to compare the actual {@code InputStream} to.
	 * @return {@code this} assertion object.
	 * @throws NullPointerException if the given {@code InputStream} is {@code null}.
	 * @throws AssertionError if the actual {@code InputStream} is {@code null}.
	 * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the content of the given one.
	 * @throws InputStreamsException if an I/O error occurs.
   *
   * @deprecated use hasSameContentAs
	 */
  @Deprecated
	public S hasContentEqualTo(InputStream expected) {
		inputStreams.assertSameContentAs(info, actual, expected);
		return myself;
	}

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the content of the given one.
   *
   * @param expected the given {@code InputStream} to compare the actual {@code InputStream} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the content of the given one.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public S hasSameContentAs(InputStream expected) {
      inputStreams.assertSameContentAs(info, actual, expected);
      return myself;
  }
}
