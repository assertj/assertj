/*
 * Created on Oct 18, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Integer}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Integers extends Numbers<Integer> {

  private static final Integers INSTANCE = new Integers();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Integers instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Integers() {
    super();
  }

  public Integers(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Integer zero() {
    return 0;
  }
}
