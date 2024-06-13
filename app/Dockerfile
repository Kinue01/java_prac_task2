FROM maven:3.9.6 as build
COPY . /app/
WORKDIR /app
RUN mvn clean package

FROM openjdk:24-slim-bullseye
RUN apt-get update && apt-get install -y --no-install-recommends wget unzip libgl1-mesa-glx libgl1-mesa-dri libglu1-mesa libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0 libgtk-3-dev libgtk2.0-0 libgtk2.0-dev && rm -rf /var/lib/apt/lists/*
RUN wget https://download2.gluonhq.com/openjfx/22.0.1/openjfx-22.0.1_linux-x64_bin-sdk.zip -O openjfx-22.0.1_linux-x64_bin-sdk.zip && \
       unzip openjfx-22.0.1_linux-x64_bin-sdk.zip -d /opt && \
       rm openjfx-22.0.1_linux-x64_bin-sdk.zip
ENV DISPLAY=:0
ENV XAUTHORITY=/root/.Xauthority
COPY --from=build /app/target /app/target
WORKDIR /app
CMD ["sh", "-c", "LD_LIBRARY_PATH=/opt/javafx-sdk-22.0.1/lib:$LD_LIBRARY_PATH java --module-path /opt/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -Dprism.verbose=true -cp target/app-1.0-SNAPSHOT-jar-with-dependencies.jar org.example.prac2.App"]