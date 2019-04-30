FROM java:8
VOLUME /tmp
ADD fungo-game-config-server-8768.jar fungo-game-config-server-8768.jar
RUN bash -c 'touch /fungo-game-config-server-8768.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fungo-game-config-server-8768.jar"]