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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssertionErrorCollectorImpl implements AssertionErrorCollector {

  volatile boolean wasSuccess = true;
  List<AssertionError> assertionsCollected = Collections.synchronizedList(new ArrayList<>());
  
  AfterAssertionErrorCollected callback = this;
  
  public AssertionErrorCollectorImpl() {
    super();
  }

  @Override
  public void collectAssertionError(AssertionError error) {
    assertionsCollected.add(error);
    wasSuccess = false;
    callback.onAssertionErrorCollected(error);
  }

  /**
   * Returns a copy of list of soft assertions collected errors.
   * @return a copy of list of soft assertions collected errors.
   */
  @Override
  public List<AssertionError> assertionErrorsCollected() {
    return Collections.unmodifiableList(assertionsCollected);
  }

  /**
   * Register a callback allowing to react after an {@link AssertionError} is collected by the current soft assertion.
   * <p>
   * The callback is an instance of {@link AfterAssertionErrorCollected} which can be expressed as lambda.
   * <p>
   * Example:
   * <pre><code class='java'> SoftAssertions softly = new SoftAssertions();
   * StringBuilder reportBuilder = new StringBuilder(format("Assertions report:%n"));
  
   * // register our callback
   * softly.setAfterAssertionErrorCollected(error -&gt; reportBuilder.append(String.format("------------------%n%s%n", error.getMessage())));
   *
   * // the AssertionError corresponding to the failing assertions are registered in the report
   * softly.assertThat("The Beatles").isEqualTo("The Rolling Stones");
   * softly.assertThat(123).isEqualTo(123)
   *                       .isEqualTo(456);</code></pre>
   * <p>
   * resulting {@code reportBuilder}:
   * <pre><code class='java'> Assertions report:
   * ------------------
   * Expecting:
   *  &lt;"The Beatles"&gt;
   * to be equal to:
   *  &lt;"The Rolling Stones"&gt;
   * but was not.
   * ------------------
   * Expecting:
   *  &lt;123&gt;
   * to be equal to:
   *  &lt;456&gt;
   * but was not.</code></pre>
   * <p>
   * Alternatively, if you have defined your own SoftAssertions subclass and inherited from {@link AbstractSoftAssertions},
   * the only thing you have to do is to override {@link AfterAssertionErrorCollected#onAssertionErrorCollected(AssertionError)}.
   *
   * @param afterAssertionErrorCollected the callback.
   *
   * @since 3.17.0
   */
  public void setAfterAssertionErrorCollected(AfterAssertionErrorCollected afterAssertionErrorCollected) {
    callback = afterAssertionErrorCollected;
  }
  
  @Override
  public void succeeded() {
    wasSuccess = true;
  }
  
  @Override
  public boolean wasSuccess() {
    return wasSuccess;
  }
}