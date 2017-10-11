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
package org.assertj.core.description;

import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Objects.hashCodeFor;
import static org.assertj.core.util.Strings.formatIfArgs;

import org.assertj.core.util.Arrays;
import org.assertj.core.util.VisibleForTesting;


/**
 * A text-based description.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author William Delanoue
 */
public class TextDescription extends Description {
  @VisibleForTesting
  final String value;

  final Object[] args;

  /**
   * Creates a new <code>{@link TextDescription}</code>.
   *
   * @param value the value of this description.
   * @param args the replacements parameters of this description.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public TextDescription(String value, Object... args) {
    this.value = value == null ? "" : value;
    this.args = Arrays.isNullOrEmpty(args) ? null : args.clone();
  }

  @Override
  public String value() {
    return formatIfArgs(value, args);
  }

  @Override
  public int hashCode() {
    return HASH_CODE_PRIME + hashCodeFor(value) + hashCodeFor(args);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TextDescription other = (TextDescription) obj;
    return areEqual(value, other.value) && areEqual(args, other.args);
  }
}
