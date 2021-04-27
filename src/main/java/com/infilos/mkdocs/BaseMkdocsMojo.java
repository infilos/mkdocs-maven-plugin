package com.infilos.mkdocs;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;

public abstract class BaseMkdocsMojo extends AbstractMojo {

    @Parameter(property = "mkdocs.skip", defaultValue = "false")
    protected boolean skip;

    @Parameter(property = "mkdocs.dir", defaultValue = "${basedir}")
    protected File dir;

    @Parameter(property = "mkdocs.config")
    protected File config;

    @Parameter(property = "mkdocs.cmd", defaultValue = "mkdocs")
    protected String command;

    protected abstract void perform() throws MojoExecutionException, MojoFailureException, IOException;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping mkdocs build...");
            return;
        }

        File mkfile = config!=null ? config:new File(dir, "mkdocs.yml");
        if (!mkfile.isFile()) {
            getLog().info("No mkdocs.yml found, skipping mkdocs build...");
            return;
        }

        try {
            perform();
        } catch (IOException e) {
            throw new MojoExecutionException("Build mkdocs failed", e);
        }
    }

    protected void invoke(List<String> args, File dir) throws IOException {
        if (getLog().isDebugEnabled()) {
            getLog().debug(args.stream().collect(Collectors.joining("' '", "'", "'")));
        }

        try {
            Process process = new ProcessBuilder(args).directory(dir).start();
            MkdocsLogger logger = new MkdocsLogger(getLog());
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), logger);
            StreamGobbler errGobbler = new StreamGobbler(process.getErrorStream(), logger);
            outGobbler.start();
            errGobbler.start();

            int code = process.waitFor();
            outGobbler.join();
            errGobbler.join();

            if (code!=0) {
                throw new IOException("Build mkdocs failed with return code: " + code);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted", e);
        }
    }

    private static class StreamGobbler extends Thread {
        private final InputStream in;
        private final MkdocsLogger logger;

        StreamGobbler(InputStream in, MkdocsLogger logger) {
            this.in = in;
            this.logger = logger;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, defaultCharset()))) {
                reader.lines().forEach(logger::log);
            } catch (IOException e) {
                throw new RuntimeException("Reading stream failed", e);
            }
        }
    }
}
