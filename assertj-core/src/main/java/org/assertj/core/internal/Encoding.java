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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal;

import static java.nio.file.Files.newInputStream;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Closeables.closeQuietly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that validates the charset encoding of a {@link Path}, {@link File} or {@link InputStream}.
 *
 * @author Ludovic VIEGAS
 */
public class Encoding {

  private final boolean lenient;

  private final CharsetDecoder charsetDecoder;

  public Encoding(Charset charset, boolean lenient) {
    this.lenient = lenient;

    // Store the charset decoder and make it throw an exception if a decoding issue is encountered
    charsetDecoder = charset.newDecoder();
    charsetDecoder.onMalformedInput(CodingErrorAction.REPORT);
    charsetDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
  }

  public List<EncodingIssue> validate(File actual) {
    return validate(actual.toPath());
  }

  public List<EncodingIssue> validate(Path actual) {
    try {
      return validate(newInputStream(actual));
    } catch (IOException e) {
      return fail("Failed to create BufferedReader: " + actual, e);
    }
  }

  public List<EncodingIssue> validate(InputStream inputStream) {
    return validate(readerFor(inputStream));
  }

  private BufferedReader readerFor(InputStream stream) {
    return new BufferedReader(new InputStreamReader(stream, charsetDecoder));
  }

  private List<EncodingIssue> validate(BufferedReader reader) {
    final List<EncodingIssue> encodingIssues = new ArrayList<>();

    // A line counter that starts at 1
    long lineCounter = 1;

    try {
      String line;
      while ((line = reader.readLine()) != null) {

        // Look for the replacement character if validation is not lenient = false.
        if (!lenient && line.contains(charsetDecoder.replacement())) {
          encodingIssues.add(new EncodingIssue(lineCounter, line));
        }

        // Increment line counter before reading next line
        lineCounter++;
      }
    } catch (CharacterCodingException e) {
      encodingIssues.add(new EncodingIssue(lineCounter, null));
    } catch (IOException e) {
      // Any other IOException shall fail immediately
      return fail("Failed to read lines", e);
    } finally {
      closeQuietly(reader);
    }

    return unmodifiableList(encodingIssues);
  }
}
