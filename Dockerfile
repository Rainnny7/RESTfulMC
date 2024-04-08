FROM maven:3.8.5-openjdk-17-slim

# Install OpenSSL
RUN apt-get update && apt-get install -y openssl

# Set the working directory
WORKDIR /home/container

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