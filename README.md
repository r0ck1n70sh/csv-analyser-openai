# csv-analyser-openai

## Requirements
- Java 17
- Postegres

 
## Setup
### Postgres Credentials
```shell
username: postgres
password: password
database: db_springboot_14102023
port: 5432
```
 
### Install
```shell
mvn clean install -DskipTests
```
 
### Run
```shell
mvn spring-boot:run
```

## API
- Create User Session
```yaml
endpoint: /api/user_session/
method: POST
response: 
    - id: int
    - userSessionId: str
```
 
- Create Chat Session
```yaml
endpoint: /api/chat_session/
method: POST
request:
    - user_session_id: str
response:
    - id: int
    - chatSessionId: str
```
 
- Pipeline
```yaml
endpoint: /api/pipeline/run/{userSessionId}/{chatSessionId}
method: POST
request:
    - csvName: str
    - csvStrBase64: str
    - userPrompt: str
response:
    - graphs: list
    - message: str
    - status: enum
```

## Pipeline Status Order
- `INITIALIZED`
- `CSV_SAVED`
- `CSV_PROCESSED`
- `ANALYTICS_COMPLETED`
