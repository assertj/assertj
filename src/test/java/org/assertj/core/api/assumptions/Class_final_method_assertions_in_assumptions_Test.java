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
package org.assertj.core.api.assumptions;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.assumptionRunner;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest.AnnotatedClass;
import org.assertj.core.api.ClassAssertBaseTest.AnotherAnnotation;
import org.assertj.core.api.ClassAssertBaseTest.MyAnnotation;
import org.assertj.core.api.ProxyableClassAssert;
import org.assertj.core.util.VisibleForTesting;

import java.util.stream.Stream;

/**
 * verify that assertions final methods in {@link ClassAssert} work with assumptions (i.e. that they are proxied correctly in {@link ProxyableClassAssert}).
 */
public class Class_final_method_assertions_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  public static Stream<AssumptionRunner<?>> provideAssumptionsRunners() {
    return Stream.of(
        assumptionRunner(AnnotatedClass.class,
            value -> assumeThat(value).hasAnnotations(MyAnnotation.class, AnotherAnnotation.class),
            value -> assumeThat(value).hasAnnotations(SafeVarargs.class, VisibleForTesting.class))
    );
  }
}
