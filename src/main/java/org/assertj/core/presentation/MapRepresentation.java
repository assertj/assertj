package org.assertj.core.presentation;

import java.util.*;

/**
 * Representation of Map instances.
 *
 * @author Vojislav Marinkovic
 */
public class MapRepresentation implements Representation {
    private static final String NORMAL_REPRESENTATION = "%s=%s";
    private static final String DIFF_REPRESENTATION = "[%s=%s]";

    Representation elementsRepresentation = new StandardRepresentation();
    Representation valueRepresentation = new StandardRepresentation();

    Set unequalEntries = new HashSet();

    public void addUnequalEntry(Object key) {
        this.unequalEntries.add(key);
    }

    /** {@inheritDoc} */
    @Override public String toStringOf(Object object) {
        Map map = (Map)object;

        if (map == null) {
            return null;
        }

        Iterator i = map.entrySet().iterator();
        StringBuilder buffer = new StringBuilder();

        Formatter formatter = new Formatter(buffer);
        buffer.append("{");
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            String key = elementsRepresentation.toStringOf(e.getKey());
            String value = valueRepresentation.toStringOf(e.getValue());
            String representation = NORMAL_REPRESENTATION;
            if (unequalEntries.contains(e.getKey())){
                representation = DIFF_REPRESENTATION;
            }
            formatter.format(representation, key, value);
            if (i.hasNext()) {
                buffer.append(", ");
            }
        }
        return buffer.append("}").toString();
    }
}
