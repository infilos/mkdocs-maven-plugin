package com.infilos.mkdocs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name="build", threadSafe = true)
public class MkdocsBuildMojo extends BaseMkdocsMojo {
    
    @Parameter(property = "mkdocs.theme")
    private String theme;

    @Parameter(property = "mkdocs.themedir")
    private File themedir;

    @Parameter(property = "mkdocs.strict", defaultValue = "false")
    private boolean strict;
    
    @Parameter(property = "mkdocs.output", defaultValue = "${project.build.directory}/mkdocs")
    private File output;
    
    @Parameter(property = "mkdocs.clean", defaultValue = "true")
    private boolean clean;
    
    @Override
    protected void perform() throws MojoExecutionException, MojoFailureException, IOException {
        List<String> args = new ArrayList<String>(){{
            add(command);
            add("build");
        }};
        
        if(getLog().isDebugEnabled()) {
            args.add("--verbose");
        }
        
        if(strict) {
            getLog().info("Strict mode enabled");
            args.add("--strict");
        }
        
        if(clean) {
            args.add("--clean");
        } else {
            args.add("--dirty");
        }
        
        if(config!=null) {
            args.add("--config-file");
            args.add(config.toString());
        }
        
        if(theme != null) {
            args.add("--theme");
            args.add(theme);
        }
        
        if(themedir!= null) {
            args.add("--theme-dir");
            args.add(themedir.toString());
        }
        
        args.add("--site-dir");
        args.add(output.toString());
        
        invoke(args, dir);
    }
}
