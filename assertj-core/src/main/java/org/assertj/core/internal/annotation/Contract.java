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
package org.assertj.core.internal.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Specifies some aspects of the annotated method's behavior to be used by tools
 * for data flow analysis.
 *
 * @see <a href="https://javadoc.io/static/org.jetbrains/annotations/26.0.2/org/jetbrains/annotations/Contract.html">org.jetbrains.annotations.Contract</a>
 * @see <a href="https://github.com/uber/NullAway/wiki/Configuration#custom-contract-annotations">NullAway custom contract annotations</a>
 * @since 3.27.4
 */
@Documented
@Target(ElementType.METHOD)
public @interface Contract {

  /**
   * Contains the contract clauses describing causal relations between call
   * arguments and the returned value.
   */
  String value();

}
