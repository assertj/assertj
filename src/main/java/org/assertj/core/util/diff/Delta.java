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

import java.util.List;

/**
 * Describes the delta between original and revised texts.
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param <T> The type of the compared elements in the 'lines'.
 */
public abstract class Delta<T> {
	
	/** The original chunk. */
    private Chunk<T> original;
    
    /** The revised chunk. */
    private Chunk<T> revised;
    
    /**
     * Specifies the type of the delta.
     *
     */
    public enum TYPE {
    	/** A change in the original. */
        CHANGE, 
        /** A delete from the original. */
        DELETE, 
        /** An insert into the original. */
        INSERT
    }
    
    /**
     * Construct the delta for original and revised chunks
     * 
     * @param original Chunk describing the original text. Must not be {@code null}.
     * @param revised Chunk describing the revised text. Must not be {@code null}.
     */
    public Delta(Chunk<T> original, Chunk<T> revised) {
    	if (original == null) {
    		throw new IllegalArgumentException("original must not be null");
    	}
    	if (revised == null) {
    		throw new IllegalArgumentException("revised must not be null");
    	}
        this.original = original;
        this.revised = revised;
    }
    
    /**
     * Verifies that this delta can be used to patch the given text.
     * 
     * @param target the text to patch.
     * @throws PatchFailedException if the patch cannot be applied.
     */
    public abstract void verify(List<T> target) throws PatchFailedException;
    
    /**
     * Applies this delta as the patch for a given target
     * 
     * @param target the given target
     * @throws PatchFailedException
     */
    public abstract void applyTo(List<T> target) throws PatchFailedException;

    /**
     * Returns the type of delta
     * @return the type enum
     */
    public abstract TYPE getType();
    
    /**
     * @return The Chunk describing the original text.
     */
    public Chunk<T> getOriginal() {
        return original;
    }
    
    /**
     * @param original The Chunk describing the original text to set.
     */
    public void setOriginal(Chunk<T> original) {
        this.original = original;
    }
    
    /**
     * @return The Chunk describing the revised text.
     */
    public Chunk<T> getRevised() {
        return revised;
    }
    
    /**
     * @param revised The Chunk describing the revised text to set.
     */
    public void setRevised(Chunk<T> revised) {
        this.revised = revised;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((original == null) ? 0 : original.hashCode());
        result = prime * result + ((revised == null) ? 0 : revised.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Delta other = (Delta) obj;
        if (original == null) {
            if (other.original != null)
                return false;
        } else if (!original.equals(other.original))
            return false;
        if (revised == null) {
            if (other.revised != null)
                return false;
        } else if (!revised.equals(other.revised))
            return false;
        return true;
    }
    
}
