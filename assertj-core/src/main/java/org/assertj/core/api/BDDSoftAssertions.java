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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import java.util.function.Consumer;

import org.opentest4j.MultipleFailuresError;

/**
 * <p>
 * Suppose we have a test case and in it we'd like to make numerous BDD assertions. In this case, we're hosting a dinner
 * party and we want to ensure not only that all our guests survive but also that nothing in the mansion has been unduly
 * disturbed:
 * <pre><code class='java'> &#064;Test
 * public void host_dinner_party_where_nobody_dies() {
 *   Mansion mansion = new Mansion();
 *   mansion.hostPotentiallyMurderousDinnerParty();
 *   then(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *   then(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *   then(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *   then(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *   then(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *   then(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *   then(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 * }</code></pre>
 *
 * <p>
 * After running the test, JUnit provides us with the following exception message:
 * <pre><code class='java'> org.junit.ComparisonFailure: [Living Guests] expected:&lt;[7]&gt; but was:&lt;[6]&gt;</code></pre>
 *
 * <p>
 * Oh no! A guest has been murdered! But where, how, and by whom?
 * </p>
 *
 * <p>
 * Unfortunately frameworks like JUnit halt the test upon the first failed assertion. Therefore, to collect more
 * evidence, we'll have to rerun the test (perhaps after attaching a debugger or modifying the test to skip past the
 * first assertion). Given that hosting dinner parties takes a long time, this seems rather inefficient.
 * </p>
 *
 * <p>
 * Instead let's change the test so that at its completion we get the result of all assertions at once. We can do that
 * by using a BDDSoftAssertions instance instead of the static methods on {@link BDDAssertions} as follows:
 * <pre><code class='java'> &#064;Test
 * public void host_dinner_party_where_nobody_dies() {
 *   Mansion mansion = new Mansion();
 *   mansion.hostPotentiallyMurderousDinnerParty();
 *   BDDSoftAssertions softly = new BDDSoftAssertions();
 *   softly.then(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
 *   softly.then(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
 *   softly.then(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
 *   softly.then(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
 *   softly.then(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
 *   softly.then(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
 *   softly.then(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
 *   softly.assertAll();
 * } </code></pre>
 *
 * <p>
 * Now upon running the test our JUnit exception message is far more detailed:
 * <pre><code class='java'> org.assertj.core.api.SoftAssertionError: The following 4 assertions failed:
 * 1) [Living Guests] expected:&lt;[7]&gt; but was:&lt;[6]&gt;
 * 2) [Library] expected:&lt;'[clean]'&gt; but was:&lt;'[messy]'&gt;
 * 3) [Candlestick] expected:&lt;'[pristine]'&gt; but was:&lt;'[bent]'&gt;
 * 4) [Professor] expected:&lt;'[well kempt]'&gt; but was:&lt;'[bloodied and disheveled]'&gt;</code></pre>
 *
 * <p>
 * Aha! It appears that perhaps the Professor used the candlestick to perform the nefarious deed in the library. We
 * should let the police take it from here.
 * </p>
 *
 * <p>
 * BDDSoftAssertions works by providing you with proxies of the AssertJ assertion objects (those created by
 * {@link BDDAssertions}#then...) whose assertion failures are caught and stored. Only when you call
 * {@link BDDSoftAssertions#assertAll()} will a {@link SoftAssertionError} be thrown containing the error messages of
 * those previously caught assertion failures.
 * </p>
 *
 * <p>
 * Note that because BDDSoftAssertions is stateful you should use a new instance of BDDSoftAssertions per test method.
 * Also, if you forget to call assertAll() at the end of your test, the test <strong>will pass</strong> even if any
 * assertion objects threw exceptions (because they're proxied, remember?). So don't forget. You might use
 * {@link JUnitBDDSoftAssertions} or {@link AutoCloseableBDDSoftAssertions} to get assertAll() to be called
 * automatically.
 * </p>
 *
 * <p>
 * It is recommended to use {@link AbstractAssert#as(String, Object...)} so that the multiple failed assertions can be
 * easily distinguished from one another.
 *
 * @author Brian Laframboise
 *
 * @see <a href="http://beust.com/weblog/2012/07/29/reinventing-assertions/">Reinventing Assertions (inspired this feature)</a>
 */
public class BDDSoftAssertions extends AbstractSoftAssertions implements BDDSoftAssertionsProvider {
  /**
   * Convenience method for calling {@link SoftAssertionsProvider#assertSoftly} for these assertion types.
   * Equivalent to {@code SoftAssertion.assertSoftly(BDDSoftAssertions.class, softly)}.
   *
   * @param softly the Consumer containing the code that will make the soft assertions.
   *     Takes one parameter (the actual BDDSoftAssertions instance used to make the assertions).
   * @throws MultipleFailuresError if possible or SoftAssertionError if any proxied assertion objects threw an {@link AssertionError}
   * @since 3.16.0
   */
  public static void thenSoftly(Consumer<BDDSoftAssertions> softly) {
    SoftAssertionsProvider.assertSoftly(BDDSoftAssertions.class, softly);
  }

}
