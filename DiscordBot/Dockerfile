FROM maven:3.8.5-openjdk-17-slim

# Set the working directory
WORKDIR /home/container

# Copy project files
COPY . .

# Build the app
RUN mvn clean package -T4C

# Start the app
CMD ["java", "-jar", "target/DiscordBot.jar"]