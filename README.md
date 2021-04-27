## Mkdocs Maven Plugin

Simple maven plugin for mkdocs. 

This project is forked from [mkdocs-maven-plugin](https://github.com/shred/mkdocs-maven-plugin) manually just used to lean how to develop a simple maven plugin.

### Release

- Snapshot: `mvn clean deploy`
- Release: `mvn clean package source:jar gpg:sign install:install deploy:deploy`
