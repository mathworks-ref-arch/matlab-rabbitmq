@ECHO Message Sender

copy ..\src\main\resources\mps.yaml .
copy ..\target\RabbitMQClient-0.0.1.jar .

java -cp .;RabbitMQClient-0.0.1.jar;../lib/* com.mathworks.messaging.MessageSender mps.yaml  