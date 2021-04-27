package com.infilos.mkdocs;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "serve", requiresDirectInvocation = true)
public class MkdocsServeMojo extends BaseMkdocsMojo {

    @Parameter(property = "mkdocs.serve.host")
    private String host;

    @Parameter(property = "mkdocs.serve.port")
    private Integer port;

    @Parameter(property = "mkdocs.serve.theme")
    private String theme;

    @Parameter(property = "mkdocs.serve.themedir")
    private String themedir;

    @Parameter(property = "mkdocs.serve.strict", defaultValue = "false")
    private boolean strict;

    @Parameter(property = "mkdocs.serve.rolling", defaultValue = "true")
    private boolean rolling;

    @Override
    protected void perform() throws MojoExecutionException, MojoFailureException, IOException {
        List<String> args = new ArrayList<String>() {{
            add(command);
            add("serve");
        }};

        if (getLog().isDebugEnabled()) {
            args.add("--verbose");
        }

        if (strict) {
            getLog().info("Strict mode is enabled");
            args.add("--strict");
        }

        if (config!=null) {
            args.add("--config-file");
            args.add(config.toString());
        }

        if (host!=null || port!=null) {
            String addr = (host!=null ? host:"localhost")
                + ":"
                + (port!=null ? port:8000);
            args.add("--dev-addr");
            args.add(addr);
        }

        if (theme!=null) {
            args.add("--theme");
            args.add(theme);
        }

        if (themedir!=null) {
            args.add("--theme-dir");
            args.add(themedir.toString());
        }

        if (rolling) {
            args.add("--livereload");
        } else {
            args.add("--no-livereload");
        }

        invoke(args, dir);
    }
}
