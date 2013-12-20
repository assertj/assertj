/*
 * Created on Jan 28, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.SystemProperties.LINE_SEPARATOR;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldHaveEqualContent#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Yvonne Wang
 * @author Matthieu Baechler
 */
public class ShouldHaveEqualContent_create_Test {

  private ErrorMessageFactory factory;
  private List<String> diffs;

  @Before
  public void setUp() {
    diffs = newArrayList("line:<0>, \nExpecting:\n<line0> but was:<line_0>", "line:<1>, \nExpecting:\n<line1> but was:<line_1>",
        "line:<2>, \nExpecting:\n<line2> but was:<line_%s>");
  }

  @Test
  public void should_create_error_message_file_even_if_content_contains_format_specifier() {
    factory = ShouldHaveEqualContent.shouldHaveEqualContent(new FakeFile("abc"), new FakeFile("xyz"), diffs);

    StringBuilder b = new StringBuilder();
    b.append("[Test] \nFile:\n <abc>\nand file:\n <xyz>\ndo not have equal content:");
    for (String diff : diffs)
      b.append(LINE_SEPARATOR).append(diff);
    assertEquals(b.toString(), factory.create(new TextDescription("Test"), new StandardRepresentation()));
  }

  @Test
  public void should_create_error_message_inputstream_even_if_content_contains_format_specifier() {
    factory = ShouldHaveEqualContent.shouldHaveEqualContent(new ByteArrayInputStream(new byte[] { 'a' }),
        new ByteArrayInputStream(new byte[] { 'b' }), diffs);

    StringBuilder b = new StringBuilder();
    b.append("[Test] \nInputStreams do not have equal content:");
    for (String diff : diffs)
      b.append(LINE_SEPARATOR).append(diff);
    assertEquals(b.toString(), factory.create(new TextDescription("Test"), new StandardRepresentation()));
  }

}
