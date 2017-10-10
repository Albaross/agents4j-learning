package org.albaross.agents4j.learning.components;

import org.albaross.agents4j.core.ValueComponent;
import org.albaross.agents4j.learning.QTable;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 * @param <A> action
 */
public class QTableComponent<S, A> implements ValueComponent<S, A> {

	protected QTable<S, A> table = new QTable<>();
	protected double alpha = 0.1;
	protected double gamma = 0.9;

	@Override
	public void update(S state, A action, double reward, S next, boolean terminal) {
		table.update(state, action, reward, next, terminal, alpha, gamma);
	}

	@Override
	public A getBestAction(S state) {
		return table.getBestAction(state);
	}

}
