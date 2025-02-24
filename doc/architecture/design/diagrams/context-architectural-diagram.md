### Diagrama de Contexto - Nível 1
                                                                                                      
```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

Container_Ext(client, "Client App", "*", "Aplicação que necessita ouvir mensagens via websocket")
Container_Ext(backend_apis, "Backend APIs", "*", "Aplicação que publica mensagens para o websocket")
Container(mentorAssist_websocket, "Mentor Assist Websocket", "AWS Lambda + AWS Api gateway + DynamoDB", "Solução Serverless Websocket")

BiRel_R(client, mentorAssist_websocket, "Conecta e escuta o canal websocket", "wss")
Rel_U(backend_apis, mentorAssist_websocket, "Publica mensagens para o websocket", "SNS")

@enduml 
```
