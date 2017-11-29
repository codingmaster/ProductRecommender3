package de.hpi.semrecsys.main;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import de.hpi.semrecsys.populator.Populator.PopulationOption;
import de.hpi.semrecsys.utils.CollectionUtils;
import de.hpi.semrecsys.utils.FileUtils;

/**
 * Main class for command line tool
 * @author Michael Wolowyk
 *
 */
public class Main {

	static String conf = "--conf";
	static String dbinit = "--dbinit";
	static String recommend = "--recommend";
	static String populate = "--populate";
	static String help = "help";
	public static String confPath = "docs/conf";
	static String path;
    static Logger log = Logger.getLogger("Recommender");

	public static void main(String[] args) {
		String[] commands = { conf, dbinit, recommend, populate, help };
		List<String> commandsList = Arrays.asList(commands);
		List<String> argsList = Arrays.asList(args);
		if (args.length < 1) {
			System.out.println("help");
			printHelp();
		}
		if (argsList.contains(conf)) {
			initConf(commandsList, argsList);
		}
		if (path == null) {
			setPath(getDefaultPath());
		}
		if (argsList.contains(dbinit)) {
			log.info("Executing DB Initialisation");
			DatabaseInitialiser.execute();
		}

		if (argsList.contains(populate)) {
            log.info("Executing Virtuoso population");

            int commandIndex = argsList.indexOf(populate);
            if(args.length > commandIndex + 1){
                String opt = argsList.get(commandIndex + 1);
                PopulationOption option = PopulationOption.valueOf(opt);
                log.info("With option: " + option.name());
                PopulatorMain.execute(option);
            }
            else{
                PopulatorMain.execute(PopulationOption.all);
            }
        }

		if (argsList.contains(recommend)) {
            log.info("Executing Recommendation");
			int commandIndex = argsList.indexOf(recommend);
			String opt = argsList.get(commandIndex + 1);
			if (opt.equalsIgnoreCase("all")) {
				RecommenderMain.execute();
			} else {
				RecommenderMain.execute(Integer.valueOf(opt));
			}
		}
		if (!argsList.isEmpty() && CollectionUtils.getIntersection(commandsList, argsList).isEmpty()) {
            log.info("not recognised command.");
			printHelp();
		}

	}

	private static void initConf(List<String> commandsList, List<String> argsList) {
		int commandIndex = argsList.indexOf(conf);
        log.info("Configuration");
		if (argsList.size() < commandIndex + 1 || commandsList.contains(argsList.get(commandIndex + 1))) {
            log.info("Path parameter for " + conf + " is missing");
			String path = getDefaultPath();
            log.info("Default path is " + path);

		} else {
			path = argsList.get(commandIndex + 1);
			setPath(path);
		}
	}

	private static void setPath(String path) {
        log.info("Configuration path is set to " + new File(path).getAbsolutePath());
		FileUtils.writeTextToFile(path, confPath, false);
	}

	private static String getDefaultPath() {
		path = FileUtils.getCurrentPath().getParentFile().getAbsolutePath() + "/docs/properties";
		return path;
	}

	private static void printHelp() {
		String usage = "Usage:\n" + "--conf <PROPS_DIR_PATH>\n" + "--dbinit\n"
                + "--populate <POPULATE_OPTION>\n" + "POPULATE_OPTION : ";
        for(PopulationOption option : PopulationOption.values()){
            usage+=option.name() + ", ";
        }
		usage+= "\n--recommend <RECOMMEND_OPTION>\n" + "RECOMMEND_OPTION : productId or all";
        log.info(usage);
	}
}
