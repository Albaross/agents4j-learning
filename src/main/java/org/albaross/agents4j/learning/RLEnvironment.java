package org.albaross.agents4j.learning;

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

	public RLEnvironment(List<Agent<S, A>> agents) {
		super(agents);
	}

	@Override
	protected void runSingleAgent(Agent<S, A> agent, int agentId) {
		S state = createPerception(agentId);
		LOG.debug("create perception {} for agent {}", state, agentId);
		A action = agent.generateAction(state);
		LOG.debug("agent {} executes {}", agentId, action);

		executeAction(agentId, action);

		double reward = getReward(agentId);
		S next = createPerception(agentId);

		if (agent instanceof RLAgent<?, ?>)
			((RLAgent<S, A>) agent).update(state, action, reward, next);
	}

	protected abstract double getReward(long agentId);

}
