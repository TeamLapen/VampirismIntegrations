Vampirism Integrations for MC 1.20 - Latest branch [![](http://cf.way2muchnoise.eu/versions/For%20MC_vampirism-integrations_all.svg)](https://minecraft.curseforge.com/projects/vampirism-integrations) [![Build Status](https://travis-ci.org/TeamLapen/VampirismIntegrations.svg?branch=1.12)](https://travis-ci.org/TeamLapen/VampirismIntegrations) 
============================================
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://maxanier.de) 

This mod handles integrations or respectively compatibility with other mods.

Curseforge Page: [https://minecraft.curseforge.com/projects/vampirism-integrations](https://minecraft.curseforge.com/projects/vampirism-integrations)  

While the JEI and GuideAPI integration will stay inside the main mod, any additional compatibility will be implemented here.

Therefore, this mod is only useful if you are using one or more of the supported mods:

## Reasons
a) Mod integrations are a lot of work regarding Minecraft updates. If MC updates, but the other mods are not finished yet, I have to uncomment all the relevant parts in Vampirism codes, remove the dependencies and redo all these things once the mod eventually updates.  
b) If a other mods changes and requires changes, I don't want to release an update for Vampirism each time  
c) It keeps Vampirism's code slightly more organized  
d) It verifies that Vampirism's API is working, shows me areas for improvement and can act as an example.  
e) It makes it more straight-forward for others contribute and add more integrations (hope this is gonna happen, but I did not have any luck so far)  
  
The most important integrations will stay in Vampirism (JEI, GuideAPI,?), but any additional (hopefully many) will be added into the separate mod.  
If you are already installing many mods or a modpack it should not make that much a difference anyway.

## Configuration
Integrations can be disabled per mod.  
Some integrations also allow more detailed customization.

## Setting up the development environment
If you would like to compile your own versions or even contribute to Vampirism Integrations's development you need to set up a dev environment.
The following example instructions will setup IntelliJ (Free community edition or Non-Free Ultimate edition). If you already have a setup or want to use another IDE, jump [here](#setting-up-vampirism-integrations-in-another-environment).

#### IntelliJ
1. Make sure you have the Java **JDK** (Java 8) as well as the IntelliJ IDE installed.
2. If you want to contribute to the development (via pull requests), fork Vampirism Integrations on Github.
3. (Optionally) Install Git, so you can clone the repository and push changes.
4. Clone (`git clone https://github.com/TeamLapen/VampirismIntegrations`) or [download](https://github.com/TeamLapen/VampirismIntegrations/archive/master.zip) VampirismIntegrations to a new "VampirismIntegrations" folder.
5. In IntelliJ use `New...` -> `New from Version Control` -> Fill out repo, directory and name
6. After cloning is done IntelliJ offers you to import a unlinked Gradle Project. Click this.
7. Select `Create directories for empty content roots` and __deselect__ `Create seperate module per source set` [Image](https://picload.org/image/ripradpa/importprojectfromgradle_001.png). If you do not get that dialog, you might have to edit your .idea/gradle.xml to include ` <option name="resolveModulePerSourceSet" value="false" />` (see [here](https://gist.github.com/maxanier/142b27c7800f9512cc4ef3d4e10b9bfd)) and refresh the gradle project again.  
8. Refresh the gradle project  
9. Run `genIntellijRuns` and edit the run config to use the correct module
10. Make sure `Settings -> Build, Execution, Deployment -> Compiler -> 'Add runtime assertions for not-null-annotated methods and parameters' is disabled` (Unfortunately required, requires rebuild if the project has been built before)
11. You might have to modify the project's compiler output path  


That's it.

#### Setting up Vampirism Integrations in another environment
If you would like to setup Vampirism Integrations in another way or another IDE, you should pay regard to the following points.  
1. Make sure `src/main/java`, `src/api/java´ and `src/lib/java` are marked as source folders and `src/main/resources` and `src/lib/resources` are marked as resource folders.  
2. Vampirism Integrations has several dependencies (e.g. Waila), which are specified in the gradle files and should be automatically downloaded and added when you run import/refresh the gradle project  
3. Vampirism Integrations requires at least Java 8 
4. Some packages might have to be excluded in your IDE. See 'gradle/forge.gradle'.


## Licence
This mod is licenced under [LGPLv3](https://raw.githubusercontent.com/TeamLapen/VampirismIntegrations/master/LICENCE)