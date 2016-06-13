package org.tfcode.project;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a project structure in the specified output folder.
 * <p>
 * An example config file:
 * <code>
 * <pre>
   {
     "project_name": "ExampleProject",
     "out_folder":   "test_project",
     "main_class":   "MainApp",
     "package":      "org.template"
   }
 * </pre>
 * </code>
 * </p>
 * 
 * @author me
 *
 */
public class CreateProject implements ApplicationCommand {

    private Logger logger = LoggerFactory.getLogger(CreateProject.class);

    private CreateProjectParameters params = null;

    protected String projectPath     = "";
    protected String srcFolder       = "";
    protected String resourcesFolder = "";
    protected String packageFolder   = "";
    protected String mainClassFull   = "";

    @Override
    public void run(String[] args) throws Exception {
        setParameters(args);
        setPaths();
        
        createGradleScript();
        createAppCommand();
        createCommandParameters();
        createMainClass();
        createHelloWorld();
        createHelloWorldParameters();
        createGitIgnore();
        createLogbackXml();
    }

    protected void setPaths() throws Exception {
        projectPath = params.getOutputFolder() + File.separator + params.getProjectName();
        logger.info("Creating " + projectPath + " (if not exists)");
        FileUtils.forceMkdir(new File(projectPath));
        
        srcFolder = projectPath + File.separator + "src" + File.separator + "main" +
                      File.separator + "java";
        logger.info("Creating " + srcFolder + " (if not exists)");
        FileUtils.forceMkdir(new File(srcFolder));
        
        String pckg = params.getPackage().replaceAll("\\.", File.separator);
        packageFolder = srcFolder + File.separator + pckg;
        logger.info("Creating " + packageFolder + " (if not exists)");
        FileUtils.forceMkdir(new File(packageFolder));
        
        resourcesFolder = projectPath + File.separator + "src" + File.separator + "main" +
                            File.separator + "resources";
        logger.info("Creating " + resourcesFolder + " (if not exists)");
        FileUtils.forceMkdir(new File(resourcesFolder));
        
        String testFolder = projectPath + File.separator + "src" + File.separator + "test" +
                File.separator + "java";
        logger.info("Creating " + testFolder + " (if not exists)");
        FileUtils.forceMkdir(new File(testFolder));
        
        String testResourcesFolder = projectPath + File.separator + "src" + File.separator + "test" +
                File.separator + "resources";
        logger.info("Creating " + testResourcesFolder + " (if not exists)");
        FileUtils.forceMkdir(new File(testResourcesFolder));
  
        mainClassFull = params.getPackage() + "." + params.getMainClass();
    }
    
    protected void createLogbackXml() throws Exception {
        logger.info("Creating " + resourcesFolder + File.separator + "logback.xml");
        FileWriter fw = new FileWriter(resourcesFolder + File.separator + "logback.xml");
        fw.write("<configuration>\n" + 
                "\n" + 
                "    <appender name=\"STDOUT\" class=\"ch.qos.logback.core.ConsoleAppender\">\n" + 
                "        <!-- encoders are assigned the type ch.qos.logback.classic.encoder.PatternLayoutEncoder \n" + 
                "            by default -->\n" + 
                "        <encoder>\n" + 
                "            <pattern>%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>\n" + 
                "        </encoder>\n" + 
                "    </appender>\n" + 
                "\n" + 
                "    <!-- <appender name=\"FILE\" class=\"ch.qos.logback.core.FileAppender\"> <file>" + params.getProjectName() + ".log</file> \n" + 
                "        <append>false</append> <encoder> <pattern>%date %level [%thread] %logger{10} \n" + 
                "        [%file:%line] - %msg%n</pattern> </encoder> </appender> -->\n" + 
                "\n" + 
                "    <root level=\"info\">\n" + 
                "        <appender-ref ref=\"STDOUT\" />\n" + 
                "        <!-- appender-ref ref=\"FILE\" / -->\n" + 
                "    </root>\n" + 
                "</configuration>\n" + 
                "");
        fw.close();
    }
    
