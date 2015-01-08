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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.groups.Properties.extractProperty;

import java.util.List;

/**
 * <p>
 * BDD style variation of {@link SoftAssertions}.
 * </p>
 *
 * <p>
 * Example usage compared to SoftAssertions:
 * </p>
 *
 * SoftAssertions:
 *
 * <pre><code class='java'>
 *     SoftAssertions softly = new SoftAssertions();
 *     softly.assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *     softly.assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *     softly.assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *     softly.assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *     softly.assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *     softly.assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *     softly.assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 *     softly.assertAll();
 * </code></pre>
 *
 * translated to BDDSoftAssertions:
 *
 * <pre><code class='java'>
 *     BDDSoftAssertions.then(new BDDSoftAssertionsConsumer() {
 *
 *         &#064Override
 *         public void accept(BDDSoftAssertions softly) {
 *
 *             softly.assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *             softly.assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *             softly.assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *             softly.assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *             softly.assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *             softly.assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *             softly.assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 *         }
 *     });
 * </code></pre>
 *
 * simplified with Java 8 lambdas:
 *
 * <pre><code class='java'>
 *     BDDSoftAssertions.then(softly -> {
 *
 *             softly.assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *             softly.assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *             softly.assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *             softly.assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *             softly.assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *             softly.assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *             softly.assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 *     });
 * </code></pre>
 *
 * Note that {@link #assertAll()} is called in {@link #then} for the user and therefore it is private.
 *
 * @author Lovro Pandzic
 * @see org.assertj.core.api.SoftAssertions
 */
public final class BDDSoftAssertions extends AbstractSoftAssertions {

	/**
	 * Entry point for constructing {@link BDDSoftAssertions}.
	 *
	 * @param consumer that performs multiple assertions on the provided {@link BDDSoftAssertions}
	 */
	public static void then(BDDSoftAssertionsConsumer consumer) {

		BDDSoftAssertions softAssertions = new BDDSoftAssertions();
		consumer.accept(softAssertions);
		softAssertions.assertAll();
	}

	private BDDSoftAssertions() {

	}

	/**
	 * Verifies that no proxied assertion methods have failed.
	 *
	 * <p><strong> This method is marked private since {@link #then} calls this method after execution of {@link
	 * BDDSoftAssertionsConsumer#accept(BDDSoftAssertions)}. </strong> </p>
	 *
	 * @throws SoftAssertionError if any proxied assertion objects threw
	 * @see SoftAssertions#assertAll()
	 */
	private void assertAll() {

		List<Throwable> errors = collector.errors();
		if (!errors.isEmpty()) {
			throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
		}
	}

}
