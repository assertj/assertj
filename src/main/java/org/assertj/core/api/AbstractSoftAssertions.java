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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

public class AbstractSoftAssertions {

  protected final SoftProxies proxies;

  public AbstractSoftAssertions() {
    proxies = new SoftProxies();
  }

  public <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
    return proxies.create(assertClass, actualClass, actual);
  }

  /**
   * Returns a copy of list of soft assertions collected errors.
   * @return a copy of list of soft assertions collected errors.
   */
  public List<Throwable> errorsCollected(){
    return newArrayList(proxies.errorsCollected());
  }

  /**
   * Returns the result of last soft assertion which can used to decide what the next one should be.
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
  public boolean wasSuccess(){
    return proxies.wasSuccess();
  }
}
