/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.presentation;

import org.assertj.core.api.Assertions;

/**
 * Controls the formatting (String representation) of types in assertion error message.
 * <p>
 * There are two ways to replace the default {@link Representation} ({@link StandardRepresentation}):  
 * <ul>
 * <li>call {@link Assertions#useRepresentation(Representation)}, from this point all the assertions will use the given representation</li>
 * <li>register a representation as a service discovered at program startup</li>
 * </ul>
 * <p>
 * The advantage of registering a representation is that you don't need to do anything in your tests, the java runtime will discover it 
 * and AssertJ will use it but it requires a bit more work than a simple call to {@link Assertions#useRepresentation(Representation)}.  
 * <p>
 * To register a {@link Representation}, you need to do several things:
 * <ul>
 * <li>create a file named {@code org.assertj.core.presentation.Representation} file in META-INF/services directory</li>
 * <li>put the fully qualified class name of your {@link Representation} in it</li>   
 * <li>make sure {@code META-INF/services/org.assertj.core.presentation.Representation} is in the runtime classpath, usually putting it in {@code src/test/resources} is enough</li>
 * <li>we recommend that you extend from the {@link StandardRepresentation} and override the
 * {@link StandardRepresentation#fallbackToStringOf(Object)}. By doing this all the defaults of AssertJ would be applied and you can apply your own customization</li>
 * </ul>
 * <p>
 * The <a href="https://github.com/joel-costigliola/assertj-examples/tree/master/assertions-examples">assertj-examples</a> project provides a working example of registering a custom representation.
 * <p>
 * Registering a representation has been introduced in AssertJ 2.9.0/3.9.0.  
 * 
 * @author Mariusz Smykula
 */
public interface Representation {

  /**
   * Returns the {@code String} representation of the given object. It may or not the object's own implementation of
   * {@code toString}.
   *
   * @param object the object to represent.
   * @return the {@code toString} representation of the given object.
   */
  String toStringOf(Object object);

  /**
   * Returns the {@code String} representation of the given object with its type and hexadecimal hash code so that 
   * it can be differentied from other objects with the same {@link #toStringOf(Object)} representation.
   *
   * @param object the object to represent.
   * @return the {@code toString} representation of the given object.
   */
  String unambiguousToStringOf(Object object);

}
