package org.assertj.core.api;

import java.io.InputStream;

import org.assertj.core.internal.InputStreams;
import org.assertj.core.internal.InputStreamsException;
import org.assertj.core.util.VisibleForTesting;


/**
 * Base class for all implementations of assertions for {@link InputStream}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
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
	 */
	public S hasContentEqualTo(InputStream expected) {
		inputStreams.assertEqualContent(info, actual, expected);
		return myself;
	}
}
