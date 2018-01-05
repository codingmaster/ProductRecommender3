package de.hpi.semrecsys.main;

import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.populator.Populator;
import de.hpi.semrecsys.populator.Populator.PopulationOption;

import static org.junit.Assert.assertTrue;

/***
 * Class used for the population of the virtuoso data server
 * @author Michael Wolowyk
 *
 */
public class PopulatorMain {

	static SemRecSysConfigurator configurator = SemRecSysConfigurator.getDefaultConfigurator();

	static Populator populator = new Populator(configurator);

	private static int numberOfProducts = 1000;
	static boolean clean = true;


	public static void main(String[] args) {
		PopulationOption[] options = { PopulationOption.all};
		execute(options);
	}

	/***
	 * Executes population of the Virtuoso data server
	 * @param options configuration options
	 */
	public static void execute(PopulationOption... options) {
		populator.populate(numberOfProducts, clean, options);
	}

	/**
	 * Deletes graphs from the Virtuoso Datasource
	 * @param graphs graphs to delete
	 */
	public static void cleanGraphs(String[] graphs) {
		for (String graphName : graphs) {
			populator.cleanGraph(graphName);
			assertTrue(0 == populator.getGraphSize(graphName));
		}
	}

}
