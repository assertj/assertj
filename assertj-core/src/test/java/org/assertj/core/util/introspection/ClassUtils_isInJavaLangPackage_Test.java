/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.introspection.ClassUtils.isInJavaLangPackage;

import java.lang.ref.SoftReference;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClassUtils_isInJavaLangPackage_Test {

  @ParameterizedTest
  @ValueSource(classes = { String.class, Object.class, SoftReference.class })
  void should_detect_java_lang_types(Class<?> clazz) {
    // WHEN
    boolean isInJavaLangPackage = isInJavaLangPackage(clazz);
    // THEN
    then(isInJavaLangPackage).isTrue();
  }

  @ParameterizedTest
  @ValueSource(classes = { List.class, Date.class })
  void should_detect_non_java_lang_types(Class<?> clazz) {
    // WHEN
    boolean isInJavaLangPackage = isInJavaLangPackage(clazz);
    // THEN
    then(isInJavaLangPackage).isFalse();
  }
}
