# BetterFly ![Plugin Logo](https://i.imgur.com/z1Jxx4D.png)
Intellij IDEA plugin allows:
1. To redeploy JBoss resource files on the fly. Plugin is tested on _JBoss EAP 6.4_.

   All **non java class** files are considered as resource ones.

3. To form and store usefull for development data in cloud storage for future usage by AI system.

## Build
1. Download zipped project folder or clone it with git.

2. Enter the project folder.

3. Set variable _org.gradle.java.home_ in gradle.properties to JDK with version greater than or equal to 17. Pay attention you should write path to JDK with SLASH (/) for OS Windows.

4. Run the command:

- On Linux/Mac
```
./gradlew buildPlugin
```
- On Windows
```
gradlew.bat buildPlugin
```
You can find plugin jar file on path: build/distributions/BetterFly-[version].zip
## Install
Go to plugin settings by choosing menu File -> Settings -> Plugins

Then do the next:

![Install Plugin](https://i.imgur.com/Ay3cl34.png)

After successful installation you'll see the new plugin in list:

![Installed Plugin](https://i.imgur.com/dVB0BTX.png)
## Setup
Choose menu File -> Settings -> Tools -> BetterFly: Plugin Settings
![Plugin Setup](https://i.imgur.com/GtKqFtV.png)

You must enter:
1. The JBoss server install folder path value. For Windows OS you should use slashes in folder path. For example: C:/unipay/jboss7/
2. Artifact Id of the module which resources will be redeployed from IDE.
3. Google Key file for connection to Google Sheet to store data. This file you can obtain from the plugin developer.
4. Google Sheets Data file Id (default value - 1_S0GqdchUf7LdLaQivgZ_djnnzrkWlJXygIfFkbwg3o).
5. Google Sheets Tag file Id (default value - 14EpCUWZvnZrU2fhjrRw2p6qr1_E1QJzXqlUCi6CEyXc). This file contains modules/packages/classes/files mapping on predefined tags.
## Usage
1. For redeploy resources:

when your current IDE editor window contains resource file:

- press Ctrl+\ Ctrl+P

or

- use "Redeploy Resource" item of right mouse click menu popup:

![Popup Menu](https://i.imgur.com/wYt2BET.png)

When resource file is copied successfully you'll see popup message:

![Success message](https://i.imgur.com/RrDbLXi.png)

2. For knowledge database:

   select some code for comment and choose menu Help -> Knowledge Base. Нou don't have to select code if it is not necessary.
   The following form is displayed:
   ![Knowledge Base Form](https://i.imgur.com/69Ar0Lz.png)
   Fields "User Name", "Task" and "Tags" are filled automatically. "User Name" and "Task" fields are calculated from git repository information. "Tags" field is calculated by using modules/packages/classes/files mapping on predefined tags from Google Sheets Tag file.

   These fields can be empty if your current git branch does not contaion task code in its name or there is no mapping of your current module/package/class/file in Google Sheets Tag file.
## Limitations
Mvel files can not be redeployed.

## Problem
If exception raised during file coping it is reported in log file. To get the location of idea.log file choose IDE menu Help -> Show Log in Files.
