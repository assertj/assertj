package org.fest.assertions.api;

import java.io.InputStream;

import org.fest.assertions.internal.InputStreams;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for <code>{@link InputStream}</code>s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(InputStream)}</code>.
 * </p>
 */
public class InputStreamAssert extends AbstractAssert<InputStreamAssert, InputStream> {

	@VisibleForTesting InputStreams inputStreams = InputStreams.instance();
	
	public InputStreamAssert(InputStream actual) {
		super(actual, InputStreamAssert.class);
	}

	public void hasContentEqualTo(InputStream expected) {
		inputStreams.assertEqualContent(info, actual, expected);
	}
	
}
