/*
 * Created on Jul 21, 2012
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
package org.fest.assertions.api;

import static org.fest.assertions.test.ExpectedException.none;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;

import org.fest.assertions.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link FileAssert#usingCharset(String)}</code> and <code>{@link FileAssert#usingCharset(Charset)}</code>.
 * 
 * @author Olivier Michallat
 */
public class FileAssert_usingCharset_Test {

  @Rule
  public ExpectedException thrown = none();

  private FileAssert assertions;
  private Charset defaultCharset;
  private Charset otherCharset;

  @Before
  public void setUp() {
    assertions = new FileAssert(new File("abc"));
    defaultCharset = Charset.defaultCharset();
    for (Charset charset : Charset.availableCharsets().values()) {
      if (!charset.equals(defaultCharset)) otherCharset = charset;
    }
  }

  @Test
  public void should_use_default_charset_if_none_provided() {
    assertEquals(defaultCharset, assertions.charset);
  }

  @Test
  public void should_use_provided_charset() {
    assertions.usingCharset(otherCharset);
    assertEquals(otherCharset, assertions.charset);
  }
  
  @Test
  public void should_use_provided_charset_name() {
    assertions.usingCharset(otherCharset.name());
    assertEquals(otherCharset, assertions.charset);
  }
  
  @Test
  public void should_throw_exception_for_null_charset() {
    thrown.expectNullPointerException("The charset should not be null");
    assertions.usingCharset((Charset) null);
  }

  @Test
  public void should_throw_exception_for_invalid_charset_name() {
    thrown.expectIllegalArgumentException("Charset:<'Klingon'> is not supported on this system");
    assertions.usingCharset("Klingon");
  }
}
