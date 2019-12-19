rem
rem Copyright 2018 The MathWorks, Inc.
rem
rem Windows script for starting the RabbitMQ-to-MTALAB Production Server Client
rem

@ECHO OFF ECHO Message Receiver

copy ..\src\main\resources\mps.yaml .
copy ..\target\RabbitMQClient-0.0.1.jar ..\lib

java -cp .;../lib/* com.mathworks.messaging.MessageReceiver mps.yaml 
