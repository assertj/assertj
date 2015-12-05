/*
   Copyright 2010 Dmitry Naumenko (dm.naumenko@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.assertj.core.util.diff;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    
    public static <T> String join(final Iterable<T> objs, final String delimiter) {
        Iterator<T> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(String.valueOf(iter.next()));
        while (iter.hasNext()) {
            buffer.append(delimiter).append(String.valueOf(iter.next()));
        }
        return buffer.toString();
    }
    
    /**
     * Replaces all tabs with 4 spaces.
     * @param str The string.
     * @return the string with tabs replaced.
     */
    public static String expandTabs(String str) {
        return str.replace("\t", "    ");
    }
    
    /**
     * Replaces all opening an closing tags with <code>&lt;</code> or <code>&gt;</code>.
     * @param str The string.
     * @return the string with < and > html escaped.
     */
    public static String htmlEntites(String str) {
        return str.replace("<", "&lt;").replace(">", "&gt;");
    }
    
    public static String normalize(String str) {
        return expandTabs(htmlEntites(str));
    }
    
    public static List<String> normalize(List<String> list) {
        return list.stream()
                .map(StringUtils::normalize)
                .collect(Collectors.toList());
    }
    
    public static List<String> wrapText(List<String> list, int columnWidth) {
        return list.stream()
                .map(l -> wrapText(l, columnWidth))
                .collect(Collectors.toList());
    }
    
    /**
     * Wrap the text with the given column width 
     * @param line the text
     * @param columnWidth the given column
     * @return the wrapped text
     */
    public static String wrapText(String line, int columnWidth) {
        int length = line.length();
        int delimiter = "<br>".length();
        int widthIndex = columnWidth;
        
        for (int count = 0; length > widthIndex; count++) {
            line = line.subSequence(0, widthIndex + delimiter * count) + "<br>"
                    + line.substring(widthIndex + delimiter * count);
            widthIndex += columnWidth;
        }
        
        return line;
    }
}
