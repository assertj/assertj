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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static org.assertj.core.util.Arrays.array;

import java.util.function.Consumer;

class BaseAssumptionRunner<T> extends AssumptionRunner<T> {

  protected final T actual;
  private Consumer<T> failingAssumption;
  private Consumer<T> passingAssumption;

  BaseAssumptionRunner() {
    this.actual = null;
  }

  static <T> BaseAssumptionRunner<T> assumptionRunner(T actual, Consumer<T> passingAssumption,
                                                      Consumer<T> failingAssumption) {
    return new BaseAssumptionRunner<>(actual, passingAssumption, failingAssumption);
  }

  static <T> AssumptionRunner<T>[] run(T actual, Consumer<T> passingAssumption, Consumer<T> failingAssumption) {
    return array(new BaseAssumptionRunner<>(actual, passingAssumption, failingAssumption));
  }

  BaseAssumptionRunner(T actual, Consumer<T> passingAssumption, Consumer<T> failingAssumption) {
    this.actual = actual;
    this.passingAssumption = passingAssumption;
    this.failingAssumption = failingAssumption;
  }

  @Override
  protected void runFailingAssumption() {
    failingAssumption.accept(actual);
  }

  @Override
  protected void runPassingAssumption() {
    passingAssumption.accept(actual);
  }

}
