# Flight Data Coding Assignment

Given the flight and passenger information:

| Field       | Description             |
|-------------|-------------------------|
| passengerId |                         |
| flightId    |                         |
| from        | the departure country   |
| to          | the destination country |
| date        | the date of a flight    |

| Field       | Description |
|-------------|-------------|
| passengerId |             |
| firstName   |             |
| lastName    |             |

Four questions need to be answered: 

1. total number of flights for each month 
2. the top 100 most frequent flyers
3. the greatest number of countries a passenger has been in without being in the UK, i.e. FR → UK → FR → US → US → CN → UK → DE → UK, the correct answer is **3**
4. find the pair of passengers who have been on more than 3 flights together 

### ✅ Prerequisites

Make sure the following software installed locally: 

- JDK `1.8`
- Scala `2.12.10` & SBT `1.8.0`
- Intellij 2022.3.1 for development 
- Docker Desktop `4.15.0` for running Spark cluster

Installation instructions pls refer to each software official website. 

### ⚙️ Compiling & Running (for Mac/Linux)

1. in a terminal window, navigate to the project folder
2. start sbt
3. compile the app 
4. run the app (com.quantexa.Main) to get answer for each question 

```bash
❯ sbt
[info] welcome to sbt 1.8.0 (Temurin Java 1.8.0_352)
[info] loading project definition from /Users/lzhong/Downloads/quantexa-flight/project
[info] loading settings for project quantexa-flight from build.sbt ...
[info] set current project to quantexa-flight (in build file:/Users/lzhong/Downloads/quantexa-flight/)
[info] sbt server started at local:///Users/lzhong/.sbt/1.0/server/dc81d86b13700be73197/sock
[info] started sbt server
sbt:quantexa-flight> compile
[success] Total time: 2 s, completed Dec 25, 2022 12:40:43 PM
sbt:quantexa-flight> runMain com.quantexa.Main 1
...
```

### 📂 Project Folder Structure 

```text
quantexa-flight/
├── .DS_Store
├── .bsp
│   └── sbt.json
├── .gitignore
├── README.md
├── build-image.sh
├── build.sbt
├── docker
│   ├── .DS_Store
│   ├── spark-base
│   │   ├── Dockerfile
│   │   └── wait-for-it.sh
│   ├── spark-master
│   │   ├── Dockerfile
│   │   └── start-master.sh
│   ├── spark-submit
│   │   ├── Dockerfile
│   │   └── spark-submit.sh
│   └── spark-worker
│       ├── Dockerfile
│       └── start-worker.sh
├── docker-compose.yml
├── project
│   ├── .DS_Store
│   ├── build.properties
│   └── project
└── src
    ├── .DS_Store
    ├── main
    │   ├── .DS_Store
    │   ├── resources
    │   └── scala
    └── test
        └── scala
```

- scala source under src/main/scala, all solutions inside `com.quandexa.Main`
- csv data under src/main/resources/data
- Dockerfile under docker

### 📝 Notes  

- unit test: not implemented (due to time constraint)  

- integration test: not implemented (due to time constraint) but local Spark cluster can be created via Docker   

  - build Docker images for Spark:
    ```bash
    # chmod a+x build-image.sh
    # ./build-image.sh
    ```
  - start a local Spark cluster:
    ```bash
    # docker compose up
    ```


