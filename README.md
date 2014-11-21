avaje-pig-doclet
================

javadoc doclet for styling code using pygments


Maven plugin
------------

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
            <version>2.9</version>
            <executions>
                <execution>
                    <id>attach-javadocs</id>
                    <goals>
                        <goal>jar</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <source>1.8</source>
                <doclet>org.avaje.doclet.PygmentsDoclet</doclet>
                <excludePackageNames>com.avaje.ebeaninternal.*</excludePackageNames>
                <docletArtifact>
                    <groupId>org.avaje</groupId>
                    <artifactId>pygments-doclet</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
                </docletArtifact>
                <additionalparam> 
                    -include-basedir ${project.basedir}
                    -attributes "idseparator=-; project_name=${project.name}; \
                    project_version=${project.version}; \
                    project_desc=${project.description}"
                </additionalparam>
                <linksource>true</linksource>
                <overview>src/main/java/com/avaje/ebean/overview.html</overview>

            </configuration>
        </plugin>

