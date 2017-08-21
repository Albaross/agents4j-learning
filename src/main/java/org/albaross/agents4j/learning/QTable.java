package org.albaross.agents4j.learning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * Both state and action should override the hashCode() and equals() methods in order to work properly.
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 * @param <A> action
 */
public class QTable<S, A> implements ValueFunction<S, A> {

	protected Random rnd = new Random();
	protected Map<S, Map<A, Double>> map;
	protected int size;

	public QTable() {
		this.map = new HashMap<>();
		this.size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void update(S state, A action, double reward, S nextState) {
		update(state, action, reward, nextState, 0.1, 0.9);
	}

	/**
	 * Calculates and updates the weight of an executed action.
	 * 
	 * @param state
	 * @param action
	 * @param reward
	 * @param nextState
	 * @param learningRate
	 * @param discountRate
	 */
	public void update(S state, A action, double reward, S nextState, double learningRate, double discountRate) {
		double old = get(state, action);
		double max = getMaxWeight(nextState);
		double weight = (1 - learningRate) * old + learningRate * (reward + discountRate * max);
		put(state, action, weight);
	}

	public boolean containsKey(S state, A action) {
		Map<A, Double> tmp = map.get(state);
		return (tmp != null) ? tmp.containsKey(action) : false;
	}

	public double get(S state, A action) {
		Map<A, Double> tmp = map.get(state);
		if (tmp == null)
			return 0;

		Double weight = tmp.get(action);
		if (weight == null)
			return 0;

		return weight;
	}

	public void put(S state, A action, double weight) {
		Map<A, Double> tmp = map.get(state);

		if (tmp == null)
			map.put(state, (tmp = new HashMap<>()));

		if (!tmp.containsKey(action))
			size++;

		tmp.put(action, weight);
	}

	public void remove(S state, A action) {
		Map<A, Double> tmp = map.get(state);
		if (tmp != null) {
			tmp.remove(action);
			size--;

			if (tmp.isEmpty())
				map.remove(state);
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<S> states() {
		return map.keySet();
	}

	public Collection<Double> values(S state) {
		Map<A, Double> tmp = map.get(state);
		return (tmp != null) ? tmp.values() : Collections.emptyList();
	}

	public Set<Entry<A, Double>> entrySet(S state) {
		Map<A, Double> tmp = map.get(state);
		return (tmp != null) ? tmp.entrySet() : Collections.emptySet();
	}

	public double getMaxWeight(S state) {
		if (!map.containsKey(state))
			return 0;

		double maxWeight = -Double.MAX_VALUE;
		for (double weight : values(state)) {
			if (weight > maxWeight)
				maxWeight = weight;
		}

		return maxWeight;
	}

	public A getBestAction(S state) {
		if (!map.containsKey(state))
			return null;

		double maxWeight = getMaxWeight(state);
		List<A> bestActions = new ArrayList<>();

		for (Entry<A, Double> e : entrySet(state)) {
			if (e.getValue() == maxWeight)
				bestActions.add(e.getKey());
		}

		if (bestActions.size() == 1)
			return bestActions.get(0);

		return bestActions.get(rnd.nextInt(bestActions.size()));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		String state;
		int i = 0;

		for (Entry<S, Map<A, Double>> s : map.entrySet()) {
			state = s.getKey().toString();

			for (Entry<A, Double> a : s.getValue().entrySet()) {
				if (i > 0)
					sb.append(',').append(' ');

				sb.append(state);
				sb.append(a.getKey());
				sb.append('=');
				sb.append(String.format(Locale.ENGLISH, "%.2f", a.getValue()));

				i++;
			}
		}

		sb.append('}');
		return sb.toString();
	}

}
