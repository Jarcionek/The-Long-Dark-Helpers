# The-Long-Dark-Helpers
Helper programs for playing The Long Dark



# Map Helper

## How does it work?

To create a new map, you have to load an image. You can grab an image of the map from the Internet, take a screenshot of what you mapped in game or you could even draw it yourself.

Right click anywhere to place a new marker.

Left or right click an existing marker to interact with it.

![Screenshot](Screenshot.PNG "Screenshot")

## Installation

Simply download and run the jar file. You need to have [Java](https://www.java.com/en/download/) installed.

There is no really any installation - no shortcuts will be added to the desktop or the Start menu, nothing will be added
to the Program Files folders or to the Windows registry. The program will create a folder in its current directory
and all data it saves will be in this folder.

## Releases

You can download jar files from this [Google drive folder](https://drive.google.com/open?id=1a-Tr_NBwtsMtFzHucmmitY7k2Cus0NJt).

See all the updates in the [changelog](CHANGELOG.md).

## Build the source code yourself

Build jar file by running `mvn clean compile assembly:single`.



# Feats Backup

Have you ever lost your feats progression and sandbox stats due to faulty Steam sync or any other reason?

[This script](featsBackup.bat) makes a backup copy of user001* files where these information are stored.
