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
open module org.assertj.tests.core {
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires com.google.common;
  requires hamcrest.core;
  requires java.sql;
  requires javax.servlet.api;
  requires junit;
  requires nl.jqno.equalsverifier;
  requires org.apache.commons.lang3;
  requires org.assertj.core;
  requires org.junit.platform.testkit;
  requires org.junit.jupiter.engine;
  requires org.junit.jupiter.params;
  requires org.junitpioneer;
  requires org.mockito;
}
