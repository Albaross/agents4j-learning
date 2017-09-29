package org.albaross.agents4j.learning;

@FunctionalInterface
public interface ActionDecoder<A> {

	A decode(int code);

}
