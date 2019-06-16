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

module org.assertj.core {
  exports org.assertj.core.annotations;
  exports org.assertj.core.api;
  exports org.assertj.core.api.exception;
  exports org.assertj.core.api.filter;
  exports org.assertj.core.api.iterable;
  exports org.assertj.core.api.recursive.comparison;
  exports org.assertj.core.condition;
  exports org.assertj.core.configuration;
  exports org.assertj.core.data;
  exports org.assertj.core.description;
  exports org.assertj.core.error;
  exports org.assertj.core.error.future;
  exports org.assertj.core.error.uri;
  exports org.assertj.core.extractor;
  exports org.assertj.core.groups;
  exports org.assertj.core.internal;
  exports org.assertj.core.matcher;
  exports org.assertj.core.presentation;
  exports org.assertj.core.util;
  exports org.assertj.core.util.diff;
  exports org.assertj.core.util.diff.myers;
  exports org.assertj.core.util.introspection;
  exports org.assertj.core.util.xml;
  requires java.instrument;
  // required when printThreadDump is true
  requires static java.management;
  // used for pretty print XML
  requires static java.xml;
  // For annotations: Beta and NonNull ones for Kotlin
  requires static jsr305;
  // JUnit/Hamcrest specific features
  requires static hamcrest.core;
  requires static junit;
  requires static org.junit.jupiter.api;
  // To throw AssertionFailedError which is IDE friendly
  requires static org.opentest4j;
}
