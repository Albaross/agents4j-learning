package org.albaross.agents4j.learning;

public class Experience<S, A> {

	protected final S state;
	protected final A action;
	protected final double reward;
	protected final S next;
	protected final boolean terminal;

	public Experience(S state, A action, double reward, S next, boolean terminal) {
		this.state = state;
		this.action = action;
		this.reward = reward;
		this.next = next;
		this.terminal = terminal;
	}

	public S getState() {
		return state;
	}

	public A getAction() {
		return action;
	}

	public double getReward() {
		return reward;
	}

	public S getNext() {
		return next;
	}

	public boolean isTerminal() {
		return terminal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		long temp;
		temp = Double.doubleToLongBits(reward);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + (terminal ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Experience other = (Experience) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (Double.doubleToLongBits(reward) != Double.doubleToLongBits(other.reward))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (terminal != other.terminal)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + state + ", " + action + ", " + reward + ", " + next + ", " + terminal + ")";
	}

}
