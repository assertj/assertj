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
package org.assertj.core.internal.objects;

import java.util.Comparator;
import java.util.Date;

public class SymmetricDateComparator implements Comparator<Date> {

  public static final SymmetricDateComparator SYMMETRIC_DATE_COMPARATOR = new SymmetricDateComparator();

  @Override
  public int compare(Date date1, Date date2) {
    return date1.equals(date2) || date2.equals(date1) ? 0 : date1.compareTo(date2);
  }
}