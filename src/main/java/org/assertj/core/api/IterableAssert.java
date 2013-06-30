/*
 * Created on Jul 26, 2010
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
package org.assertj.core.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Assertion methods for {@link Iterable}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Iterable)}</code>.
 * </p>
 * @param <T> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Matthieu Baechler
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Julien Meddah
 */
public class IterableAssert<T> extends AbstractIterableAssert<IterableAssert<T>, Iterable<T>, T> {

  protected IterableAssert(Iterable<T> actual) {
    super(actual, IterableAssert.class);
  }
  
  protected IterableAssert(Iterator<T> actual) {
    this(toIterable(actual));
  }

  private static <T> Iterable<T> toIterable(Iterator<T> actual) {
    if (actual == null) {
      return null;
    }
    Collection<T> actualList = new ArrayList<T>();
    while(actual.hasNext()) {
      actualList.add(actual.next());
    }
    return actualList;
  }
}
