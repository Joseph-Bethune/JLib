package jLibrary.interfaces;

import jLibrary.JObject;

/**
 * Indicates that objects of this class can be converted to JObjects.
 */
public interface ConvertibleToJObject {

    /**
     * Creates a JObject from the calling instance.
     * @return Returns a JObject created from the calling instance.
     */
    JObject toJObject();
}
