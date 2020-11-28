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

@FunctionalInterface
public interface AfterAssertionErrorCollected {

  /**
   * This method is called <b>after</b> each {@link AssertionError} is collected by soft assertions
   * (you can't prevent the error to be collected).
   * <p>
   * {@link SoftAssertionsProvider} provides a do-nothing implementation which you can override in your own
   * custom soft assertions class.<br>
   * Note that your custom soft assertions class must extend {@link AbstractSoftAssertions} as this is where the actual soft
   * assertion errors collection is implemented.
   * <p>
   * If you just use the standard soft assertions classes provided by AssertJ, you can register your callback with
   * {@link AbstractSoftAssertions#setAfterAssertionErrorCollected(AfterAssertionErrorCollected)}.
   * <p>
   * Example with custom soft assertions:
   * <pre><code class='java'> class TolkienSoftAssertions extends SoftAssertions {
   *
   *   public TolkienHeroesAssert assertThat(TolkienHero actual) {
   *     return proxy(TolkienHeroesAssert.class, TolkienHero.class, actual);
   *   }
   *
   *   {@literal @}Override
   *   public void onAssertionErrorCollected(AssertionError assertionError) {
   *     System.out.println(assertionError);
   *   }
   * }
   *
   * TolkienSoftAssertions softly = new TolkienSoftAssertions();
   *
   * TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
   *
   * // the AssertionError corresponding to this failing assertion is printed to the console.
   * softly.assertThat(frodo).hasName("Bilbo");</code></pre>
   * <p>
   * Example with standard soft assertions:
   * <pre><code class='java'> SoftAssertions softly = new SoftAssertions();
   *
   * // register our callback
   * softly.setAfterAssertionErrorCollected(error -&gt; System.out.println(error));
   *
   * // the AssertionError corresponding to this failing assertion is printed to the console.
   * softly.assertThat("The Beatles").isEqualTo("The Rolling Stones");</code></pre>
   *
   * @param assertionError the collected {@link AssertionError}.
   */
  void onAssertionErrorCollected(AssertionError assertionError);

}
