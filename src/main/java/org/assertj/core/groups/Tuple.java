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
package org.assertj.core.groups;

import static java.util.Collections.addAll;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Objects.areEqual;

import java.util.List;

public class Tuple {

  private final List<Object> datas = newArrayList();

  public Tuple(Object... values) {
	addAll(datas, values);
  }

  public Object[] toArray() {
	return datas.toArray();
  }

  public List<Object> toList() {
    return datas;
  }

  @Override
  public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + datas.hashCode();
	return result;
  }

  @Override
  public boolean equals(Object obj) {
	if (this == obj) return true;
	if (obj == null) return false;
    if (!(obj instanceof Tuple)) return false;
	Tuple other = (Tuple) obj;
	// datas can't be null
    return areEqual(datas.toArray(), other.datas.toArray());
  }

  @Override
  public String toString() {
    return CONFIGURATION_PROVIDER.representation().toStringOf(this);
  }

  public static Tuple tuple(Object... values) {
	return new Tuple(values);
  }

}
