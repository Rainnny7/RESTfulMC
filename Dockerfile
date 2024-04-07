FROM openjdk:17-jdk-slim

# Copy project files
COPY . .

# Build the app
RUN mvn clean package

# Exposting on port 80 so we can
# access via a reverse proxy for Dokku
ENV HOSTNAME "0.0.0.0"
EXPOSE 80
ENV PORT 80

# Start the app
CMD ["java", "-jar", "target/RESTfulMC.jar"]