# Release Notes

## Release 0.5.0 (November 2025)
* Added SSL/TLS Support.

## Release 0.4.0 (June 2023)
* Added ability to set headers when publishing messages.

## Release 0.3.3 (23th March 2023)
* Added additional `arguments` option for queues and exchanges to allow 
  setting further arguments.

## Release 0.3.2 (17th March 2023)
* Added additional configuration options for queues and exchanges. This allows
  creating/working with durable or non-durable, internal or non-internal,
  exclusive or non-exclusive queues and exchanges. This introduces a number of
  breaking changes. If these additional configuration options are not needed,
  there should simply be no need to update in which case there are no changes.
  If these options are needed, configurations will have to be updated anyway to
  configure them.
* **BREAKING CHANGES**
  * Requires MATLAB R2019b or newer instead of R2017b or newer.
  * `queueName` configuration option/property has been removed. Instead there is
    now a `queue` object with `name` property, as well as a few new properties.
  * `exchange` configuration option/property is no longer just the exchange
    name. Instead it is now an object with `name` property as well as a few new
    properties.
* Updated documentation format.
* Published HTML documentation on GitHub pages.

## Release 0.2.0 (23rd March 2022)
* Fixed library dependency conflicts and updated library dependencies
* Renamed `MessageReceiver` to `MessageBroker` to underline the fact that the
  application does not only receive messages but then also actually forwards
  them to MATLAB Production Server
* Added a MATLAB interface for interacting with RabbitMQ from MATLAB directly
* Documentation improvements
* Code clean-up

## Release 0.1.4 (13th Oct 2020)
* Bumped junit version

## Release 0.1.3 (Jul 2020)
* Change license file
* Fix link in documentation

## Release 0.1.2 (Jun 2020)
* Documentation updates
* Code clean-up

## Release 0.1.1 (22nd Apr 2020)
* Minor fixes

## Release 0.1.0 (Dec 2019)
* Initial release supporting RabbitMQ

[//]: #  (Copyright 2019-2022 The MathWorks, Inc.)
