# RisingWaveQueries

### 1. Start zookeeper on local machine
cd C:/kafka

.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

### 2. Start kafka on local machine
cd C:/kafka

.\bin\windows\kafka-server-start.bat .\config\server.properties

### 3. Create "stress" and "weight" topics if they don't exist
.\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092
--replication-factor 1 --partitions 1 --topic <topic-name>

### 4. Start docker desktop

### 5. Run risingwave container:
docker run -it --pull=always -p 4566:4566 -p 5691:5691 --add-host host.docker.internal:host-gateway risingwavelabs/risingwave:latest single_node

### 6. Run postgres container:
docker run -it --add-host host.docker.internal:host-gateway postgres bin/bash

### 7. In postgre container, start cli
psql -h host.docker.internal -p 4566 -d dev -U root

### 8. In the cli, create the sources
CREATE SOURCE stressStream (timestamp timestamp, id int, status varchar, stressLevel int)
WITH (
connector = 'kafka',
topic = 'stress',
properties.bootstrap.server = 'host.docker.internal:9092',
scan.startup.mode = 'latest'
) FORMAT PLAIN ENCODE CSV (
without_header = 'true',
delimiter = ','
);

CREATE SOURCE weightStream (timestamp timestamp, id int, weight double)
WITH (
connector = 'kafka',
topic = 'weight',
properties.bootstrap.server = 'host.docker.internal:9092',
scan.startup.mode = 'latest'
) FORMAT PLAIN ENCODE CSV (
without_header = 'true',
delimiter = ','
);

### 9. Run StreamGenerator class once

### 8. Run RisingWaveConnect file
RisingWave APIs will read the content of the topic from beginning and generate the output file right away