package org.albaross.agents4j.learning.utils;

/**
 * 
 * @author Manuel barbi
 *
 * @param <A> action
 */
@FunctionalInterface
public interface ActionDecoder<A> {

	A decode(int code);

}
