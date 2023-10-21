package jLibrary.interfaces.functional;

import jLibrary.JObject;

/**
 * This is a functional interface with a single abstract method.
 * @param <t>
 */
public interface JObjectExecutionPredicate<t extends JObject> {
    t execute(t input);
}
