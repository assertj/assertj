package org.assertj.core.api;

/**
 * Specialized operation similar to Java 8 java.util.function.Consumer that accepts a single {@link BDDSoftAssertions}
 * input argument and and returns no result.
 *
 * @author Lovro Pandzic
 * @see BDDSoftAssertions
 */
interface BDDSoftAssertionsConsumer {

	void accept(BDDSoftAssertions softly);
}
