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

import static org.assertj.core.api.AbstractAssert.customRepresentation;
import static org.assertj.core.api.AbstractAssert.descriptionConsumer;
import static org.assertj.core.api.AbstractAssert.printAssertionsDescription;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;

import org.assertj.core.description.Description;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.CheckReturnValue;

public abstract class AbstractThrowingExecutableAssert<SELF extends AbstractThrowingExecutableAssert<SELF, ACTUAL>, ACTUAL>
    implements ThrowingExecutableAssert<SELF, ACTUAL> {

  protected final SELF myself;

  protected WritableAssertionInfo info;
  private Throwable thrown;

  @SuppressWarnings("unchecked")
  protected AbstractThrowingExecutableAssert(ACTUAL actual, Class<?> selfType) {
    this.myself = (SELF) selfType.cast(this);
    this.info = new WritableAssertionInfo(customRepresentation);

    try {
      execute(actual);
    } catch (Throwable e) {
      this.thrown = e;
    }
  }

  protected abstract void execute(ACTUAL actual) throws Throwable;

  @Override
  @CheckReturnValue
  public SELF describedAs(Description description) {
    info.description(description);
    if (printAssertionsDescription) printDescriptionText();
    if (descriptionConsumer != null) descriptionConsumer.accept(description);
    return myself;
  }

  private void printDescriptionText() {
    String descriptionText = info.descriptionText();
    if (!descriptionText.isEmpty()) System.out.println(descriptionText);
  }

  public SELF doesNotThrowAnyException() {
    if (thrown != null) throw Failures.instance().failure(info, shouldNotHaveThrown(thrown));
    return myself;
  }

  public ThrowableAssertAlternative<Throwable> hasThrownException() {
    if (thrown == null) throw Failures.instance().expectedThrowableNotThrown(Throwable.class);
    ThrowableAssertAlternative<Throwable> newAssert = new ThrowableAssertAlternative<>(thrown);
    newAssert.propagateAssertionInfoFrom(info);
    return newAssert;
  }

}
