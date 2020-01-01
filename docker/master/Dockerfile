FROM mcr.microsoft.com/java/jdk:11u5-zulu-ubuntu
RUN useradd -ms /bin/bash  master
USER master
WORKDIR /home/master
RUN mkdir data
COPY --chown=master ./build/libs/minihadoop-1.0-SNAPSHOT-all.jar ./
COPY --chown=master ./docker/config ./config
CMD java -cp minihadoop-1.0-SNAPSHOT-all.jar comnet.minihadoop.master.Application -f config/master-config.yaml