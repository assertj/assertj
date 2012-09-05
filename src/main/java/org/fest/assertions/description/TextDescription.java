/*
 * Created on Jul 20, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.description;

import static org.fest.util.Objects.*;
import static org.fest.util.Preconditions.checkNotNull;

import org.fest.util.VisibleForTesting;

/**
 * A text-based description.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class TextDescription extends Description {
  @VisibleForTesting
  final String value;

  /**
   * Creates a new </code>{@link TextDescription}</code>.
   * 
   * @param value the value of this description.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public TextDescription(String value) {
    checkNotNull(value);
    this.value = value;
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public int hashCode() {
    return HASH_CODE_PRIME * 1 + hashCodeFor(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TextDescription other = (TextDescription) obj;
    return areEqual(value, other.value);
  }
}
