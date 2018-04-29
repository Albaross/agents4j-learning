package org.albaross.agents4j.learning.components;

import org.albaross.agents4j.learning.Experience;
import org.albaross.agents4j.learning.QNet;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @param <S> state
 * @param <A> action
 * @author Manuel Barbi
 */
public class QNetComponent<S, A> implements ValueComponent<S, A> {

    protected static final Random RND = new Random();

    protected QNet<S, A> net;
    protected double gamma = 0.99;

    protected CircularFifoQueue<Experience<S, A>> replayStore;
    protected int batchSize;

    public QNetComponent(QNet<S, A> net) {
        this(net, new CircularFifoQueue<>(100000), 32);
    }

    public QNetComponent(QNet<S, A> net, int replayCapacity, int batchSize) {
        this(net, new CircularFifoQueue<>(replayCapacity), batchSize);
    }

    public QNetComponent(QNet<S, A> net, CircularFifoQueue<Experience<S, A>> replayStore, int batchSize) {
        this.net = Objects.requireNonNull(net, "net must not be null");
        this.replayStore = Objects.requireNonNull(replayStore, "replay store must not be null");
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
