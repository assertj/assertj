package org.assertj.core.presentation;

import java.util.*;

/**
 * Representation of Map instances.
 *
 * @author Vojislav Marinkovic
 */
public class MapRepresentation implements Representation {
    private static final String NORMAL_REPRESENTATION = "%s=%s";
    private static final String KEY_DIFF_REPRESENTATION = "[%s=%s]";
    private static final String VALUE_DIFF_REPRESENTATION = "%s=[%s]";

    Representation elementsRepresentation = new StandardRepresentation();
    Representation valueRepresentation = new StandardRepresentation();

    private Set unequalEntryByKey = new HashSet();
    private Set unequalEntryByValue = new HashSet();

    public void addUnequalEntryByKey(Object entryKey) {
        this.unequalEntryByKey.add(entryKey);
    }

    public void addUnequalEntryByValue(Object entryKey) {
        this.unequalEntryByValue.add(entryKey);
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
            if (unequalEntryByKey.contains(e.getKey())){
                representation = KEY_DIFF_REPRESENTATION;
            }
            else if (unequalEntryByValue.contains(e.getKey())){
                representation = VALUE_DIFF_REPRESENTATION;
            }
            formatter.format(representation, key, value);
            if (i.hasNext()) {
                buffer.append(", ");
            }
        }
        return buffer.append("}").toString();
    }
}
