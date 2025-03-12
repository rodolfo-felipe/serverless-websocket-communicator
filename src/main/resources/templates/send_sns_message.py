import boto3
import argparse

# Pré-requisito as credenciais estarem configuradas e atualizadas

# Criando cliente SNS
sns_client = boto3.client("sns", region_name="us-east-1") 

# ARN do tópico SNS
TOPIC_ARN = "arn:aws:sns:us-east-1:ACCOUNT_ID:mentor-assist-websocket-message-publisher"

def parse_attributes(attribute_list):
    """Converte atributos no formato esperado pelo SNS"""
    attributes = {}
    for attr in attribute_list:
        try:
            key, value = attr.split("=")
            if value.replace(".", "", 1).isdigit():
                attributes[key] = {"DataType": "Number", "StringValue": value}
            else:
                attributes[key] = {"DataType": "String", "StringValue": value}
        except ValueError:
            print(f"❌ Formato inválido para atributo: {attr}. Use chave=valor.")
    return attributes

def send_message(message, attributes):
    """ Envia uma mensagem para o tópico SNS com atributos """
    try:
        response = sns_client.publish(
            TopicArn=TOPIC_ARN,
            Message=message,
            MessageAttributes=attributes
        )
        print(f"✅ Mensagem enviada com sucesso! MessageId: {response['MessageId']}")
    except Exception as e:
        print(f"❌ Erro ao enviar mensagem: {str(e)}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Enviar mensagem para um tópico SNS com atributos")
    parser.add_argument("--message", dest= "message", type=str, help="Mensagem a ser enviada")
    parser.add_argument("--attributes", nargs="*", default=[], help="Atributos no formato chave=valor")

    args = parser.parse_args()
    
    message_attributes = parse_attributes(args.attributes)
    
    send_message(args.message, message_attributes)