    protected void createGitIgnore() throws Exception {
        logger.info("Creating " + projectPath + File.separator + ".gitignore");
        FileWriter fw = new FileWriter(projectPath + File.separator + ".gitignore");
        fw.write(".classpath\n" + 
                ".gradle/\n" + 
                ".project\n" + 
                ".settings/\n" + 
                "bin/\n" + 
                "build.gradle\n" + 
                "build/\n" + 
                "src/\n" + 
                "data/\n" + 
                "libs/\n");
        fw.close();
    }
    
    protected void createHelloWorldParameters() throws Exception {
        logger.info("Creating " + packageFolder + File.separator + "HelloWorldParameters.java");
        FileWriter fw = new FileWriter(packageFolder + File.separator +  "HelloWorldParameters.java");
        fw.write("package " + params.getPackage() + ";\n" +
                 "\n" + 
                 "import org.slf4j.Logger;\n" + 
                 "import org.slf4j.LoggerFactory;\n" + 
                 "\n" + 
                 "\n" + 
                 "public class HelloWorldParameters extends AbstractCommandParameters {\n" + 
                 "    private Logger logger = LoggerFactory.getLogger(HelloWorldParameters.class);\n" + 
                 "    \n" + 
                 "    private HelloWorldParameters() {\n" + 
                 "    }\n" + 
                 "    \n" + 
                 "    public HelloWorldParameters(String args[]) throws Exception {\n" + 
                 "        init();\n" + 
                 "        defineOptions();\n" + 
                 "        setParameters(args);\n" +
                 "    }\n" + 
                 "    \n" + 
                 "    private void init() {\n" + 
                 "        commandTitle = \"Hello world\";\n" + 
                 "    }\n" + 
                 "    \n" +
                 "    public void setParameters(String args[]) throws Exception {\n" +
                 "        cli = parser.parse(options, args);\n" + 
                 "        if (cli.hasOption(\"h\")) {\n" + 
                 "            printHelp();\n" + 
                 "            System.exit(0);\n" + 
                 "        }\n" +
                 "    }\n" +
                 "    \n" +
                 "    @Override\n" + 
                 "    protected void defineOptions() {\n" + 
                 "        addHelpOption();\n" + 
                 "    }\n" +
                 "}\n"
                );
        fw.close();
    }
    
    
    protected void createHelloWorld() throws Exception {
        logger.info("Creating " + packageFolder + File.separator + "HelloWorld.java");
        FileWriter fw = new FileWriter(packageFolder + File.separator +  "HelloWorld.java");
        fw.write("package " + params.getPackage() + ";\n" + 
                "\n" + 
                "import org.slf4j.Logger;\n" + 
                "import org.slf4j.LoggerFactory;\n" + 
                "\n" + 
                "\n" + 
                "public class HelloWorld implements ApplicationCommand {\n" + 
                "\n" + 
                "    private Logger logger = LoggerFactory.getLogger(HelloWorld.class);\n" + 
                "\n" + 
                "    private HelloWorldParameters params = null;\n" + 
                "\n" + 
                "    @Override\n" + 
                "    public void run(String[] args) throws Exception {\n" + 
                "        params = new HelloWorldParameters(args);\n" + 
                "        \n" + 
                "        logger.info(\"Hello world!\");\n" + 
                "    }\n" + 
                "}\n");
        fw.close();
    }
    
