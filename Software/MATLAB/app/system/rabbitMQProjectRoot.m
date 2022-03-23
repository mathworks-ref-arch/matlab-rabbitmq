function [str] = rabbitMQProjectRoot(varargin)
% RABBITMQPROJECTROOT Helper function to locate the RabbitMQ package.
%
% Locate the installation of the RabbitMQ package to allow easier construction
% of absolute paths to the required dependencies.

% Copyright 2020 The MathWorks

str = fileparts(fileparts(fileparts(mfilename('fullpath'))));

end %function
