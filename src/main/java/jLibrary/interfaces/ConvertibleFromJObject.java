package jLibrary.interfaces;

import jLibrary.JObject;

/**
 * Indicates that objects of this class can be created from JObjects.
 */
public interface ConvertibleFromJObject {
    /**
     * Attempts to extract data from the source JObject.
     * @param source The JObject to be parsed for data.
     * @return Returns true if the extraction was successful, or else false.
     */
    boolean fromJObject(JObject source);
}