    protected void createMainClass() throws Exception {
        logger.info("Creating " + packageFolder + File.separator + params.getMainClass() + ".java");
        FileWriter fw = new FileWriter(packageFolder + File.separator + params.getMainClass() + ".java");
        fw.write("package " + params.getPackage() + ";\n" + 
                "\n" + 
                "import java.util.Arrays;\n" + 
                "\n" + 
                "import org.slf4j.Logger;\n" + 
                "import org.slf4j.LoggerFactory;\n" + 
                "\n" + 
                "public class " + params.getMainClass() + " {\n" + 
                "    public static final String USAGE = \"java -jar (...).jar <command> <args> ...\";\n" + 
                "\n" + 
                "    private Logger logger = LoggerFactory.getLogger(" + params.getMainClass() + ".class);\n" + 
                "\n" + 
                "    public static enum COMMAND {\n" + 
                "        hello_world(\"Hello world\", HelloWorld.class);\n" + 
                "\n" + 
                "        String description = \"\";\n" + 
                "        Class<?> cmd = null;\n" + 
                "\n" + 
                "        private COMMAND(String desc, Class<?> cmd) {\n" + 
                "            description = desc;\n" + 
                "            this.cmd = cmd;\n" + 
                "        }\n" + 
                "    }\n" + 
                "\n" + 
                "    public " + params.getMainClass() + "() {\n" + 
                "    }\n" + 
                "\n" + 
                "    public static void printHelp() {\n" + 
                "        System.out.println(\"\\nUsage: \" + USAGE + \"\\n\\nAvailable commands:\\n\");\n" + 
                "\n" + 
                "        for (COMMAND cmd : COMMAND.values()) {\n" + 
                "            System.out.println(cmd.toString() + \"  -  \" + cmd.description);\n" + 
                "        }\n" + 
                "\n" + 
                "        System.out.println(\"\\nFor more info about each command try (java -jar ...) COMMAND -h\\n\");\n" + 
                "        System.out.println(\"\\n---------------------------\\n\\n\");\n" + 
                "    }\n" + 
                "\n" + 
                "    public static void main(String[] args) throws Exception {\n" + 
                "        if (args.length < 1) {\n" + 
                "            printHelp();\n" + 
                "            return;\n" + 
                "        }\n" + 
                "        " + params.getMainClass() + " app = new " + params.getMainClass() + "();\n" + 
                "\n" + 
                "        String cmd = args[0];\n" + 
                "        String[] cargs = (args.length > 1) ? Arrays.copyOfRange(args, 1, args.length) : new String[] {};\n" + 
                "        COMMAND com = null;\n" + 
                "\n" + 
                "        try {\n" + 
                "            com = COMMAND.valueOf(cmd);\n" + 
                "        } catch (Exception e) {\n" + 
                "            throw e;\n" + 
                "        }\n" + 
                "\n" + 
                "        app.logger.info(\"Running command \" + cmd);\n" + 
                "        ((ApplicationCommand) com.cmd.newInstance()).run(cargs);\n" + 
                "    }\n" + 
                "}\n");
        fw.close();
    }
    
    protected void createAppCommand() throws Exception {
        logger.info("Creating " + packageFolder + File.separator + "ApplicationCommand.java");
        FileWriter fw = new FileWriter(packageFolder + File.separator + "ApplicationCommand.java");
        
        fw.write("package " + params.getPackage() + ";\n" + 
                "\n" + 
                "public interface ApplicationCommand {\n" + 
                "    public void run(String[] args) throws Exception;\n" + 
                "}");
        fw.close();
    }
    
    protected void createCommandParameters() throws Exception {
        logger.info("Creating " + packageFolder + File.separator + "AbstractCommandParameters.java");
        FileWriter fw = new FileWriter(packageFolder + File.separator + "AbstractCommandParameters.java");
        fw.write("package " + params.getPackage() + ";\n\n" + 
                "import org.apache.commons.cli.CommandLine;\n" + 
                "import org.apache.commons.cli.CommandLineParser;\n" + 
                "import org.apache.commons.cli.DefaultParser;\n" + 
                "import org.apache.commons.cli.HelpFormatter;\n" + 
                "import org.apache.commons.cli.Options;\n" + 
                "\n" + 
                "public abstract class AbstractCommandParameters {\n" + 
                "    protected Options           options    = new Options();\n" + 
                "    protected CommandLineParser parser     = new DefaultParser();\n" + 
                "    protected HelpFormatter     hformatter = new HelpFormatter();\n" + 
                "    protected CommandLine       cli;\n" + 
                "    \n" + 
                "    protected String commandTitle = \"Command\";\n" + 
                "    protected String helpHeader   = \"+++\";\n" + 
                "    protected String helpFooter   = \"***\";\n" + 
                "    \n" + 
                "    abstract protected void defineOptions();\n" +
                "\n" +
                "    public void printHelp() {\n" + 
                "        hformatter.setWidth(120);\n" + 
                "        hformatter.printHelp(commandTitle, helpHeader, options, helpFooter);\n" + 
                "    }\n" + 
                "\n" + 
                "    protected void addHelpOption() {\n" + 
                "        options.addOption(\"h\", \"help\", false, \"print this text and exit\");\n" + 
                "    }\n" +
                "}\n"
                );
        fw.close();
    }
    
