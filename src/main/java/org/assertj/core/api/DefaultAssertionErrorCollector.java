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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Throwables;

public class DefaultAssertionErrorCollector implements AssertionErrorCollector {

  // Marking this field as volatile doesn't ensure complete thread safety
  // (mutual exclusion, race-free behaviour), but guarantees eventual visibility
  private volatile boolean wasSuccess = true;
  private List<AssertionError> collectedAssertionErrors = synchronizedList(new ArrayList<>());

  private AfterAssertionErrorCollected callback = this;

  private AssertionErrorCollector delegate = null;

  public DefaultAssertionErrorCollector() {
    super();
  }

  // I think ideally, this would be set in the constructor and made final;
  // however that would require a new constructor that would not make it
  // backward compatible with existing SoftAssertionProvider implementations.
  @Override
  public void setDelegate(AssertionErrorCollector delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<AssertionErrorCollector> getDelegate() {
    return Optional.ofNullable(delegate);
  }

  @Override
  public void collectAssertionError(AssertionError error) {
    if (delegate == null) {
      collectedAssertionErrors.add(error);
      wasSuccess = false;
    } else {
      delegate.collectAssertionError(error);
    }
    callback.onAssertionErrorCollected(error);
  }

  /**
   * Returns a list of soft assertions collected errors. If a delegate
   * has been set (see {@link #setDelegate(AssertionErrorCollector) setDelegate()},
   * then this method will return the result of the delegate's {@code assertErrorsCollected()}.
   *
   * @return A list of soft assertions collected errors.
   */
  @Override
  public List<AssertionError> assertionErrorsCollected() {
    List<AssertionError> errors = delegate != null ? delegate.assertionErrorsCollected()
        : unmodifiableList(collectedAssertionErrors);
    return decorateErrorsCollected(errors);
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
    if (delegate == null) {
      wasSuccess = true;
    } else {
      delegate.succeeded();
    }
  }

  @Override
  public boolean wasSuccess() {
    return delegate == null ? wasSuccess : delegate.wasSuccess();
  }

  /**
   * Modifies collected errors. Override to customize modification.
   * @param <T> the supertype to use in the list return value
   * @param errors list of errors to decorate
   * @return decorated list
  */
  protected <T extends Throwable> List<T> decorateErrorsCollected(List<? extends T> errors) {
    return Throwables.addLineNumberToErrorMessages(errors);
  }

}
