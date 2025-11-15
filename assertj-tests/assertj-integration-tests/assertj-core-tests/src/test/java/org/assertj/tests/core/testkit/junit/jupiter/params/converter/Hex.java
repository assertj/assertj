/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.testkit.junit.jupiter.params.converter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.assertj.core.internal.Digests;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

/**
* Annotation that allows converting string parameters of hexadecimal values into byte arrays.
*/
@Target(PARAMETER)
@Retention(RUNTIME)
@ConvertWith(Hex.HexArgumentConverter.class)
public @interface Hex {

  class HexArgumentConverter extends TypedArgumentConverter<String, byte[]> {

    protected HexArgumentConverter() {
      super(String.class, byte[].class);
    }

    @Override
    protected byte[] convert(String source) throws ArgumentConversionException {
      return Digests.fromHex(source);
    }

  }

}
