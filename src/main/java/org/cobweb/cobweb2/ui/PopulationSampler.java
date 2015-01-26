package org.cobweb.cobweb2.ui;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cobweb.cobweb2.Simulation;
import org.cobweb.cobweb2.core.Agent;
import org.cobweb.cobweb2.core.Direction;
import org.cobweb.cobweb2.core.Location;
import org.cobweb.cobweb2.core.LocationDirection;
import org.cobweb.cobweb2.impl.ComplexAgent;
import org.cobweb.cobweb2.impl.ComplexAgentParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class PopulationSampler {


	/** Save a sample population as an XML file */
	public static void savePopulation(Simulation sim, String popName, String option, int amount) {

		int totalPop;

		if (option.equals("percentage")) {
			totalPop = sim.theEnvironment.getAgentCount() * amount / 100;
		} else {
			int currPop = sim.theEnvironment.getAgentCount();
			if (amount > currPop) {

				totalPop = currPop;
			} else {

				totalPop = amount;
			}
		}

		Document d;
		try {
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		}
		Node root = d.createElement("Agents");

		int currentPopCount = 1;

		for (Agent agent : sim.theEnvironment.getAgents()) {
			if (currentPopCount > totalPop)
				break;

			//FIXME!!! Node node = saveAgent(serializer, (ComplexAgent) agent, d);

			//FIXME!!! root.appendChild(node);

			currentPopCount++;
		}

		d.appendChild(root);
	}

	/**
	 * Loads agent population saved with savePopulation()
	 * @param fileName path to population file
	 * @param replace delete current population before inserting
	 */
	public static void insertPopulation(Simulation sim, String fileName, boolean replace) {
		if (replace) {
			sim.theEnvironment.clearAgents();
		}

		try {
			FileInputStream file = new FileInputStream(fileName);

			// DOM initialization
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			factory.setIgnoringComments(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			file.close();

			NodeList agents = document.getElementsByTagName("Agent");

			for (int i = 0 ; i < agents.getLength(); i++){
				ComplexAgentParams params = new ComplexAgentParams(sim.simulationConfig.envParams);

				Node agent = agents.item(i);
				Element element = (Element) agent;

				NodeList paramsElement = element.getElementsByTagName("params");
				Element paramNode = (Element) paramsElement.item(0);

				NodeList pdCheaterElement = element.getElementsByTagName("doCheat");

				Element location = (Element)element.getElementsByTagName("location").item(0);
				Location loc = new Location(
						Integer.parseInt(location.getAttribute("x")),
						Integer.parseInt(location.getAttribute("y")));

				Element direction = (Element)element.getElementsByTagName("direction").item(0);
				Direction facing = new Direction(
						Integer.parseInt(direction.getAttribute("x")),
						Integer.parseInt(direction.getAttribute("y")));

				LocationDirection locDir = new LocationDirection(loc, facing);


				//FIXME!!! serializer.load(params, paramNode);

				// doCheat
				boolean pdCheater = Boolean.parseBoolean(pdCheaterElement.item(0).getChildNodes().item(0).getNodeValue());

				// FIXME plugin params: production, disease, etc

				ComplexAgent cAgent = (ComplexAgent) sim.newAgent(params.type);
				cAgent.init(sim.theEnvironment, locDir, params);
				cAgent.pdCheater = pdCheater;
				sim.theEnvironment.setAgent(loc, cAgent);
			}
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			throw new RuntimeException("Can't open config file", ex);
		}
	}

}
