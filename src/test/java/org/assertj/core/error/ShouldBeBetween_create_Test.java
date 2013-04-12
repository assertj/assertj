/*
 * Created on Sep 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.util.Dates.parse;

import org.assertj.core.description.*;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeBetween;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeBetween#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeBetween_create_Test {

  @Test
  public void should_create_error_message_with_period_boundaries_included() {
    ErrorMessageFactory factory = shouldBeBetween(parse("2010-01-01"), parse("2011-01-01"), parse("2012-01-01"), true, true);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <2010-01-01T00:00:00>\nto be in period:\n [2011-01-01T00:00:00, 2012-01-01T00:00:00]", message);
  }

  @Test
  public void should_create_error_message_with_period_lower_boundary_included() {
    ErrorMessageFactory factory = shouldBeBetween(parse("2010-01-01"), parse("2011-01-01"), parse("2012-01-01"), true, false);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <2010-01-01T00:00:00>\nto be in period:\n [2011-01-01T00:00:00, 2012-01-01T00:00:00[", message);
  }

  @Test
  public void should_create_error_message_with_period_upper_boundary_included() {
    ErrorMessageFactory factory = shouldBeBetween(parse("2010-01-01"), parse("2011-01-01"), parse("2012-01-01"), false, true);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <2010-01-01T00:00:00>\nto be in period:\n ]2011-01-01T00:00:00, 2012-01-01T00:00:00]", message);
  }

  @Test
  public void should_create_error_message_with_period_boundaries_excluded() {
    ErrorMessageFactory factory = shouldBeBetween(parse("2010-01-01"), parse("2011-01-01"), parse("2012-01-01"), false, false);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <2010-01-01T00:00:00>\nto be in period:\n ]2011-01-01T00:00:00, 2012-01-01T00:00:00[", message);
  }
}
