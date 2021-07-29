# Blackjack


Ben Brecher
COP3252 Project

Blackjack game implementation.

## Features

* [Client and Server](#client-and-server)
* Saving
* Multiple decks of cards
* Blackjack payout of 3:2
* Hit, Stand, Double
* Split
* [Quality of Life Features](#quality-of-life-features)
* Preloading and caching
* Full multithreading support
* Server and Client Sync


### Quality of Life Features

When you press +X votes (larger than 1) after reset, it will go to that amount of votes.
Ex: You press reset then +100. You will be betting 100 instead of 101.

## Implementation notes

Client and Server code are separated, however, it only supports a single, local player at the moment. It shouldn't be
too difficult to add support for it, but I'm limited by time. See [Adding Multiplayer](#adding-multiplayer).

It uses Guice for dependency injection and Guava for an event bus. This significantly reduces coupling and makes the game
easier to code.

All assets are preloaded and cached to facilitate smoother gameplay.

The messaging system is somewhat poorly coded, and could benefit from a rewrite, such as separating events and packets
into different types.


### Client and Server

It starts its own listen server and connects to it. This is done to enforce separation of concerns between GUI and Server,
and makes it simple to expand and add multiplayer support in the future.

Client and Server will sync before the round to make sure there are no race conditions.

#### Adding multiplayer

Because it is intended for only a single local player, some things are hardcoded, but it wouldn't be too hard to 
expand upon it and add the functionality.


## Incomplete Features

### Animations for money earned/lost
Support for displaying change in money earned and lost exists, and ideally would be implemented with a flashy floating 
text animation for adding to your total balance, but I had no time to implement this feature.

## Compiling and Running


#### Compiling and running from extracted jar file

Extract the entire jar.
Run the commands exactly as shown:

```shell
# Compiling
javac me/brecher/blackjack/Game.java
# Running
java me.brecher.blackjack.Game
```

#### Or, compiling and running from repo

##### Compiling
```shell
git clone https://github.com/boboben1/blackjack.git
cd blackjack/src

# Windows:
javac -cp ".;../lib/*" me/brecher/blackjack/Game.java
# Linux:
javac -cp ".:../lib/*" me/brecher/blackjack/Game.java
```

#### Running

```shell
# Windows
java -cp ".;../lib/*" me.brecher.blackjack.Game
# Linux
java -cp ".:../lib/*" me.brecher.blackjack.Game
```

## Attribution

Card images from:
https://acbl.mybigcommerce.com/52-playing-cards/