    protected void createGradleScript() throws Exception {
        FileWriter fw = new FileWriter(projectPath + File.separator + "build.gradle");
        fw.write("apply plugin: 'java'\n" + 
                "apply plugin: 'eclipse'\n" + 
                "\n" + 
                "sourceCompatibility = 1.8\n" + 
                "targetCompatibility = 1.8\n" + 
                "version = '1.0'\n" + 
                "\n" + 
                "jar {\n" + 
                "    baseName = '" + params.getProjectName() + "'\n" + 
                "    version =  '1.0'\n" + 
                "    \n" + 
                "    manifest {\n" + 
                "        attributes 'Implementation-Title': '" + params.getProjectName() + "',\n" + 
                "                   'Implementation-Version': version,\n" + 
                "                   'Main-Class': '" + mainClassFull + "'\n" + 
                "    }\n" + 
                "    \n" + 
                "    dependsOn configurations.compile\n" + 
                "    from {\n" + 
                "        configurations.compile.collect {\n" + 
                "            it.isDirectory() ? it : zipTree(it)\n" + 
                "        }\n" + 
                "    }\n" + 
                "}\n" + 
                "\n" + 
                "repositories {\n" + 
                "    mavenCentral()\n" + 
                "}\n" + 
                "\n" + 
                "\n" + 
                "dependencies {\n" + 
                "    compile 'commons-cli:commons-cli:1.3.1',\n" + 
                "            'commons-io:commons-io:2.4',\n" + 
                "            'ch.qos.logback:logback-classic:1.1.3',\n" + 
                "            'io.vertx:vertx-core:3.2.0',\n" + 
                "            'io.vertx:vertx-web:3.2.0'\n" + 
                "    testCompile group: 'junit', name: 'junit', version: '4.+'\n" + 
                "}\n" + 
                "\n" + 
                "task copyDependencies(type: Copy) {\n" + 
                "    description = 'Copy depencies to libs folder, allowing build path inclusion in Eclipse'\n" + 
                "    def libDir = new File(project.projectDir, '/libs')\n" + 
                "    println libDir\n" + 
                "    println 'Adding dependencies from compile configuration'\n" + 
                "    configurations.compile.each { File file ->\n" + 
                "        println 'Added ' + file\n" + 
                "        copy {\n" + 
                "            from file\n" + 
                "            into libDir\n" + 
                "        }\n" + 
                "    }\n" + 
                "}\n" + 
                "\n" + 
                "\n" + 
                "test {\n" + 
                "    systemProperties 'property': 'value'\n" + 
                "}\n" + 
                "\n" + 
                "task wrapper(type: Wrapper) {\n" + 
                "    gradleVersion = '2.10'\n" + 
                "}\n" + 
                "\n" + 
                "/*\n" + 
                "uploadArchives {\n" + 
                "    repositories {\n" + 
                "       flatDir {\n" + 
                "           dirs 'repos'\n" + 
                "       }\n" + 
                "    }\n" + 
                "}\n" + 
                "*/");
        fw.close();
    }

    public void setParameters(String[] args) throws Exception {
        setParameters(new CreateProjectParameters(args));
    }

    public void setParameters(CreateProjectParameters params) {
        this.params = params;
    }
}
