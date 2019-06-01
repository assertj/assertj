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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import java.util.List;

import org.assertj.core.error.AssertionErrorCreator;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

/**
 * Same as {@link SoftAssertions}, but with the following differences: <br>
 * First, it's a JUnit Jupiter extension, which can be used without having to call
 * {@link SoftAssertions#assertAll() assertAll()}, example:
 * <pre><code class='java'> public class SoftlyTest {
 *
 *     &#064;RegisterExtension
 *     public final JUnitJupiterSoftAssertions softly = new JUnitJupiterSoftAssertions();
 *
 *     &#064;Test
 *     public void testSoftly() throws Exception {
 *       softly.assertThat(1).isEqualTo(2);
 *       softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
 *     }
 *  }</code></pre>
 *
 * Second, the failures are recognized by IDE's (like IntelliJ IDEA) which open a comparison window.
 */
public class JUnitJupiterSoftAssertions extends AbstractStandardSoftAssertions implements BeforeEachCallback, AfterEachCallback {

  static final Namespace ASSERTJ_JUNIT_EXTENSION_NAMESPACE = Namespace.create("AssertJ");
  private static final String SOFT_PROXIES_KEY = "JUnitJupiterSoftAssertions SoftProxies";
  private final AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();
  private Store store;

  // is used by errorsCollected() which gets the errors from the SoftProxies instance in use when the tests ran.
  @Override
  protected SoftProxies getProxies() {
    return store.get(SOFT_PROXIES_KEY, SoftProxies.class);
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    List<Throwable> errors = errorsCollected();
    if (!errors.isEmpty()) assertionErrorCreator.tryThrowingMultipleFailuresError(errors);
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    store = extensionContext.getStore(ASSERTJ_JUNIT_EXTENSION_NAMESPACE);
    store.put(SOFT_PROXIES_KEY, new SoftProxies());
  }
}
