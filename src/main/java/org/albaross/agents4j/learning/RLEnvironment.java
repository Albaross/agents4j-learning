package org.albaross.agents4j.learning;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.albaross.agents4j.core.Agent;
import org.albaross.agents4j.core.common.BasicEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <S> state
 * @param <A> action
 */
public abstract class RLEnvironment<S, A> extends BasicEnvironment<S, A> {

	private static final Logger LOG = LoggerFactory.getLogger(RLEnvironment.class);

	protected double[] cumulative;
	protected S[] keepState;
	protected A[] keepAction;
	protected double[] keepReward;

	public RLEnvironment(List<Agent<S, A>> agents) {
		super(agents);
		this.cumulative = new double[agents.size()];
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void runSingleAgent(Agent<S, A> agent, int agentId) {
		S state = createPerception(agentId);
		LOG.info("<tick {}> agent {} perceives {}", tick, agentId, state);

		if (this.keepState != null) {
			// update experience from the last step
			if (agent instanceof RLAgent<?, ?>)
				((RLAgent<S, A>) agent).update(keepState[agentId], keepAction[agentId], keepReward[agentId], state, false);
		}

		A action = agent.generateAction(state);
		executeAction(agentId, action);

		double reward = getReward(agentId);
		LOG.info("<tick {}> agent {} executes {} and receives {}", tick, agentId, action, reward);
		cumulate(agentId, reward);

		if (agent instanceof RLAgent<?, ?> && terminationCriterion(agentId))
			// since the next state is terminal the actual state doesn't matter
			((RLAgent<S, A>) agent).update(state, action, reward, null, true);

		if (this.keepState == null) {
			this.keepState = (S[]) Array.newInstance(state.getClass(), agents.size());
			this.keepAction = (A[]) Array.newInstance(action.getClass(), agents.size());
			this.keepReward = new double[agents.size()];
		}

		this.keepState[agentId] = state;
		this.keepAction[agentId] = action;
		this.keepReward[agentId] = reward;
	}

	protected void cumulate(int agentId, double reward) {
		cumulative[agentId] += reward;
	}

	protected abstract double getReward(int agentId);

	public double getCumulative(int agentId) {
		return cumulative[agentId];
	}

	@Override
	public void reboot() {
		super.reboot();
		Arrays.fill(this.cumulative, 0);
		this.keepState = null;
		this.keepAction = null;
		this.keepReward = null;
	}

}
