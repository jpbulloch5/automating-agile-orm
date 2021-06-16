# The project is Java with Maven, so the latest maven image should
# be a good starting point
FROM maven:latest

# setup a working directory for the application
WORKDIR /app

# Copy the cucrrent directory into the container
COPY . .

# Create a clean package from the pom.xml file and install it
RUN mvn clean package && mvn install

CMD ["/bin/bash"]