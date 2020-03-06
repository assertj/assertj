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
package org.assertj.core.extractor;

import java.util.function.Function;

import org.assertj.core.util.introspection.MethodSupport;

/**
 * 
 * Extractor for extracting data by a method name.
 * 
 * @author Michał Piotrkowski
 * @author Mateusz Haligowski
 */
class ResultOfExtractor implements Function<Object, Object> {

  private final String methodName;

  ResultOfExtractor(String methodName) {
    this.methodName = methodName;
  }

  /**
   * Behavior is described in {@link MethodSupport#methodResultFor(Object, String)}
   */
  @Override
  public Object apply(Object input) {
    return MethodSupport.methodResultFor(input, methodName);
  }

}
