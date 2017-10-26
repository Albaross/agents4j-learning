package org.albaross.agents4j.learning.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import org.albaross.agents4j.core.ValueComponent;
import org.albaross.agents4j.learning.Experience;
import org.albaross.agents4j.learning.QNet;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 * @param <A> action
 */
public class QNetComponent<S, A> implements ValueComponent<S, A> {

	protected static final Random RND = new Random();

	protected QNet<S, A> net;
	protected double gamma = 0.99;

	protected CircularFifoQueue<Experience<S, A>> replayStore;
	protected int replayCapacity;
	protected int batchSize;

	public QNetComponent(QNet<S, A> net, int replayCapacity, int batchSize) {
		this.net = Objects.requireNonNull(net, "net must not be null");
		this.replayCapacity = replayCapacity;
		this.replayStore = new CircularFifoQueue<>(replayCapacity);
		this.batchSize = batchSize;
	}

	@Override
	public void update(S state, A action, double reward, S next, boolean terminal) {
		replayStore.add(new Experience<>(state, action, reward, next, terminal));

		if (replayStore.size() > batchSize) {
			IntStream stream = RND.ints(batchSize, 0, replayStore.size());
			final List<Experience<S, A>> batch = new ArrayList<>();
			stream.forEach((i) -> batch.add(replayStore.get(i)));
			net.updateBatch(batch, gamma);
		}
	}

	@Override
	public A getBestAction(S state) {
		return net.getBestAction(state);
	}

}
