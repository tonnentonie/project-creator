package org.tfcode.project;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

public class CreateProjectParameters extends AbstractCommandParameters {
    private Logger logger = LoggerFactory.getLogger(CreateProjectParameters.class);
    
    
    protected String     configFile = "";
    protected JsonObject config     = null;
    
    protected String projectName = "";
    protected String outfolder   = ".";
    protected String mainClass   = "MainClass";
    protected String packageName = "org.template";
    

    public CreateProjectParameters() {
        init();
        defineOptions();
    }

    public CreateProjectParameters(String args[]) throws Exception {
        init();
        defineOptions();
        setParameters(args);
    }
    
    private void init() {
        commandTitle = "Create project";
    }
    
    public String getProjectName() { return projectName; }
    public String getOutputFolder() { return outfolder; }
    public String getMainClass() { return mainClass; }
    public String getPackage() { return packageName; }
    
    @Override
    protected void defineOptions() {
        addHelpOption();
        addConfigFileOption();
    }

    public void setParameters(String[] args) throws Exception {
        cli = parser.parse(options, args);
        if (cli.hasOption("h")) {
            printHelp();
            // Do something nicer to exit here ...
            System.exit(0);
        }

        if (cli.hasOption("c")) {
            configFile = cli.getOptionValue("c");
            setParameters(configFile);
        } else {
            throw new IllegalArgumentException("Please specify a config file");
        }
    }

    public void setParameters(String configFile) throws Exception {
        StringBuilder  sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(configFile));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        config = new JsonObject(sb.toString());
        br.close();
        sb.setLength(0);
        sb = null;
        
        setProjectName();
        setOutputFolder();
        setPackage();
        setMainClass();
    }
    
    protected void setProjectName() throws Exception {
        if (config.containsKey("project_name")) {
            projectName = config.getString("project_name");
            if (projectName.length() < 1) {
                throw new IllegalArgumentException("Please provide a project name with length > 0");
            }
        } else {
            throw new IllegalArgumentException("Please provide a project name");
        }
    }
    
    protected void setOutputFolder() throws Exception {
        outfolder = ".";
        if (config.containsKey("out_folder")) {
            outfolder = config.getString("out_folder");
            if (outfolder.length() < 1) {
                throw new IllegalArgumentException("Please provide an output folder name with length > 0");
            }
        }
    }
    
    protected void setPackage() throws Exception {
        if (config.containsKey("package")) {
            packageName = config.getString("package");
        }
    }
    
    protected void setMainClass() throws Exception {
        if (config.containsKey("main_class")) {
            mainClass = config.getString("main_class");
        }
    }

    protected void addConfigFileOption() {
        Option opt = new Option("c", true, "configuration file in JSON format");
        opt.setRequired(false);
        opt.setLongOpt("config");
        opt.setType(String.class);
        options.addOption(opt);
    }

    public void setCommandTitle(String title) throws Exception {
        if (title.isEmpty())
            throw new IllegalArgumentException("Title should not be empty");
        commandTitle = title;
    }

    public void printHelp() {
        hformatter.setWidth(120);
        hformatter.printHelp(commandTitle, helpHeader, options, helpFooter);
    }

    protected void addHelpOption() {
        options.addOption("h", "help", false, "print this text and exit");
    }
}
