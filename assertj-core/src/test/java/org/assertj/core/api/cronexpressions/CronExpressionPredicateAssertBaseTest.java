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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.cronexpressions;

import org.assertj.core.api.BaseTestTemplate;
import org.assertj.core.api.CronExpressionAssert;
import org.assertj.core.api.CronExpressionPredicate;
import org.assertj.core.api.CronExpressionPredicateAssert;
import org.assertj.core.internal.CronExpression;
import org.assertj.core.internal.Iterables;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Base class for {@link org.assertj.core.api.CronExpressionAssert} tests.
 * @author Neil Wang
 */
public abstract class CronExpressionPredicateAssertBaseTest extends BaseTestTemplate<CronExpressionPredicateAssert, CronExpressionPredicate> {

  protected Iterables iterables;
  protected Predicate<Long> wrapped;

  @Override
  protected CronExpressionPredicateAssert create_assertions() {
    return new CronExpressionPredicateAssert(Objects::nonNull);
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
  }
}
