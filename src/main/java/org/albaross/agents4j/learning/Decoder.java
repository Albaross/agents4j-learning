package org.albaross.agents4j.learning;

@FunctionalInterface
public interface Decoder<A> {

	A decode(int code);

}
