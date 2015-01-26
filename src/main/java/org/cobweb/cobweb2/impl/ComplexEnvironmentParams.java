/**
 *
 */
package org.cobweb.cobweb2.impl;

import org.cobweb.io.ConfDisplayName;
import org.cobweb.io.ConfXMLTag;
import org.cobweb.io.ParameterSerializable;

/**
 * Parameters for the ComplexEnvironment
 */
public class ComplexEnvironmentParams implements AgentFoodCountable, ParameterSerializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3308627358945982393L;

	/**
	 * Number of Agent types.
	 */
	@ConfDisplayName("Agent types")
	@ConfXMLTag("AgentTypeCount")
	public int agentTypeCount = 4;

	/**
	 * Class name of the controller object.
	 */
	@ConfDisplayName("Controller type")
	@ConfXMLTag("ControllerName")
	public String controllerName;

	/**
	 * Class name of the agent object
	 */
	@ConfDisplayName("Agent type")
	@ConfXMLTag("AgentName")
	public String agentName;

	/**
	 * Class name of environment object
	 */
	@ConfDisplayName("Environment type")
	@ConfXMLTag("EnvironmentName")
	public String environmentName;

	/**
	 * Width of the grid.
	 */
	@ConfDisplayName("Width")
	@ConfXMLTag("Width")
	public int width = 80;

	/**
	 * Height of the grid.
	 */
	@ConfDisplayName("Height")
	@ConfXMLTag("Height")
	public int height = 80;

	/**
	 * Enables the grid to wrap around at the edges.
	 */
	@ConfDisplayName("Wrap edges")
	@ConfXMLTag("wrap")
	public boolean wrapMap = true;


	/**
	 * Random number generator seed for repeating the simulation exactly.
	 */
	@ConfDisplayName("Random seed")
	@ConfXMLTag("randomSeed")
	public long randomSeed = 42;


	/**
	 * Keeps the existing food on the grid.
	 */
	@ConfDisplayName("Keep old array")
	@ConfXMLTag("keepOldArray")
	public boolean keepOldArray = false;

	/**
	 * Spawns new food on the grid.
	 */
	@ConfDisplayName("Drop new food")
	@ConfXMLTag("dropNewFood")
	public boolean dropNewFood = true;

	/**
	 * Keeps existing waste on the grid.
	 */
	@ConfDisplayName("Keep old waste")
	@ConfXMLTag("keepOldWaste")
	public boolean keepOldWaste = false;

	/**
	 * Keeps existing agents.
	 */
	@ConfDisplayName("Keep old agents")
	@ConfXMLTag("keepOldAgents")
	public boolean keepOldAgents = false;

	/**
	 * Spawns new agents.
	 */
	@ConfDisplayName("Spawn new agents")
	@ConfXMLTag("spawnNewAgents")
	public boolean spawnNewAgents = true;

	/**
	 * Keeps old communication packets.
	 */
	@ConfDisplayName("Keep old packets")
	@ConfXMLTag("keepOldPackets")
	public boolean keepOldPackets = false;

	/**
	 * Number of stones to randomly place
	 */
	@ConfDisplayName("Random stones")
	@ConfXMLTag("randomStones")
	public int initialStones = 10;


	/**
	 * Probability that food will grow around similar existing food.
	 */
	@ConfDisplayName("Like food probability")
	@ConfXMLTag("likeFoodProb")
	public float likeFoodProb = 0;


	/**
	 * Agents can play prisoner's dilemma game when they meet.
	 */
	@ConfDisplayName("Prisoner's dilemma")
	@ConfXMLTag("PrisDilemma")
	public boolean prisDilemma = false;

	/**
	 * Prisoner's dilemma parameters.
	 */
	@ConfXMLTag("pd")
	public PDParams pdParams = new PDParams();

	/**
	 * Initialises the default parameters
	 */
	public ComplexEnvironmentParams() {
		// public, no parameter constructor for serialization
	}

	/**
	 * @return Number of agent types.
	 */
	@Override
	public int getAgentTypes() {
		return agentTypeCount;
	}

	/**
	 * @return Height of the grid.
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * @return Width of the grid.
	 */
	@Override
	public int getWidth() {
		return width;
	}

}