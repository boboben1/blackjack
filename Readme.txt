Card images from:
https://acbl.mybigcommerce.com/52-playing-cards/


Client and Server code are separated, however, it only supports one client at the moment, and the event handling is
quite hacky. If I have time (probably will not), I will tidy up the event system. Possibly separating client events into
a different "event bus" like object.

It starts it's own listen server and connects to it. No option to connect to other players (yet).

Hopefully I can add multiplayer support.

This definitely needs more refactoring.


Also, it supports saving high scores ;)