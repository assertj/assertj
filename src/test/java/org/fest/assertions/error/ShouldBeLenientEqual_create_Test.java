/*
 * Created on Mar 19, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.error;

import static org.fest.assertions.error.ShouldBeLenientEqual.shouldBeLenientEqual;
import static org.fest.util.Collections.list;
import static org.junit.Assert.assertEquals;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.fest.assertions.test.Jedi;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeLenientEqual#create(Description)}</code>.
 *
 * @author Nicolas François
 */
public class ShouldBeLenientEqual_create_Test {

	  private ErrorMessageFactory factory;

	  @Test public void should_create_error_message_plurial() {
		  factory = shouldBeLenientEqual(new Jedi("Yoda","green"), list("name", "lightsaberColor"), list((Object)"Yoda", (Object)"green"));
		  String message = factory.create(new TextDescription("Test"));
		  assertEquals("[Test] expected values <['Yoda', 'green']> in fields <['name', 'lightsaberColor']> of <Yoda the Jedi>", message);
	  }	
	 
	  @Test public void should_create_error_message_singular() {
		  factory = shouldBeLenientEqual(new Jedi("Yoda","green"), list("lightsaberColor"), list((Object)"green"));
		  String message = factory.create(new TextDescription("Test"));
		  assertEquals("[Test] expected value <'green'> in field <'lightsaberColor'> of <Yoda the Jedi>", message);
	  }		  
	  
}
