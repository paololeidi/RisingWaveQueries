# RisingWaveQueries

### start docker desktop!!

### start container:
docker run -it --pull=always -p 4566:4566 -p 5691:5691 --add-host host.docker.internal:host-gateway risingwavelabs/risingwave:latest single_node

### in another terminal run postgres container (?):
docker run -it --add-host host.docker.internal:host-gateway postgres bin/bash

### in it start cli
psql -h host.docker.internal -p 4566 -d dev -U root
