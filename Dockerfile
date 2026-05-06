FROM selenium/standalone-chrome:latest

USER root

# Install Maven and JDK 17
RUN apt-get update && \
    apt-get install -y --no-install-recommends maven openjdk-17-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY testng.xml .
COPY run-tests.sh .
COPY .env .
COPY src ./src

# Pre-download all Maven dependencies (cached layer)
RUN mvn dependency:resolve -q

# Make entrypoint executable
RUN chmod +x run-tests.sh

# Runtime env vars — .env provides defaults; override at docker run time with -e
ENV ENV=dev
ENV BROWSER=chrome
ENV HEADLESS=true
ENV BASE_URL=""

ENTRYPOINT ["./run-tests.sh"]
