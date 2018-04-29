package org.albaross.agents4j.learning;

import org.albaross.agents4j.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The environment one or more agents are located in.
 * The environment's execution is measured in ticks and is executed as long as not all
 * agents has finished respectively abandon their task.
 *
 * @author Manuel Barbi
 */
public abstract class RLEnvironment<P, A> {

    private static final Logger LOG = LoggerFactory.getLogger(RLEnvironment.class);

    protected double[] cumulative;

    public RLEnvironment(List<Agent<P, A>> agents) {
        //    super(agents);
        this.cumulative = new double[agents.size()];
    }


	/*
	protected void runSingleAgent(Agent<P, A> agent, int agentId) {
		P perception = createPerception(agentId);
		LOG.debug("<tick {}> agent {} perceives {}", tick, agentId, perception);
		A action = agent.generateAction(perception, tick);
		executeAction(agentId, action);
		double reward = getReward(agentId);
		LOG.debug("<tick {}> agent {} executes {} and receives", tick, agentId, action, reward);
		cumulate(agentId, reward);
		P next = createPerception(agentId);
		agent.update(perception, action, reward, next, terminationCriterion(agentId));
	}

	public abstract double getReward(int agendId);

	public void cumulate(int agentId, double reward) {
		cumulative[agentId] += reward;
	}

	public double getCumulative(int agentId) {
		return cumulative[agentId];
	}

	public void reboot() {
		this.tick = 0;
		Arrays.fill(this.agentDisabled, false);
		Arrays.fill(this.cumulative, 0);
	} */

}
