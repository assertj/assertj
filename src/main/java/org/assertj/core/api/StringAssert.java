/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;


/**
 * Assertion methods for {@code String}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(String)}</code>.
 * </p>
 * This class has been defined so that, when calling {@link #usingComparator(java.util.Comparator)}, one provide a
 * String comparator instead of a {@link CharSequence} comparator.
 * 
 * @author Mikhail Mazursky
 */
public class StringAssert extends AbstractCharSequenceAssert<StringAssert, String> {

  public StringAssert(String actual) {
    super(actual, StringAssert.class);
  }
}
