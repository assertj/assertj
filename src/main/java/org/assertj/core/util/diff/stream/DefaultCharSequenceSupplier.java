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
package org.assertj.core.util.diff.stream;

import static java.nio.CharBuffer.allocate;
import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static java.nio.charset.CodingErrorAction.REPORT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Objects;

import org.assertj.core.util.VisibleForTesting;

class DefaultCharSequenceSupplier implements CharSequenceSupplier {
  @VisibleForTesting
  static final CharBuffer EMPTY_BUFFER = allocate(0);

  private final CharsetDecoder decoder;
  private final SeekableByteChannel channel;

  DefaultCharSequenceSupplier(Charset charset, SeekableByteChannel channel) {
    decoder = charset.newDecoder().onMalformedInput(REPORT).onUnmappableCharacter(REPORT);
    this.channel = channel;
  }

  @Override
  public CharSequence get(long from, long to) throws IOException {
    if (Objects.equals(from, to)) {
      return EMPTY_BUFFER;
    }
    try {
      CharBuffer charBuffer = allocate((int) (to - from));
      channel.position(from);
      decode(channel, charBuffer, decoder);
      charBuffer.position(0);
      return charBuffer;
    } finally {
      decoder.reset();
    }
  }

  private void decode(SeekableByteChannel channel, CharBuffer charBuffer, CharsetDecoder decoder) throws IOException {
    ByteBuffer byteBuffer = ByteBuffer.allocate(charBuffer.capacity());
    while (charBuffer.hasRemaining()) {
      // read data from channel to byteBuffer
      if (channel.read(byteBuffer) <= 0) {
        decode(charBuffer, decoder, byteBuffer, true);
        decoder.flush(charBuffer);
        break;
      }
      byteBuffer.flip();

      // decode data from byteBuffer to charBuffer
      CoderResult res = decode(charBuffer, decoder, byteBuffer, false);
      if (OVERFLOW == res) {
        charBuffer.clear();
      } else if (UNDERFLOW == res) {
        byteBuffer.compact();
        charBuffer.limit(charBuffer.capacity() - charBuffer.remaining());
      }
    }
  }

  private CoderResult decode(CharBuffer charBuffer, CharsetDecoder decoder, ByteBuffer byteBuffer,
                             boolean b) throws CharacterCodingException {
    CoderResult decode = decoder.decode(byteBuffer, charBuffer, b);
    if (!decode.isUnderflow() && !decode.isOverflow()) {
      decode.throwException();
    }
    return decode;
  }
}
