FROM java:8

RUN mkdir -p /src/logs
COPY *SNAPSHOT.jar /src
WORKDIR /src

EXPOSE 9999

ENV JAVA_OPTS="-Xms1024m -Xmx2048m"

ENTRYPOINT java -jar -server ${JAVA_OPTS} \
	-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -Xloggc:/src/logs/server_gc.log -XX:SurvivorRatio=8 -XX:NewRatio=4 \
	-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/src/logs -Duser.timezone=GMT+08 -Djava.security.egd=file:/dev/urandom *.jar 2>&1|tee /src/logs/console.log
