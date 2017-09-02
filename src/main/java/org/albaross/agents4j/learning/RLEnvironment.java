package org.albaross.agents4j.learning;

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

	public RLEnvironment(List<Agent<S, A>> agents) {
		super(agents);
		this.cumulative = new double[agents.size()];
	}

	@Override
	protected void runSingleAgent(Agent<S, A> agent, int agentId) {
		S state = createPerception(agentId);
		LOG.debug("create perception {} for agent {}", state, agentId);
		A action = agent.generateAction(state);
		LOG.debug("agent {} executes {}", agentId, action);

		executeAction(agentId, action);

		double reward = getReward(agentId);
		cumulate(agentId, reward);
		S next = createPerception(agentId);

		if (agent instanceof RLAgent<?, ?>)
			((RLAgent<S, A>) agent).update(state, action, reward, next);
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
	}

}
