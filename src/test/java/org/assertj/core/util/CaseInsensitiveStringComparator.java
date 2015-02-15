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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import java.util.Comparator;

public class CaseInsensitiveStringComparator implements Comparator<String> {

  public final static CaseInsensitiveStringComparator instance = new CaseInsensitiveStringComparator();

  @Override
  public int compare(String s1, String s2) {

    return s1.toLowerCase().compareTo(s2.toLowerCase());
  }
}