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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Set;

import org.junit.jupiter.api.Test;

class Module_Test {

  private final Module underTest = ModuleLayer.boot().findModule("org.assertj.core").orElseThrow();

  @Test
  void should_export_non_internal_packages() {
    // WHEN
    Set<String> packages = underTest.getPackages();
    // THEN
    then(packages).filteredOn(p -> !p.contains("internal")).allMatch(underTest::isExported);
  }

}
