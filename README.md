# hitbox
Indie horror AI game, where you need to hit monsters with box

![alt tag](/HitBoxImg.png)

This project was built using [LibGDX framework](https://libgdx.badlogicgames.com/) and Flow Field 
pathfinding algorithm. Used [Tiled Map Editor](http://www.mapeditor.org/) to generate levels. The pattern for level's name is `###`, where `#` - digit from `[0, 9]`. Xbox controller support.
Mobile versions (Android, IOS) are still in development process, but you can test desktop version with or without controller. The project will change over time (fixing bugs, adding new features, etc.). Enjoy!

### How to play
- Use `arrow keys` to move around
- Use `space key` to hit the box
- Use `escape key` to pause game

### How to compile
- Download gradle tool from [here](https://gradle.org/)
- Use `gradlew.bat` (for Windows) or `./gradlew tasks` (for Unix) command

*If an error occurs after compiling try to change sourceCompatibility parameter from `build.gradle` files* 

### How to run
- Use `gradlew.bat desktop:run` (for Windows) or `./gradlew desktop:run` (for Unix) command

*If you have any questions about this project or if you find bugs :) leave comments here or send me an email to <code>yuriy.peysakhov@gmail.com</code>*
