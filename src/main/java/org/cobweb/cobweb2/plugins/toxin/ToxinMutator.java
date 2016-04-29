package org.cobweb.cobweb2.plugins.toxin;

import java.util.Arrays;
import java.util.Collection;

import org.cobweb.cobweb2.core.Agent;
import org.cobweb.cobweb2.core.RandomSource;
import org.cobweb.cobweb2.plugins.ConsumptionMutator;
import org.cobweb.cobweb2.plugins.EnvironmentMutator;
import org.cobweb.cobweb2.plugins.LoggingMutator;
import org.cobweb.cobweb2.plugins.StatefulSpawnMutatorBase;
import org.cobweb.cobweb2.plugins.UpdateMutator;


public class ToxinMutator extends StatefulSpawnMutatorBase<ToxinState> implements LoggingMutator, UpdateMutator, ConsumptionMutator, EnvironmentMutator {


	private ToxinParams params;

	public ToxinMutator(RandomSource rand) {
		super(ToxinState.class, rand);
	}

	public void setParams(ToxinParams toxinParams, int agentTypes) {
		this.params = toxinParams;

		agentCount = new int[agentTypes];
		agentTypeToxin = new float[agentTypes];
	}

	@Override
	public void onUpdate(Agent agent) {
		ToxinState state = getAgentState(agent);
		state.toxicity *= (1.0f - state.agentParams.purgeRate);

		agentCount[agent.getType()]++;
		agentTypeToxin[agent.getType()] += state.toxicity;

		float effect = 1
				+ Math.max(0, state.toxicity - state.agentParams.toxicityThreshold)
				* state.agentParams.toxicityEffect;

		state.agentParams.param.modifyValue(this, agent, effect);
	}

	@Override
	public Collection<String> logDataAgent(int agentType) {
		float average = agentTypeToxin[agentType] / agentCount[agentType];
		if (Float.isNaN(average))
			average = 0;
		return Arrays.asList(Float.toString(average));
	}

	@Override
	public Collection<String> logDataTotal() {
		float totalToxin = 0;
		for (int i =0; i < agentTypeToxin.length; i++)
			totalToxin += agentTypeToxin[i];

		int totalCount = 0;
		for (int i =0; i < agentCount.length; i++)
			totalCount += agentCount[i];

		float average = totalToxin / totalCount;
		if (Float.isNaN(average))
			average = 0;

		return Arrays.asList(Float.toString(average));
	}

	@Override
	public Collection<String> logHeadersAgent() {
		return Arrays.asList("Toxicity");
	}

	@Override
	public Collection<String> logHeaderTotal() {
		return Arrays.asList("Toxicity");
	}

	@Override
	protected ToxinState stateForNewAgent(Agent agent) {
		ToxinAgentParams agentParams = params.agentParams[agent.getType()];
		return new ToxinState(agentParams.clone(), agentParams.initialToxicity);
	}

	@Override
	protected ToxinState stateFromParent(Agent agent, ToxinState parentState) {
		ToxinAgentParams agentParams = params.agentParams[agent.getType()];
		return new ToxinState(agentParams.clone(), parentState.toxicity * parentState.agentParams.childTransfer);
	}

	@Override
	protected boolean validState(ToxinState value) {
		return value != null;
	}

	@Override
	public void onConsumeAgent(Agent agent, Agent food) {
		ToxinState state = getAgentState(agent);
		ToxinState foodState = getAgentState(food);
		state.toxicity += foodState.toxicity * state.agentParams.agentToxicityTransfer[food.getType()];
	}

	@Override
	public void onConsumeFood(Agent agent, int foodType) {
		ToxinState state = getAgentState(agent);
		state.toxicity += state.agentParams.foodToxicity[foodType];
	}

	private int[] agentCount;
	private float[] agentTypeToxin;

	@Override
	public void update() {
		for(int i = 0; i < agentCount.length; i++) {
			agentCount[i] = 0;
			agentTypeToxin[i] = 0;
		}
	}

	@Override
	public void loadNew() {
		update();
	}

}
