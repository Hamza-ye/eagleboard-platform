package com.mass3d.common;

/**
 * Marker interface for marking an object to not be treated as a id object (even if the class itself
 * implements id object), this object will not be treated as normal metadata (no refs etc) but
 * instead need to be contained in the entity that owns it.
 * <p>
 * Embedded objects should also always be implemented as cascade="delete-all-orphan".
 */
public interface EmbeddedObject {

}
