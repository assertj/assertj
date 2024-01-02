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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions useDefaultDateFormatsOnly method")
class EntryPointAssertions_useDefaultDateFormatsOnly_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("useDefaultDateFormatsOnlyFunctions")
  void should_set_default_DefaultDateFormatsOnly(Pair<Consumer<String>, Runnable> params) {
    // GIVEN
    params.getLeft().accept("yyyyddMM");
    then(AbstractDateAssert.userDateFormats.get()).hasSize(1);
    // WHEN
    params.getRight().run();
    // THEN
    then(AbstractDateAssert.userDateFormats.get()).isEmpty();
  }

  private static Stream<Pair<Consumer<String>, Runnable>> useDefaultDateFormatsOnlyFunctions() {
    return Stream.of(Pair.of(Assertions::registerCustomDateFormat, () -> Assertions.useDefaultDateFormatsOnly()),
                     Pair.of(BDDAssertions::registerCustomDateFormat, () -> BDDAssertions.useDefaultDateFormatsOnly()),
                     Pair.of(withAssertions::registerCustomDateFormat, () -> withAssertions.useDefaultDateFormatsOnly()));
  }
}
