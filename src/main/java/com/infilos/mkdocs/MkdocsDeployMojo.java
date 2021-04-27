package com.infilos.mkdocs;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "gh-deploy", threadSafe = true, defaultPhase = LifecyclePhase.DEPLOY)
public class MkdocsDeployMojo extends BaseMkdocsMojo {

    @Parameter(property = "mkdocs.clean", defaultValue = "true")
    private boolean clean;

    @Parameter(property = "mkdocs.deploy.message")
    private String message;

    @Parameter(property = "mkdocs.deploy.branch")
    private String branch;

    @Parameter(property = "mkdocs.deploy.name")
    private String name;

    @Parameter(property = "mkdocs.deploy.force")
    private boolean force;
    
    @Override
    protected void perform() throws MojoExecutionException, MojoFailureException, IOException {
        List<String> args = new ArrayList<String>(){{
            add(command);
            add("gh-deploy");
        }};

        if(getLog().isDebugEnabled()) {
            args.add("--verbose");
        }

        if (clean) {
            args.add("--clean");
        } else {
            args.add("--dirty");
        }

        if(config!=null) {
            args.add("--config-file");
            args.add(config.toString());
        }

        if (message != null) {
            args.add("--message");
            args.add(message);
        }

        if (branch != null) {
            args.add("--remote-branch");
            args.add(branch);
        } else {
            args.add("--remote-branch");
            args.add("gh-pages");
        }

        if (name != null) {
            args.add("--remote-name");
            args.add(name);
        }

        if (force) {
            args.add("--force");
        }

        invoke(args, dir);
    }
}
