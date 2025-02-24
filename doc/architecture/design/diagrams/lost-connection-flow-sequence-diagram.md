### Sequência - Fluxo de perda de conexão

![image](https://github.com/user-attachments/assets/653d3103-cc8b-480f-b814-23cd63e78014)

```plantuml
@startuml
actor User
participant Client as "Client Web/Mobile"
participant BackendApi
queue       MentorAssistWebsocketPublisher as "MentorAssistWebsocketPublisher (SNS)"
participant MentorAssistWebsocketGateway
participant LambdaMentorAssistWebsocket
database WSSessionDB

User -> Client

activate Client
Client -> MentorAssistWebsocketGateway: connect websocket
note left 
   url: wss:://${api-gateway-websocket-endpoint}
   headers:
     ${Cookie: | Authorization: Bearer} ${token} 
   queryParams:
     context
     contextId
end note

activate MentorAssistWebsocketGateway
MentorAssistWebsocketGateway -> LambdaMentorAssistWebsocket: @connect
deactivate MentorAssistWebsocketGateway

activate LambdaMentorAssistWebsocket
LambdaMentorAssistWebsocket -> WSSessionDB: save websocket state
note right
   stored attributes:
     caller_id (pk) : user#${code}
     context (sk)   : ${context}#${contextId}
     connection_id  : ${connectionId}
     connected_at   : ${connectionTime}
     expiration_date: ${expirationDate}
end note

activate WSSessionDB
LambdaMentorAssistWebsocket <-- WSSessionDB
deactivate WSSessionDB
deactivate LambdaMentorAssistWebsocket

activate BackendApi
BackendApi -> MentorAssistWebsocketPublisher: send message
activate MentorAssistWebsocketPublisher
note right BackendApi
  sns message body:
    ${messageToBePublished}
  sns message attributes:
   "contextId": ${contextId},
   "context": ${context},
   "user": ${code}
end note
deactivate BackendApi

LambdaMentorAssistWebsocket <- MentorAssistWebsocketPublisher: consume message
deactivate MentorAssistWebsocketPublisher

activate LambdaMentorAssistWebsocket
LambdaMentorAssistWebsocket -> WSSessionDB: getConnectionId by userId and featureEntityId
activate WSSessionDB
LambdaMentorAssistWebsocket <-- WSSessionDB
deactivate WSSessionDB

destroy Client

LambdaMentorAssistWebsocket -> MentorAssistWebsocketGateway: publish message using connectionId
note left
 POST https://${api-gateway-websocket-endpoint}
 request body:
   ConnectionId: ${connectionId}
   Data:{
        "text": "message"
   }
end note

activate MentorAssistWebsocketGateway
MentorAssistWebsocketGateway --> LambdaMentorAssistWebsocket: Failure: Invalid Connection Id 
deactivate MentorAssistWebsocketGateway
LambdaMentorAssistWebsocket --> MentorAssistWebsocketPublisher: failed to consume message
destroy LambdaMentorAssistWebsocket

activate MentorAssistWebsocketPublisher

activate Client
Client -> MentorAssistWebsocketGateway: connect websocket
note left 
   Client creates new connection with same contextId
end note
activate MentorAssistWebsocketGateway

MentorAssistWebsocketGateway -> LambdaMentorAssistWebsocket: @connect
deactivate MentorAssistWebsocketGateway

activate LambdaMentorAssistWebsocket
LambdaMentorAssistWebsocket -> WSSessionDB: update websocket state
activate WSSessionDB
LambdaMentorAssistWebsocket <-- WSSessionDB
deactivate WSSessionDB
deactivate LambdaMentorAssistWebsocket

LambdaMentorAssistWebsocket <- MentorAssistWebsocketPublisher: consume message (retry)
deactivate MentorAssistWebsocketPublisher
activate LambdaMentorAssistWebsocket

LambdaMentorAssistWebsocket -> WSSessionDB: getConnectionId by userId and featureEntityId
activate WSSessionDB
LambdaMentorAssistWebsocket <-- WSSessionDB
deactivate WSSessionDB

LambdaMentorAssistWebsocket -> MentorAssistWebsocketGateway: publish message using connectionId
note left
 POST https://${api-gateway-websocket-endpoint}
 request body:
   ConnectionId: ${connectionId}
   Data:{
        "text": "message"
    }
end note
activate MentorAssistWebsocketGateway
deactivate LambdaMentorAssistWebsocket

MentorAssistWebsocketGateway --> Client: message delivered to frontend
deactivate MentorAssistWebsocketGateway
Client -> User

Client -> MentorAssistWebsocketGateway: disconnect websocket
deactivate Client
activate MentorAssistWebsocketGateway

MentorAssistWebsocketGateway -> LambdaMentorAssistWebsocket: @disconnect
deactivate MentorAssistWebsocketGateway

activate LambdaMentorAssistWebsocket
LambdaMentorAssistWebsocket -> WSSessionDB: clear websocket state
activate WSSessionDB
LambdaMentorAssistWebsocket <-- WSSessionDB
deactivate WSSessionDB
deactivate LambdaMentorAssistWebsocket

@enduml
```
