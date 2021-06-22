# The project is Java with Maven, so the latest maven image should
# be a good starting point
FROM maven:3.8.1-jdk-8-slim

# setup a working directory for the application
WORKDIR /app/orm

ARG maven_key
RUN mkdir ~/.m2/
RUN echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \n\
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \n\
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 \n\
                        https://maven.apache.org/xsd/settings-1.0.0.xsd"> \n\
        <servers> \n\
            <server> \n\
                <id>Flock-of-Hawks-Artifacts</id>\n\
                <username>revature-training-uta</username>\n\
                <password>'${maven_key}'</password>\n\
            </server>\n\
        </servers>\n\
</settings>'

# Copy the cucrrent directory into the container
COPY . .

# Create a clean package from the pom.xml file and install it
RUN mvn clean package && mvn install

# upload the artifact to the project artifacts
RUN mvn deploy

CMD ["/bin/bash"]