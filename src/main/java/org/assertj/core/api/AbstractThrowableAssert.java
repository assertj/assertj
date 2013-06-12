/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this Throwable except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.Throwables;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Throwable}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public abstract class AbstractThrowableAssert<S extends AbstractThrowableAssert<S, A>, A extends Throwable>
		extends AbstractAssert<S, A> {

	@VisibleForTesting
	Throwables throwables = Throwables.instance();

	protected AbstractThrowableAssert(A actual, Class<?> selfType) {
		super(actual, selfType);
	}

	/**
	 * Verifies that the message of the actual {@code Throwable} is equal to the given one.
	 *
	 * @param message the expected message.
	 * @return this assertion object.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
	 */
	public S hasMessage(String message) {
		throwables.assertHasMessage(info, actual, message);
		return myself;
	}

	/**
	 * Verifies that the actual {@code Throwable} does not have a cause.
	 *
	 * @return this assertion object.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} has a cause.
	 */
	public S hasNoCause() {
		throwables.assertHasNoCause(info, actual);
		return myself;
	}

	/**
	 * Verifies that the message of the actual {@code Throwable} starts with the given description.
	 *
	 * @param description the description expected to start the actual {@code Throwable}'s message.
	 * @return this assertion object.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
	 */
	public S hasMessageStartingWith(String description) {
		throwables.assertHasMessageStartingWith(info, actual, description);
		return myself;
	}

	/**
	 * Verifies that the message of the actual {@code Throwable} contains with the given description.
	 *
	 * @param description the description expected to be contained in the actual {@code Throwable}'s message.
	 * @return this assertion object.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
	 */
	public S hasMessageContaining(String description) {
		throwables.assertHasMessageContaining(info, actual, description);
		return myself;
	}

	/**
	 * Verifies that the message of the actual {@code Throwable} ends with the given description.
	 *
	 * @param description the description expected to end the actual {@code Throwable}'s message.
	 * @return this assertion object.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
	 */
	public S hasMessageEndingWith(String description) {
		throwables.assertHasMessageEndingWith(info, actual, description);
		return myself;
	}

	/**
	 * Verifies that the cause of the actual {@code Throwable} is an instance of the given type.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * Throwable throwable = new Throwable(new NullPointerException());
	 *
	 * // assertion will pass
	 * assertThat(throwable).hasCauseInstanceOf(NullPointerException.class);
	 * assertThat(throwable).hasCauseInstanceOf(RuntimeException.class);
	 *
	 * // assertion will fail
	 * assertThat(throwable).hasCauseInstanceOf(IllegalArgumentException.class);
	 * </pre>
	 *
	 * </p>
	 *
	 * @param type the expected cause type.
	 * @return this assertion object.
	 * @throws NullPointerException if given type is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} has no cause.
	 * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
	 */
	public S hasCauseInstanceOf(Class<? extends Throwable> type) {
		throwables.assertHasCauseInstanceOf(info, actual, type);
		return myself;
	}

	/**
	 * Verifies that the cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * Throwable throwable = new Throwable(new NullPointerException());
	 *
	 * // assertion will pass
	 * assertThat(throwable).hasCauseExactlyInstanceOf(NullPointerException.class);
	 *
	 * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
	 * assertThat(throwable).hasCauseExactlyInstanceOf(RuntimeException.class);
	 * assertThat(throwable).hasCauseExactlyInstanceOf(IllegalArgumentException.class);
	 * </pre>
	 *
	 * </p>
	 *
	 * @param type the expected cause type.
	 * @return this assertion object.
	 * @throws NullPointerException if given type is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} has no cause.
	 * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
	 *           type.
	 */
	public S hasCauseExactlyInstanceOf(Class<? extends Throwable> type) {
		throwables.assertHasCauseExactlyInstanceOf(info, actual, type);
		return myself;
	}

	/**
	 * Verifies that the root cause of the actual {@code Throwable} is an instance of the given type.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException()));
	 *
	 * // assertion will pass
	 * assertThat(throwable).hasRootCauseInstanceOf(NullPointerException.class);
	 * assertThat(throwable).hasRootCauseInstanceOf(RuntimeException.class);
	 *
	 * // assertion will fail
	 * assertThat(throwable).hasRootCauseInstanceOf(IllegalStateException.class);
	 * </pre>
	 *
	 * </p>
	 *
	 * @param type the expected cause type.
	 * @return this assertion object.
	 * @throws NullPointerException if given type is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} has no cause.
	 * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
	 */
	public S hasRootCauseInstanceOf(Class<? extends Throwable> type) {
		throwables.assertHasRootCauseInstanceOf(info, actual, type);
		return myself;
	}

	/**
	 * Verifies that the root cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException()));
	 *
	 * // assertion will pass
	 * assertThat(throwable).hasRootCauseExactlyInstanceOf(NullPointerException.class);
	 *
	 * // assertion will fail (even if NullPointerException is a RuntimeException since we want an exact match)
	 * assertThat(throwable).hasRootCauseExactlyInstanceOf(RuntimeException.class);
	 * assertThat(throwable).hasRootCauseExactlyInstanceOf(IllegalStateException.class);
	 * </pre>
	 *
	 * </p>
	 *
	 * @param type the expected cause type.
	 * @return this assertion object.
	 * @throws NullPointerException if given type is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} is {@code null}.
	 * @throws AssertionError if the actual {@code Throwable} has no cause.
	 * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
	 *           given type.
	 */
	public S hasRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
		throwables.assertHasRootCauseExactlyInstanceOf(info, actual, type);
		return myself;
	}
}
