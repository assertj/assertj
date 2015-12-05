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

/**
 * Describes the diff row in form [tag, oldLine, newLine) for showing the
 * difference between two texts
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 */
public class DiffRow {
    private final Tag tag;
    private final String oldLine;
    private final String newLine;
    
    public DiffRow(Tag tag, String oldLine, String newLine) {
        this.tag = tag;
        this.oldLine = oldLine;
        this.newLine = newLine;
    }
    
    public enum Tag {
        INSERT, DELETE, CHANGE, EQUAL
    }
    
    /**
     * @return the tag
     */
    public Tag getTag() {
        return tag;
    }
    
    /**
     * @return the oldLine
     */
    public String getOldLine() {
        return oldLine;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((newLine == null) ? 0 : newLine.hashCode());
        result = prime * result + ((oldLine == null) ? 0 : oldLine.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DiffRow other = (DiffRow) obj;
        if (newLine == null) {
            if (other.newLine != null)
                return false;
        } else if (!newLine.equals(other.newLine))
            return false;
        if (oldLine == null) {
            if (other.oldLine != null)
                return false;
        } else if (!oldLine.equals(other.oldLine))
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }
    
    public String toString() {
        return "[" + this.tag + "," + this.oldLine + "," + this.newLine + "]";
    }
}
