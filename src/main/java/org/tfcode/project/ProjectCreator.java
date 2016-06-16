package org.tfcode.project;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ProjectCreator {
    public static final String USAGE = "java -jar project-creator(...).jar <command> <args> ...";

    private Logger logger = LoggerFactory.getLogger(ProjectCreator.class);
    
    private Map<String,AppCommand> commands;
    
    private class AppCommand {
        String   name;
        String   description;
        Class<?> cmd;
    }

    public ProjectCreator() throws Exception {
        findCommands();
    }
    
    @SuppressWarnings("unchecked")
    private void findCommands() throws Exception {
        FastClasspathScanner scanner = new FastClasspathScanner("org.tfcode");
        scanner.scan();
        Command    annotation;
        boolean    isCommand;
        AppCommand cmd;
        commands = new HashMap<>();
        for (String cl : scanner.getNamesOfAllClasses()) {
            //logger.info(cl);
            Class c = Class.forName(cl);
            if (c.getAnnotation(Command.class) != null) {
                isCommand = false;
                for (Class ic : Arrays.asList(c.getInterfaces())) {
                    if (ic.getName().equals("org.tfcode.project.ApplicationCommand")) {
                        logger.info(ic.toString());
                        isCommand = true;
                    }
                }
                if (!isCommand) continue;
                annotation = (Command)c.getAnnotation(Command.class);
                if (commands.containsKey(annotation.name())) {
                    throw new UnsupportedOperationException("Command name " +
                              annotation.name() +
                              " already exists");
                }
                cmd = new AppCommand();
                cmd.name        = annotation.name();
                cmd.description = annotation.description();
                cmd.cmd         = c;
                commands.put(cmd.name, cmd);
                logger.info(annotation.name());
                logger.info(annotation.description());
                
            }
        }
    }

    public void printHelp() {
        System.out.println("\nUsage: " + USAGE + "\n\nAvailable commands:\n");

        for (String cmd : commands.keySet()) {
            System.out.println(cmd + "  -  " + commands.get(cmd).description);
        }

        System.out.println("\nFor more info about each command try (java -jar ...) COMMAND -h\n");
        System.out.println("\n---------------------------\n\n");
    }

    public static void main(String[] args) throws Exception {
        ProjectCreator app = new ProjectCreator();
        if (args.length < 1) {
            app.printHelp();
            return;
        }
        String cmd = args[0];
        String[] cargs = (args.length > 1) ? Arrays.copyOfRange(args, 1, args.length) : new String[] {};
        AppCommand com = null;

        com = app.commands.get(cmd);
        if (com == null) {
            app.printHelp();
            throw new IllegalArgumentException("Unknown command " + cmd);
        }

        app.logger.info("Running command " + cmd);
        ((ApplicationCommand) com.cmd.newInstance()).run(cargs);
    }
}
