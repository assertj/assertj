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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.util;

/**
 * <p>A functional interface that describes a function with zero parameters 
 * and no return value. A side-effect function, if you will.</p>
 * 
 * <p>To be used in places where such a function is needed as a method argument, 
 * but you don't want to call it a {@link Runnable}.</p>
 * @author bzt
 *
 */
@FunctionalInterface
public interface NonaryFunction {

  public void apply(); 
  
}
