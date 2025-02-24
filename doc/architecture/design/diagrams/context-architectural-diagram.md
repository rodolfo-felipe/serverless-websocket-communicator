### Diagrama de Contexto - Nível 1

![image](https://github.com/user-attachments/assets/aca69f0c-bbd0-4d4c-ab57-0038f3d16801)

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

Container_Ext(client_mobile, "Client Mobile App", "*", "Aplicação Mobile que necessita ouvir mensagens via websocket")
Container_Ext(client_web, "Client Web App", "*", "Aplicação Web que necessita ouvir mensagens via websocket")
Container_Ext(backend_apis, "Backend APIs", "*", "Aplicação que publica mensagens para o websocket")
Container(mentorAssist_websocket, "Mentor Assist Websocket", "AWS Lambda + AWS Api gateway + DynamoDB", "Solução Serverless Websocket")

BiRel_R(client_mobile, mentorAssist_websocket, "Conecta e escuta o canal websocket", "wss")
BiRel_L(client_web, mentorAssist_websocket, "Conecta e escuta o canal websocket", "wss")
Rel_U(backend_apis, mentorAssist_websocket, "Publica mensagens para o websocket", "SNS")

@enduml
```
