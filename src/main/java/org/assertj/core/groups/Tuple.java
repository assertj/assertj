/*
 * Created on Jan 28, 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this Throwable except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.groups;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Collections;

public class Tuple {

  private static final String END = ")";
  private static final String START = "(";
  private List<Object> datas = newArrayList();

  public Tuple(Object... values) {
    for (Object value : values) {
      datas.add(value);
    }
  }

  public void addData(Object data) {
    datas.add(data);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((datas == null) ? 0 : datas.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Tuple other = (Tuple) obj;
    if (datas == null) {
      if (other.datas != null) return false;
    } else if (!datas.equals(other.datas)) return false;
    return true;
  }

  @Override
  public String toString() {
    return Collections.format(datas, START, END);
  }

  public static List<Tuple> buildTuples(int n) {
    List<Tuple> tuples = new ArrayList<Tuple>(n);
    for (int i = 0; i < n; i++) {
      tuples.add(new Tuple());
    }
    return tuples;
  }

  public static Tuple tuple(Object... values) {
    return new Tuple(values);
  }

}
