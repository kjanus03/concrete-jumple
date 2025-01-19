# Concrete Jumple

Concrete Jumple is a 2D platformer game built using JavaFX, where players control a monkey navigating through a vertical tower of randomly generated platforms. The goal is to complete the tower as quickly as possible while avoiding enemies and utilizing various buffs to enhance gameplay. The game also features a high score system that tracks the fastest completion times.

## Features
- **Randomly Generated Levels**: Each playthrough features a unique set of platforms with gaps that are always jumpable.
- **Chasing Enemies**: Police enemies that follow the player and stun them for two seconds upon contact.
- **Buffs**: Three types of buffs are available to help the player:
  - **Speed Buff**: Increases movement speed.
  - **Jump Buff**: Increases jump height.
  - **Invincibility Buff**: Protects against enemy stuns.
- **High Score System**: Tracks and displays the top 10 fastest completion times using MongoDB.
- **Custom Assets**: All game sprites (except for the background images) and background music are original works created by the development team.
- **Volume and Resolution Settings**: Allows players to adjust audio volume and choose between HD and Full HD resolutions.

## Game Mechanics

### Character
Players control a monkey, outfitted with headphones and baggy jeans, navigating through a series of vertical platforms. The primary objective is to ascend the tower as quickly as possible while evading enemies and making use of helpful buffs.

### Platform and Buff Generation
- **Platforms**: These are generated at the start of the game, ensuring they are always within jumping distance.
- **Enemies**: Police entities are placed throughout the game and chase the player if they get too close, stunning the player for a brief period.
- **Buffs**: Buffs are also randomly placed at the beginning of the game to give players an advantage. The buffs include Speed, Jump, and Invincibility.

## Technologies Used

### Core Frameworks and Tools
- **JavaFX**: Used for the game engine and user interface development.
- **FXGL**: A framework that facilitates game development with built-in physics and rendering utilities.
- **MongoDB**: Used for storing and retrieving high scores.

### Custom Assets
- **Sprites and Backgrounds**: All character sprites, platform designs, and other graphical assets (except for background images) were created by the development team.
- **Music**: All music and sound effects were composed, recorded, and produced by the team.

### Build and Deployment
- **Maven**: Used for managing project dependencies and packaging. The project uses `maven-shade-plugin` for building the final executable jar file.
- **Java 19**: The game is built using Java 19 to ensure modern performance and compatibility.

## High Score System

The high score system uses MongoDB to store the top 10 fastest completion times. Each high score includes the player's username, the date of the score, and the completion time.

## Contact & Contributing

If you would like to contribute to the project or report issues, feel free to reach out via email at:

- **kacperj2022@gmail.com**

