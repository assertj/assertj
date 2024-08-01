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

import static org.assertj.core.api.SoftAssertionsStatement.softAssertionsStatement;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @see JUnitSoftAssertions
 *
 * @since 2.5.0 / 3.5.0
 *
 * @deprecated For Android compatible assertions use the latest assertj 2.x version which is based on Java 7 only.
 * <p>
 * JUnitSoftAssertions rule compatible with Android. Duplicated from {@link JUnitSoftAssertions}.
 */
@Deprecated
public class Java6JUnitSoftAssertions extends AbstractSoftAssertions
    implements Java6StandardSoftAssertionsProvider, SoftAssertionsRule {

  @Override
  public Statement apply(final Statement base, Description description) {
    return softAssertionsStatement(this, base);
  }
}
