/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.util.Lists.list;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

class Author {

  String name;

  static final Comparator<Author> AUTHOR_COMPARATOR = Comparator.nullsLast(Comparator.comparing(Author::getName));

  public Author(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  static SortedSet<Author> authorsTreeSet(Author... authors) {
    TreeSet<Author> set = new TreeSet<>(AUTHOR_COMPARATOR);
    set.addAll(list(authors));
    return set;
  }

  // equals not overridden on purpose!

  @Override
  public String toString() {
    return String.format("Author [name=%s]", name);
  }

}