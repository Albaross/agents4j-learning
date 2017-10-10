package org.albaross.agents4j.learning.utils;

/**
 * 
 * @author Manuel barbi
 *
 * @param <A> action
 */
@FunctionalInterface
public interface ActionEncoder<A> {

	int encode(A action);

}
