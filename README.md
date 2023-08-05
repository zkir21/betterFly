# BetterFly ![Plugin Logo](https://i.imgur.com/z1Jxx4D.png)
Intellij IDEA plugin allows to redeploy JBoss resource files on the fly. Plugin is tested on _JBoss EAP 6.4_.

All **non java class** files are considered as resource ones.

## Build
1. Download zipped project folder or clone it with git.

2. Enter the project folder.

3. Set variable _org.gradle.java.home_ in gradle.properties to JDK with version greater than or equal to 17. Pay attention you should write path to JDK with SLASH (/) for OS Windows.

4. Run the command:

- On Linux/Mac
```
./gradlew build
```
- On Windows
```
gradlew.bat build
```
You can find plugin jar file on path: build/libs/fly-[version].jar
## Install
Go to plugin settings by choosing menu File -> Settings -> Plugins

Then do the next:

![Install Plugin](https://i.imgur.com/Ay3cl34.png)

After successful installation you'll see the new plugin in list:

![Installed Plugin](https://i.imgur.com/PoIQLB2.png)
## Setup
Choose menu File -> Settings -> Tools -> BetterFly: Plugin Settings
![Plugin Setup](https://i.imgur.com/frasqkv.png)

You must enter:
1. The JBoss server install folder path value.
2. Artifact Id of the module which resources will be redeployed from IDE.
## Usage
When your current IDE editor window contains resource file:

- press Ctrl+\ Ctrl+P

or

- use "Redeploy Resource" item of right mouse click menu popup:

![Popup Menu](https://i.imgur.com/wYt2BET.png)

When resource file is copied successfully you'll see popup message:

![Success message](https://i.imgur.com/RrDbLXi.png)

## Limitations
Mvel files can not be redeployed.

## Problem
If exception raised during file coping it is reported in log file. To get the location of idea.log file choose IDE menu Help -> Show Log in Files.
