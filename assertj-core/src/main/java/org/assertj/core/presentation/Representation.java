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
package org.assertj.core.presentation;

import java.util.ServiceLoader;

import org.assertj.core.api.Assertions;
import org.assertj.core.configuration.Configuration;

/**
 * Controls the formatting (String representation) of types in assertion error messages.
 * <p>
 * There are several ways to replace the {@link StandardRepresentation} as the default {@link Representation}:  
 * <ul>
 * <li>call {@link Assertions#useRepresentation(Representation)}, from this point all assertions will use the given representation.</li>
 * <li>use a {@link Configuration} overriding the default representation specified with {@link Configuration#representation()} as explained <a href="https://assertj.github.io/doc/#automatic-configuration-discovery">here</a>.</li>
 * <li>register a representation as a service discovered at program startup.</li>
 * </ul>
 * <p>
 * The advantage of registering a representation (or a  configuration overriding the default representation) is that you don't need to do anything in your tests, 
 * the java runtime will discover it and AssertJ will use it but it requires a bit more work than a simple call to {@link Assertions#useRepresentation(Representation)}.  
 * <p>
 * Note that a {@link Configuration} overriding the default representation takes precedence over any registered representation. 
 * <p>
 * To register a {@link Representation}, you need to do several things:
 * <ul>
 * <li>create a file named {@code org.assertj.core.presentation.Representation} file in META-INF/services directory</li>
 * <li>put the fully qualified class name of your {@link Representation} in it</li>   
 * <li>make sure {@code META-INF/services/org.assertj.core.presentation.Representation} is in the runtime classpath, usually putting it in {@code src/test/resources} is enough</li>
 * </ul>
 * <p>
 * The <a href="https://github.com/assertj/assertj-examples/tree/main/assertions-examples">assertj-examples</a> project provides a working example of registering a custom representation.
 * <p>
 * Registering a representation has been introduced in AssertJ 2.9.0/3.9.0.  
 * <p>
 * Since 3.22.0, AssertJ can load multiples representations from the classpath, the idea behind is that different domain-specific libraries would be able to 
 * independently register representations for their respective domain. AssertJ aggregate them in a {@link CompositeRepresentation} which loops over 
 * the different representations and use the first non null representation value of the variable to display. If multiples representations overlap the highest priority one wins (see {@link #getPriority()}).   
 * The {@link StandardRepresentation} is the fallback option when all the registered representations returned a null representation of the value to display (meaning they did not know how to represent the value).
 * <p>
 * 
 * @author Mariusz Smykula
 */
public interface Representation {

  int DEFAULT_PRIORITY = 1;

  /**
   * Returns the {@code String} representation of the given object. It may or may not be the object's own implementation of
   * {@code toString}.
   *
   * @param object the object to represent.
   * @return the {@code toString} representation of the given object.
   */
  String toStringOf(Object object);

  /**
   * Override this method to return a {@code String} representation of the given object that is unambigous so that it can 
   * be differentiated from other objects with the same {@link #toStringOf(Object)} representation.
   * <p>
   * The default implementation calls {@link #toStringOf(Object)} but the {@link StandardRepresentation} adds 
   * the object hexadecimal identity hash code.   
   *
   * @param object the object to represent.
   * @return the unambiguous {@code toString} representation of the given object.
   */
  default String unambiguousToStringOf(Object object) {
    return toStringOf(object);
  }

  /**
   * In case multiple representations are loaded through {@link ServiceLoader} and they can represent the same types the one with the highest priority is selected.
   * If representations have the same priority, there is no guarantee which one is selected (but one will).
   * <p>
   * The {@link StandardRepresentation} is the fallback option when all the registered representations returned a null representation of the value to display (meaning they did not know how to represent the value).
   * <p>
   * The default priority is 1.
   * 
   * @return the representation priority.
   */
  default int getPriority() {
    return DEFAULT_PRIORITY;
  }

}
