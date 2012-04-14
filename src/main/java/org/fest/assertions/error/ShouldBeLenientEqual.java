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

import java.util.List;


/**
 * Creates an error message indicating that an assertion that verifies that a value satisfies a
 * 
 * @author Nicolas François
 */
public class ShouldBeLenientEqual extends BasicErrorMessageFactory {

	  /**
	   * Creates a new </code>{@link ShouldBeLenientEqual}</code>.
	   * @param actual the actual value in the failed assertion.
	   * @param rejectedFields fields name not matching
	   * @param rejectedValues fields value not matching
	   * @return the created {@code ErrorMessageFactory}.
	   */
	  public static <E> ErrorMessageFactory shouldBeLenientEqual(Object actual, List<String> rejectedFields, List<Object> rejectedValues) {
		if(rejectedFields.size() == 1){
			return new ShouldBeLenientEqual(actual, rejectedFields.get(0), rejectedValues.get(0));
		} else {
			return new ShouldBeLenientEqual(actual, rejectedFields, rejectedValues);
		}
	  }

  
	  private ShouldBeLenientEqual(Object actual, List<String> rejectedFields, List<Object> rejectedValue) {
		  super("expected values <%s> in fields <%s> of <%s>", rejectedValue, rejectedFields, actual);
	  }
	  
	  private ShouldBeLenientEqual(Object actual, String rejectedField, Object rejectedValue){
		  super("expected value <%s> in field <%s> of <%s>", rejectedValue, rejectedField, actual);
	  }
	  
}
