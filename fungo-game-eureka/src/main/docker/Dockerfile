FROM java:8
VOLUME /tmp
ADD fungo-game-eureka-8761.jar fungo-game-eureka-8761.jar
RUN bash -c 'touch /fungo-game-eureka-8761.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fungo-game-eureka-8761.jar"]