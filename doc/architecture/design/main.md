# Serverless Websocket Communicator

Solução Serverless utilizando Lambda, responsável por integrar com api de websockets do [api gateway](https://docs.aws.amazon.com/apigateway/latest/developerguide/apigateway-websocket-api-overview.html) para permitir conexão e troca de mensagens utilizando o protocolo websocket.

## Descrição do Problema

Esse projeto nasceu de uma necessidade de evolução da arquitetura de comunicação assíncrona da aplicação pois as soluções existentes de websockets na empresa são específicas de seus contextos, não tendo uma solução reutilizável para reduzir o custo com infra, a quantidade de artefatos para dar manutenção e o esforço para utilização em novos contextos.

Assim, essa solução foi proposta com o objetivo de ser reutilizável e possível de evoluir dentro do contexto da aplicação de forma geral e centralizada.

                                                                                                      
## Requisitos

 - Ser reutilizável para diferentes contextos;
 - Ser desacoplado dos contextos/projetos que a consomem;
 - Ser possível de evoluir para diferentes contextos (ex: envio de mensagens para múltiplas conexões, ou para a última conexão).

## Premissas

 - O usuário deve estar autenticado

## Arquitetura

A conexão websocket é realizada utilizando o AWS Api Gateway, que integra com o lambda versionado neste repositório. Este lambda é responsável por armazenar/gerenciar os dados da sessão do usuário em um dynamo db.

Este mesmo lambda escuta um tópico SNS para publicar mensagens no websocket.

## Diagramas:

# [Contexto - Nível 1](./diagrams/context-architectural-diagram.md)
# [Container - Nível 2](./diagrams/container-architectural-diagram.md)
# [Sequência do fluxo de sucesso](./diagrams/success-flow-sequence-diagram.md)


## Contratos:

# [Definição de contratos (conexão, sessão e mensagem)](./contracts-definition.md)
