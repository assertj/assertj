/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.util.function.Consumer;

import org.opentest4j.MultipleFailuresError;

/**
 * Parent interface for soft assertion implementations.
 *
 * @author Fr Jeremy Krieg
 * @see AbstractSoftAssertions
 * @see SoftAssertions
 */
public interface SoftAssertionsProvider extends AssertionErrorCollector {

  interface ThrowingRunnable {
    void run() throws Exception;
  }

  /**
   * Creates a proxied assertion class of the given type. The returned value
   * is an assertion object compatible with the supplied assertion class, but
   * instead of throwing errors it will collect them and store.
   *
   * @param <SELF> The type of the assertion class
   * @param <ACTUAL> The type of the object-under-test
   * @param assertClass Class instance for the assertion type.
   * @param actualClass Class instance for the type of the object-under-test.
   * @param actual The actual object-under-test.
   * @return A proxied assertion class for the given object-under-test.
   */
  <SELF extends Assert<? extends SELF, ? extends ACTUAL>, ACTUAL> SELF proxy(Class<SELF> assertClass, Class<ACTUAL> actualClass,
                                                                             ACTUAL actual);

  /**
   * Verifies that no soft assertions have failed.
   *
   * @throws MultipleFailuresError if possible or SoftAssertionError if any proxied assertion objects threw an {@link AssertionError}
   */
  void assertAll();

  /**
   * Add all errors of <code>collector</code> argument to current <code>{@link SoftAssertionsProvider}</code> instance.
   *
   * @param collector the <code>{@link AssertionErrorCollector}</code> error source
   */
  default void assertAlso(AssertionErrorCollector collector) {
    collector.assertionErrorsCollected().forEach(this::collectAssertionError);
  }

  /**
   * Returns the result of last soft assertion which can be used to decide what the next one should be.
   * <p>
   * Example :
   * <pre><code class='java'> Person person = ...
   * SoftAssertions soft = new SoftAssertions();
   * if (soft.assertThat(person.getAddress()).isNotNull().wasSuccess()) {
   *     soft.assertThat(person.getAddress().getStreet()).isNotNull();
   * }</code></pre>
   *
   * @return true if the last assertion was a success.
   */
  boolean wasSuccess();

  /**
   * Catch and collect assertion errors coming from standard and <b>custom</b> assertions.
   * <p>
   * Example :
   * <pre><code class='java'> SoftAssertions softly = new SoftAssertions();
   * softly.check(() -&gt; Assertions.assertThat(…).…);
   * softly.check(() -&gt; CustomAssertions.assertThat(…).…);
   * softly.assertAll(); </code></pre>
   *
   * @param assertion an assertion call.
   */
  default void check(ThrowingRunnable assertion) {
    try {
      assertion.run();
    } catch (AssertionError error) {
      collectAssertionError(error);
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
  * Use this to avoid having to call assertAll manually.
  *
  * <pre><code class='java'> &#064;Test
  * public void host_dinner_party_where_nobody_dies() {
  *   Mansion mansion = new Mansion();
  *   mansion.hostPotentiallyMurderousDinnerParty();
  *   SoftAssertion.assertSoftly(SoftAssertions.class, softly -&gt; {
  *     softly.assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
  *     softly.assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
  *     softly.assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
  *     softly.assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
  *     softly.assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
  *     softly.assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
  *     softly.assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
  *   });
  * }</code></pre>
  *
  * @param <S> the concrete type of soft assertions to use.
  * @param type the class object of the concrete type of soft assertions to use.
  * @param softly the Consumer containing the code that will make the soft assertions.
  *     Takes one parameter (the SoftAssertion instance used to make the assertions).
  * @throws MultipleFailuresError if possible or SoftAssertionError if any proxied assertion objects threw an {@link AssertionError}
  * @since 3.16.0
  */
  static <S extends SoftAssertionsProvider> void assertSoftly(Class<S> type, Consumer<S> softly) {
    S assertions;
    try {
      assertions = type.getConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    softly.accept(assertions);
    assertions.assertAll();
  }
}
