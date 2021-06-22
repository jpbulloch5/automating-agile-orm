# The project is Java with Maven, so the latest maven image should
# be a good starting point
FROM maven:3.8.1-jdk-8-slim

# setup a working directory for the application
WORKDIR /app/orm

# Copy the cucrrent directory into the container
COPY . .

# Create a clean package from the pom.xml file and install it
RUN mvn clean package && mvn install

CMD ["/bin/bash"]