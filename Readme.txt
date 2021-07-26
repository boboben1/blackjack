
Ben Brecher
COP3252 Project

Blackjack game implementation.

Card images from:
https://acbl.mybigcommerce.com/52-playing-cards/


Client and Server code are separated, however, it only supports one client at the moment, and the event handling is
quite hacky. If I have time (probably will not), I will tidy up the event system. Possibly separating client events into
a different "event bus" like object.

It starts it's own listen server and connects to it. No option to connect to other players (yet).

Hopefully I can add multiplayer support.

This definitely needs more refactoring.


Also, it supports saving high scores ;)


Blackjack pays out 3:2 (like standard competition).

Split is not yet fully implemented. Bets don't work, and major refactors around this functionality are needed.

Also, some nice quality of life features are implemented:
When you press +X votes (larger than 1) after reset, it will go to that amount of votes.
Ex: You press reset then +100. You will be betting 100 instead of 101.


To compile:

Extract the entire jar.
Run the commands exactly as shown:

"javac me/brecher/blackjack/Game"

To run from extracted jar:

"java me.brecher.blackjack.Game"