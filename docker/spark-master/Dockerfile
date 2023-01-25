FROM spark-base:latest

RUN apt-get update && apt-get install -y dos2unix
COPY start-master.sh /
RUN dos2unix /start-master.sh && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*

ENV SPARK_MASTER_PORT 7077
ENV SPARK_MASTER_WEBUI_PORT 4040
ENV SPARK_MASTER_LOG /spark/logs

EXPOSE 7077 6066 4040

CMD ["/bin/bash", "/start-master.sh"]