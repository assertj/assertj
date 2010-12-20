/*
 * Created on Sep 10, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.formatting;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectToStringRule#canHandle(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class ObjectToStringRule_canHandle_Test {

  private static ObjectToStringRule rule;

  @BeforeClass public static void setUpOnce() {
    rule = new ObjectToStringRule();
  }

  @Test public void should_always_return_true() {
    assertTrue(rule.canHandle(new Object()));
  }
}
