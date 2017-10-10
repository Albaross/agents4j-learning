package org.albaross.agents4j.learning.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.albaross.agents4j.learning.Experience;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 * @param <A> action
 */
public class QTableReplayComponent<S, A> extends QTableComponent<S, A> {

	protected List<Experience<S, A>> replayStorage;
	protected int replayCapacity;

	protected List<Experience<S, A>> lastSequence;

	public QTableReplayComponent(int replayCapacity) {
		this.replayCapacity = replayCapacity;
		this.replayStorage = new ArrayList<>(replayCapacity);
		this.lastSequence = Collections.emptyList();
	}

	@Override
	public void update(S state, A action, double reward, S next, boolean terminal) {
		replayStorage.add(new Experience<>(state, action, reward, next, terminal));

		if (terminal || replayStorage.size() >= replayCapacity) {
			for (int e = replayStorage.size() - 1; e >= 0; e--)
				table.update(replayStorage.get(e), alpha, gamma);

			this.lastSequence = replayStorage;
			this.replayStorage = new ArrayList<>(replayCapacity);
		}
	}

	@Override
	public A getBestAction(S state) {
		return table.getBestAction(state);
	}

	public List<Experience<S, A>> getLastSequence() {
		return lastSequence;
	}

}
