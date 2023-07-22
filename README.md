# BetterFly ![Plugin Logo](https://i.imgur.com/z1Jxx4D.png)
Intellij IDEA plugin allows to redeploy JBoss resource files on the fly.

All non java class files are considered as resources ones.

## Build
Set variable _org.gradle.java.home_ in gradle.properties to JDK with version greater than or equal to 17.

Run the command:
```
./gradlew clean build
```
## Install
Install plugin by choosing menu File -> Settings -> Plugins

Then do the next:

![Install Plugin](https://i.imgur.com/9m3Jfhq.png)

## Usage

Press Ctrl+\ Ctrl+P

or

Use "Redeploy Resource" item of right mouse click menu popup

When resource file is copied successfully you'll see popup message:

![Success message](https://i.imgur.com/RrDbLXi.png)

## Limitations
Mvel files are copied successfully but application can not apply them at once now. You need to restart server.
