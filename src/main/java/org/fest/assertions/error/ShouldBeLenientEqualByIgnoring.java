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
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are lenient equal by ignoring fields
 * failed.
 * 
 * @author Nicolas François
 */
public class ShouldBeLenientEqualByIgnoring extends BasicErrorMessageFactory {

	  /**
	   * Creates a new </code>{@link ShouldBeLenientEqualByIgnoring}</code>.
	   * @param actual the actual value in the failed assertion.
	   * @param rejectedFields fields name not matching
	   * @param rejectedValues fields value not matching
	   * @param ignoredFields fields which are not base the lenient equality
	   * @return the created {@code ErrorMessageFactory}.
	   */
	  public static <E> ErrorMessageFactory shouldBeLenientEqualByIgnoring(Object actual, List<String> rejectedFields, List<Object> rejectedValues, List<String> ignoredFields) {
		if(rejectedFields.size() == 1){
			if(ignoredFields.isEmpty()){
				return new ShouldBeLenientEqualByIgnoring(actual, rejectedFields.get(0), rejectedValues.get(0));
			} else {
				return new ShouldBeLenientEqualByIgnoring(actual, rejectedFields.get(0), rejectedValues.get(0), ignoredFields);
			}
		} else {
			if(ignoredFields.isEmpty()){
				return new ShouldBeLenientEqualByIgnoring(actual, rejectedFields, rejectedValues);
			} else {
				return new ShouldBeLenientEqualByIgnoring(actual, rejectedFields, rejectedValues, ignoredFields);
			}
		}
	  }

  
	  private ShouldBeLenientEqualByIgnoring(Object actual, List<String> rejectedFields, List<Object> rejectedValue, List<String> ignoredFields) {
		  super("expected values <%s> in fields <%s> of <%s>, comparison was performed on all fields but <%s>", rejectedValue, rejectedFields, actual, ignoredFields);
	  }
	  
	  private ShouldBeLenientEqualByIgnoring(Object actual, String rejectedField, Object rejectedValue, List<String> ignoredFields) {
		  super("expected value <%s> in field <%s> of <%s>, comparison was performed on all fields but <%s>", rejectedValue, rejectedField, actual, ignoredFields);
	  }
	  
	  private ShouldBeLenientEqualByIgnoring(Object actual, List<String> rejectedFields, List<Object> rejectedValue) {
		  super("expected values <%s> in fields <%s> of <%s>, comparison was performed on all fields", rejectedValue, rejectedFields, actual);
	  }
	  
	  private ShouldBeLenientEqualByIgnoring(Object actual, String rejectedField, Object rejectedValue) {
		  super("expected value <%s> in field <%s> of <%s>, comparison was performed on all fields", rejectedValue, rejectedField, actual);
	  }
	  
}
