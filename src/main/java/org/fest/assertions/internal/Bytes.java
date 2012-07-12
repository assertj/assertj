/*
 * Created on Oct 21, 2010
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

import org.fest.util.*;

/**
 * Reusable assertions for <code>{@link Byte}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes extends Numbers<Byte> {

  private static final Bytes INSTANCE = new Bytes();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Bytes instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Bytes() {
    super();
  }

  public Bytes(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Byte zero() {
    return 0;
  }

}
