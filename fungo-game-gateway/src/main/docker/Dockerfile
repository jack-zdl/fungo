FROM java:8
VOLUME /tmp
ADD fungo-game-zuul-8182.jar fungo-game-zuul-8182.jar
RUN bash -c 'touch /fungo-game-zuul-8182.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /fungo-game-zuul-8182.jar"]
