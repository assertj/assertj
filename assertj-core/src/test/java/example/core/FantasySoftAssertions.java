/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

// tag::user_guide[]
// we extend SoftAssertions to get standard soft assertions
public class FantasySoftAssertions extends SoftAssertions
                                   implements TolkienSoftAssertionsProvider, GoTSoftAssertionsProvider {

  // we can even add more assertions here
  public HumanAssert assertThat(Human actual) {
    return proxy(HumanAssert.class, Human.class, actual);
  }
  // end::user_guide[]

  public static class Human {
  }

  public static class HumanAssert extends AbstractAssert<HumanAssert, Human> {

    public HumanAssert(Human actual) {
      super(actual, HumanAssert.class);
    }
  }
  // tag::user_guide[]
}
// end::user_guide[]
// @formatter:on
