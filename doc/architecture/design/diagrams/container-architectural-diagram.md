### Diagrama de Container - Nível 2

![image](https://github.com/user-attachments/assets/c52676ab-6446-42bd-abdc-5fb1f43b5804)

@startuml
!define AWSPUML https://raw.githubusercontent.com/milo-minderbinder/AWS-PlantUML/release/18-2-22/dist

!includeurl AWSPUML/common.puml
!includeurl AWSPUML/ApplicationServices/AmazonAPIGateway/AmazonAPIGateway.puml
!includeurl AWSPUML/Compute/AWSLambda/AWSLambda.puml
!includeurl AWSPUML/Compute/AWSLambda/LambdaFunction/LambdaFunction.puml
!includeurl AWSPUML/Database/AmazonDynamoDB/AmazonDynamoDB.puml
!includeurl AWSPUML/Database/AmazonDynamoDB/table/table.puml
!includeurl AWSPUML/General/AWScloud/AWScloud.puml
!includeurl AWSPUML/General/client/client.puml
!includeurl AWSPUML/General/mobileclient/mobileclient.puml
!includeurl AWSPUML/Messaging/AmazonSNS/AmazonSNS.puml
!includeurl AWSPUML/Messaging/AmazonSNS/topic/topic.puml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml


CLIENT(client_web, Aplicação Web que necessita ouvir mensagens via websocket)
MOBILECLIENT(client_mobile, Aplicação Mobile que necessita ouvir mensagens via websocket)
Container_Ext(backend_apis, "Backend APIs", "*", "Aplicações que publicam mensagens para o websocket")


AWSCLOUD(aws) {

  Container_Boundary(mentorAssist_websocket_system, 'MentorAssist Websocket System') {

    AMAZONAPIGATEWAY(api)

    AWSLAMBDA(lambda) {
      LAMBDAFUNCTION(connectDisconectPublish, Connect/Disconnect/Publish)
    }

    AMAZONDYNAMODB(dynamo) {
      TABLE(sessions, MentorAssist Websocket Sessions)
    }

    AMAZONSNS(SNS) {
      TOPIC(feedBackMessages, FeedBack Messages)
    }
  }
}


Rel_D(client_mobile, api, "1 - Conecta no canal websocket", "wss")
Rel_U(api, client_mobile, "6 - Escuta o canal websocket", "wss")
Rel_L(client_web, api, "1 - Conecta o canal websocket", "wss")
Rel_R(api, client_web, "6 - Escuta o canal websocket", "wss")

Rel_D(api, connectDisconectPublish, "2 - Conecta/Disconecta\no client") 
Rel_R(connectDisconectPublish, sessions, "3 - Armazena a sessão da conexão do client/usuário")
Rel_L(backend_apis, feedBackMessages, "4 - Publica mensagens para o websocket", "SNS")
Rel_U(feedBackMessages, connectDisconectPublish, "5 - Barramento de mensagens de feedback para envio ao Websocket")

@enduml