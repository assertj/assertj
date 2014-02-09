/*
 * Created on Feb 08, 2014
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
 * Copyright @2010-2014 the original author or authors.
 */
package org.assertj.core.api.xml;


import static org.assertj.core.api.xml.XmlNodeAssertions.assertFor;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.internal.Xmls;
import org.assertj.core.util.xml.XPathExtractor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Assertion methods for list of xml nodes.  
 * 
 * <p>
 * To create an instance of this class, invoke {@link CharSequenceAssert#asXml()}:
 *
 * <p>
 * <pre>
 *  assertThat(&quot;&lt;element/&gt;&quot;).asXml();
 * </pre>
 * 
 * @author Michał Piotrkowski
 */
public class XmlNodeSetAssert extends AbstractAssert<XmlNodeSetAssert, NodeList> {

  private Xmls xmls = Xmls.instance();
  
  public XmlNodeSetAssert(NodeList nodeList) {
    super(nodeList, XmlNodeSetAssert.class);
  }

  /**
   * Extract the result of xpath query on xml nodes under test into new xml node set. This node set becoming new 
   * <code>actual</code> value of xml node set assertion.   
   *
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example 1 (extracting elements that represent movies produced after 2000):
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[@year>'2000']&quot;).hasSize(2);
   * </pre>
   * 
   * <p>
   * Example 1 (extracting attributes of best movie ever according to imdb):
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[1]/@*&quot;)
   *        .contains(&quot;@title='The Shawshank Redemption'&quot;, &quot;@year='1994'&quot;, &quot;@rating='9.2'&quot;);
   * </pre>
   * 
   * @param xpath query expression to execute on xml node set under test. 
   * @throws IllegalArgumentException if given <code>xpath</code> argument is not valid xpath expression. 
   * @return new xml node set assertion based on result of xpath query.
   */
  public XmlNodeSetAssert extractingXPath(String xpath) {
    return new XmlNodeSetAssert(new XPathExtractor(actual).extract(xpath));
  }

  /**
   * Verifies that <code>actual</code> set of xml nodes is empty.
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example 1 (no movies produced before 1950 in imdb top 10):
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[@year<'1950']&quot;).isEmpty();
   * </pre>
   * 
   * @throws AssertionError if <code>actual</code> set of xml nodes is not empty.
   * @return <code>this</code> assertion object.
   */
  public XmlNodeSetAssert isEmpty() {
    
    xmls.assertIsEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that <code>actual</code> set of xml nodes has expected size.
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example (2 movies produced after 2000 in imdb top 10):
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[@year>'2000']&quot;).hasSize(2);
   * </pre>
   * 
   * @throws AssertionError if <code>actual</code> set of xml nodes has size different than expected.
   * @return <code>this</code> assertion object.
   */
  public XmlNodeSetAssert hasSize(int expectedSize) {
    
    xmls.assertHasSize(info, actual, expectedSize);
    return myself;
  }

  /**
   * Verifies that <code>actual</code> set of xml nodes contains only one node and this node is of type {@link Node#ELEMENT_NODE}.
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[@year='1957']&quot;).isElement();
   * </pre>
   * 
   * @throws AssertionError if <code>actual</code> set of xml nodes contains:
   * <ul>
   *    <li>more than one node or</li>
   *    <li>less than one node or</li>
   *    <li>nodes other than {@link Node#ELEMENT_NODE}.</li>
   * </ul>
   * @return <code>this</code> assertion object.
   */ 
  public XmlNodeSetAssert isElement() {
    
    assertFor(singleNode()).isElement();
    return myself;
  }
  
  /**
   * Verifies that <code>actual</code> set of xml nodes contains only one node and this node is of type {@link Node#ATTRIBUTE_NODE}.
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[@year='1957']/@title&quot;).isAttribute();
   * </pre>
   * 
   * @throws AssertionError if <code>actual</code> set of xml nodes contains:
   * <ul>
   *    <li>more than one node or</li>
   *    <li>less than one node or</li>
   *    <li>nodes other than {@link Node#ATTRIBUTE_NODE}.</li>
   * </ul>
   * @return <code>this</code> assertion object.
   */ 
  public XmlNodeSetAssert isAttribute() {
    
    assertFor(singleNode()).isAttribute();
    return myself;
  }

  /**
   * Verifies that <code>actual</code> set of xml nodes contains only one node and this node is of type {@link Node#COMMENT_NODE}.
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;!-- Data from 02/09/2014 --&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/comment()&quot;).isComment();
   * </pre>
   * 
   * @throws AssertionError if <code>actual</code> set of xml nodes contains:
   * <ul>
   *    <li>more than one node or</li>
   *    <li>less than one node or</li>
   *    <li>nodes other than {@link Node#COMMENT_NODE}.</li>
   * </ul>
   * @return <code>this</code> assertion object.
   */ 
  public XmlNodeSetAssert isComment() {
    
    assertFor(singleNode()).isComment();
    return myself;
  }

  /**
   * Verifies that <code>actual</code> set of xml nodes contains only one node and this node is of type {@link Node#TEXT_NODE}.
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;   &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'&gt;\n&quot; +
   *   &quot;       Two imprisoned men bond over a number of years, finding solace and\n&quot; + 
   *   &quot;       eventual redemption through acts of common decency.\n&quot; +
   *   &quot;  &lt;/movie&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[1]/text()&quot;).isTextNode();
   * </pre>
   * 
   * @throws AssertionError if <code>actual</code> set of xml nodes contains:
   * <ul>
   *    <li>more than one node or</li>
   *    <li>less than one node or</li>
   *    <li>nodes other than {@link Node#TEXT_NODE}.</li>
   * </ul>
   * @return <code>this</code> assertion object.
   */ 
  public XmlNodeSetAssert isTextNode() {
    
    assertFor(singleNode()).isTextNode();
    return myself;
  }

  /**
   * Verifies that <code>actual</code> set of xml nodes contains only one node and this node is equal to <code>expected</code>.
   * <p id='expected_node_dsl'>
   * Depending on expected node type <code>expected</code> value should be provided in following format: 
   * <table>
   *    <thead>
   *        <th>Node type</th><th>Format</th>
   *    </thead>
   *    <tr>
   *        <td>{@link Node#ELEMENT_NODE}</td><td><code>&quot;&lt;element/&gt;&quot;</code></td>
   *    <tr/>
   *    <tr>
   *        <td>{@link Node#COMMENT_NODE}</td><td><code>&quot;&lt;!-- comment --&gt;&quot;</code></td>
   *    <tr/>
   *    <tr>
   *        <td>{@link Node#TEXT_NODE}</td><td><code>&quot;text node&quot;</code></td>
   *    <tr/>
   *    <tr>
   *        <td>{@link Node#ATTRIBUTE_NODE}</td><td><code>&quot;@attribute='value'&quot;</code></td>
   *    <tr/>
   * </table>
   * </p>
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[1]/@title&quot;)
   *        .isEqualTo("@title='The Shawshank Redemption'");
   * </pre>
   * 
   * @param expected string representing expected value of <code>actual</code>.
   * @throws AssertionError if <code>actual</code> set of xml nodes contains:
   * <ul>
   *    <li>more than one node or</li>
   *    <li>less than one node or</li>
   *    <li>does not equal to <code>expected</code> node</li>
   * </ul>
   * @return <code>this</code> assertion object.
   */ 
  public XmlNodeSetAssert isEqualTo(String expected) {
    
    assertFor(singleNode()).isEqualTo(expected);
    return myself;
  }
  
  private Node singleNode() {
    
    xmls.assertIsSingleNode(info, actual);
    return actual.item(0);
  }

  /**
   * Verify that <code>actual</code> set of xml nodes contains all of expected nodes. 
   * <p>
   * Order of nodes in <code>actual</code> set <b>is not</b> important.
   * </p>
   * <p>
   * List of expected nodes should be provided in format described in {@link XmlNodeSetAssert#isEqualTo(String)}
   * </p>
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[1]/@*&quot;)
   *        .contains(&quot;@title='The Shawshank Redemption'&quot;, &quot;@year='1994'&quot;, &quot;@rating='9.2'&quot;);
   * </pre>
   * 
   * @param expectedNodes list of xml nodes to expect in <code>actual</code> set of xml nodes. 
   * @throws AssertionError if at least one of expected nodes has not been found in <code>actual</code> set of xml nodes. 
   * @return <code>this</code> assertion object.
   */
  public XmlNodeSetAssert contains(String... expectedNodes) {
    
    xmls.assertContains(info, actual, expectedNodes);
    return myself;
  }
  
  /**
   * Verify that <code>actual</code> set of xml nodes contains all of expected nodes in given order. 
   * <p>
   * Order of nodes in <code>actual</code> set <b>is</b> important.
   * </p>
   * <p>
   * List of expected nodes should be provided in format described in {@link XmlNodeSetAssert#isEqualTo(String)}
   * </p>
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[@rating>='9.0']&quot;)
   *        .containsExactly(
   *            &quot;&lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;&quot;,
   *            &quot;&lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;&quot;,
   *            &quot;&lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;&quot;);
   * </pre>
   * 
   * @param expectedNodes list of xml nodes to expect in <code>actual</code> set of xml nodes. 
   * @throws AssertionError if at least one of expected nodes has not been found in <code>actual</code> set of xml nodes
   *    or extra (unexpected) element has been found
   *    or elements have been in different order.
   * @return <code>this</code> assertion object.
   */
  public XmlNodeSetAssert containsExactly(String... expectedNodes) {
    
    xmls.assertContainsExactly(info, actual, expectedNodes);
    return myself;
  }

  /**
   * Verify that <code>actual</code> set of xml nodes contains all of expected nodes and only expected nodes. 
   * <p>
   * Order of nodes in <code>actual</code> set <b>is not</b> important.
   * </p>
   * <p>
   * List of expected nodes should be provided in format described in {@link XmlNodeSetAssert#isEqualTo(String)}
   * </p>
   * <p>
   * Let's take an example to make things clearer. 
   * </p>
   * Given following xml document:
   * 
   * <pre>
   * String topTenImdbMovies = 
   *   &quot;&lt;movies&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Shawshank Redemption' year='1994' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather' year='1972' rating='9.2'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Godfather: Part II' year='1974' rating='9.0'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Dark Knight' year='2008' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Pulp Fiction' year='1994' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Good, the Bad and the Ugly' year='1966' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Schindler`s List' year='1993' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='12 Angry Men' year='1957' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='The Lord of the Rings: The Return of the King' year='2003' rating='8.9'/&gt;\n&quot; +
   *   &quot;  &lt;movie title='Fight Club' year='1999' rating='8.9'/&gt;\n&quot; +
   *   &quot;&lt;/movies&gt;&quot;;
   * </pre>
   * 
   * <p>
   * Example:
   * <pre>
   *    assertThat(topTenImdbMovies).asXml().extractingXPath(&quot;/movies/movie[1]/@*&quot;)
   *        .containsOnly(&quot;@title='The Shawshank Redemption'&quot;, &quot;@year='1994'&quot;, &quot;@rating='9.2'&quot;);
   * </pre>
   * 
   * @param expectedNodes list of xml nodes to expect in <code>actual</code> set of xml nodes. 
   * @throws AssertionError if at least one of expected nodes has not been found in <code>actual</code> set of xml nodes
   *    or extra (unexpected) element has been found. 
   * @return <code>this</code> assertion object.
   */
  public XmlNodeSetAssert containsOnly(String... expectedNodes) {
    
    xmls.assertContainsOnly(info, actual, expectedNodes);
    return myself;
  }

}
