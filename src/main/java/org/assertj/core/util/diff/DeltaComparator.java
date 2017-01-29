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
package org.assertj.core.util.diff;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Copy from https://code.google.com/p/java-diff-utils/.
 * <p>
 * @author mksenzov
 */
public class DeltaComparator implements Comparator<Delta<?>>, Serializable {
  private static final long serialVersionUID = 1L;
  public static final Comparator<Delta<?>> INSTANCE = new DeltaComparator();

  private DeltaComparator() {}

  public int compare(final Delta<?> a, final Delta<?> b) {
    final int posA = a.getOriginal().getPosition();
    final int posB = b.getOriginal().getPosition();
    if (posA > posB) {
      return 1;
    } else if (posA < posB) {
      return -1;
    }
    return 0;
  }
}