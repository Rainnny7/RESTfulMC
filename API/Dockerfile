FROM maven:3.9.12-eclipse-temurin-25-alpine

# Set the working directory
WORKDIR /home/container

# Copy project files
COPY . .

# Build the app
RUN mvn clean package -q -DskipTests -T4C

# Exposting on port 80 so we can
# access via a reverse proxy for Dokku
ENV HOSTNAME "0.0.0.0"
EXPOSE 80
ENV PORT 80

# We're running in production
ENV APP_ENV "production"

# Start the app
CMD ["java", "-jar", "target/RESTfulMC.jar"]