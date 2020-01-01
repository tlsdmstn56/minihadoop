FROM mcr.microsoft.com/java/jdk:11u5-zulu-ubuntu
RUN useradd -ms /bin/bash  slave
ARG hostname
USER slave
WORKDIR /home/slave
RUN mkdir data
COPY --chown=slave ./build/libs/minihadoop-1.0-SNAPSHOT-all.jar ./
COPY --chown=slave ./docker/config ./config
RUN sed -i s/HOSTNAME/$hostname/ ./config/slave-config.yaml
CMD java -cp minihadoop-1.0-SNAPSHOT-all.jar comnet.minihadoop.slave.Application -f config/slave-config.yaml