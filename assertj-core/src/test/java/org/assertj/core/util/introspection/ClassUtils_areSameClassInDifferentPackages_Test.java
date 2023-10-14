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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.util.introspection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ClassUtils_areSameClassInDifferentPackages_Test {

  @Test
  void areClassesWithSameNameInDifferentPackages() {
    // WHEN
    boolean areSameClassInDifferentPackages = ClassUtils.areClassesWithSameNameInDifferentPackages(java.util.Date.class,
                                                                                                   java.sql.Date.class);
    // THEN
    then(areSameClassInDifferentPackages).isTrue();
  }

  @Test
  void areClassesWithSameNameInDifferentPackagesForSameClass() {
    // WHEN
    boolean areSameClassInDifferentPackages = ClassUtils.areClassesWithSameNameInDifferentPackages(java.util.Date.class,
                                                                                                   java.util.Date.class);
    // THEN
    then(areSameClassInDifferentPackages).isFalse();
  }

  @Test
  void areNotClassesWithSameNameInDifferentPackages() {
    // WHEN
    boolean areSameClassInDifferentPackages = ClassUtils.areClassesWithSameNameInDifferentPackages(java.util.Date.class,
                                                                                                   java.text.DateFormat.class);
    // THEN
    then(areSameClassInDifferentPackages).isFalse();
  }
}
