package org.tfcode.project;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectCreator {
    public static final String USAGE = "java -jar project-creator(...).jar <command> <args> ...";

    private Logger logger = LoggerFactory.getLogger(ProjectCreator.class);

    public static enum COMMAND {
        create("Create a project", CreateProject.class);

        String description = "";
        Class<?> cmd = null;

        private COMMAND(String desc, Class<?> cmd) {
            description = desc;
            this.cmd = cmd;
        }
    }

    public ProjectCreator() {
    }

    public static void printHelp() {
        System.out.println("\nUsage: " + USAGE + "\n\nAvailable commands:\n");

        for (COMMAND cmd : COMMAND.values()) {
            System.out.println(cmd.toString() + "  -  " + cmd.description);
        }

        System.out.println("\nFor more info about each command try (java -jar ...) COMMAND -h\n");
        System.out.println("\n---------------------------\n\n");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printHelp();
            return;
        }
        ProjectCreator app = new ProjectCreator();

        String cmd = args[0];
        String[] cargs = (args.length > 1) ? Arrays.copyOfRange(args, 1, args.length) : new String[] {};
        COMMAND com = null;

        try {
            com = COMMAND.valueOf(cmd);
        } catch (Exception e) {
            throw e;
        }

        app.logger.info("Running command " + cmd);
        ((ApplicationCommand) com.cmd.newInstance()).run(cargs);
    }
}
