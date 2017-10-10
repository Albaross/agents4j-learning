package org.albaross.agents4j.learning.utils;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <A> action
 */
@FunctionalInterface
public interface ActionRandomizer<A> {

	A randomAction();

}
