package org.tfcode.project;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public abstract class AbstractCommandParameters {
    protected Options           options    = new Options();
    protected CommandLineParser parser     = new DefaultParser();
    protected HelpFormatter     hformatter = new HelpFormatter();
    protected CommandLine       cli;
    
    protected String commandTitle = "Command";
    protected String helpHeader   = "+++";
    protected String helpFooter   = "***";
    
    abstract protected void defineOptions();
}
