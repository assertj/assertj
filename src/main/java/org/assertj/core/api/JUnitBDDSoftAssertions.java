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

/**
 * Same as {@link SoftAssertions}, but with the following differences: <br>
 * First, it's a junit rule, which can be used without having to call {@link SoftAssertions#assertAll() assertAll()},
 * example:
 * <pre><code class='java'> public class SoftlyTest {
 *
 *     &#064;Rule
 *     public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();
 *
 *     &#064;Test
 *     public void soft_bdd_assertions() throws Exception {
 *       softly.then(1).isEqualTo(2);
 *       softly.then(Lists.newArrayList(1, 2)).containsOnly(1, 2);
 *     }
 *  }</code></pre>
 *
 * Second, the failures are recognized by IDE's (like IntelliJ IDEA) which open a comparison window.
 */
public class JUnitBDDSoftAssertions extends AbstractSoftAssertions implements BDDSoftAssertionsProvider, SoftAssertionsRule {
}
