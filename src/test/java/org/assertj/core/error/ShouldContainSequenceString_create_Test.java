/*
 * Created on Dec 26, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.error.ShouldContainCharSequenceSequence.shouldContainSequence;

import org.junit.Test;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;

/**
 * Tests for <code>{@link ShouldContainCharSequenceSequence#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldContainSequenceString_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message() {
    String[] sequenceValues = { "{", "author", "title", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";

    factory = shouldContainSequence(actual, sequenceValues, 1);
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <'" + actual + "'>\n"
        + "to contain the following CharSequences in this order:\n"
        + " <['{', 'author', 'title', '}']>\n"
        + "but <'title'> was found before <'author'>\n", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    String[] sequenceValues = { "{", "author", "title", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";

    factory = shouldContainSequence(actual, sequenceValues, 1,
                                    new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <'" + actual + "'>\n"
        + "to contain the following CharSequences in this order:\n"
        + " <['{', 'author', 'title', '}']>\n"
        + "but <'title'> was found before <'author'>\n"
        + "according to 'CaseInsensitiveStringComparator' comparator", message);
  }

}
