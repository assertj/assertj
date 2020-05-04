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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.presentation;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Throwable object representation instead of standard java representation.
 *
 * @author Mariusz Smykula
 */
public class ThrowableRepresentation extends StandardRepresentation {
  public static final ThrowableRepresentation THROWABLE_REPRESENTATION = new ThrowableRepresentation();


  /**
   * Returns throwable the {@code toString} representation of the given object. It may or not the object's own
   * implementation of {@code toString}.
   *
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (object instanceof Throwable) return  toStringOf((Throwable) object);
    return super.toStringOf(object);
  }

  protected String toStringOf(Throwable throwable) {
    //TODO: need configuration to limit line number
//    StringWriter sw = new StringWriter();
//    throwable.printStackTrace(new PrintWriter(sw));
//    return sw.toString();

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    StackTraceElement[] elements = throwable.getStackTrace();
    pw.println(throwable);
    int elementNumber = elements.length;
    // TODO: get the maxElementNumber from Configuration
    int maxElementNumber = 2;
    if (maxElementNumber>=elementNumber) {
      for (StackTraceElement element : elements) {
        pw.println("\tat " + element);
      }
    } else {
      // In this case we may select the topped elements if line number is limited.
      for ( int i=0;i<maxElementNumber; ++i ) {
        pw.println("\tat " + elements[i]);
      }
      pw.println("\t...( "+(elementNumber - maxElementNumber)+" lines folded)");
    }

    return sw.toString();
  }
}
