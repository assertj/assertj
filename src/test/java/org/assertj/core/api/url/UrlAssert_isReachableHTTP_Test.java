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
package org.assertj.core.api.url;

import static org.mockito.Mockito.verify;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.assertj.core.api.UrlAssert;
import org.assertj.core.api.UrlAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("UrlAssert isReachableHTTP")
public class UrlAssert_isReachableHTTP_Test extends UrlAssertBaseTest {

  private static class LocalHTTPHandler implements HttpHandler {

    static final LocalHTTPHandler INSTANCE = new LocalHTTPHandler();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      exchange.sendResponseHeaders(404, 0);
      exchange.close();
    }
  }

  private final URL expected = new URL("http://localhost:8082/test");

  public UrlAssert_isReachableHTTP_Test() throws MalformedURLException {}

  @Override
  protected UrlAssert create_assertions() {
    return new UrlAssert(expected);
  }

  @Override
  protected UrlAssert invoke_api_method() {
    return assertions.isHTTPReachable();
  }

  @Override
  protected void verify_internal_effects() {
    verify(urls).assertHTTPReachable(getInfo(assertions), getActual(assertions));
  }
}
