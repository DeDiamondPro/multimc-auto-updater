# multimc-auto-updater
<p align="center">
<a href="https://github.com/DeDiamondPro/multimc-auto-updater/releases" target="_blank">
<img alt="release" src="https://img.shields.io/github/v/release/DeDiamondPro/multimc-auto-updater?color=00FFFF&style=for-the-badge" />
</a>
<a href="https://github.com/DeDiamondPro/multimc-auto-updater/releases" target="_blank">
<img alt="downloads" src="https://img.shields.io/github/downloads/DeDiamondPro/multimc-auto-updater/total?color=00FFFF&style=for-the-badge" />
</a>
<a href="https://github.com/DeDiamondPro/multimc-auto-updater/blob/master/LICENSE">
    <img alt="license" src="https://img.shields.io/github/license/DeDiamondPro/multimc-auto-updater?color=00FFFF&style=for-the-badge">
 </a>
  <a href="https://github.com/DeDiamondPro/multimc-auto-updater/">
    <img alt="lines" src="https://img.shields.io/tokei/lines/github/DeDiamondPro/multimc-auto-updater?color=00FFFF&style=for-the-badge">
 </a>
    <a href="https://discord.gg/ZBNS8jsAMd" target="_blank">
    <img alt="discord" src="https://img.shields.io/discord/822066990423605249?color=00FFFF&label=discord&style=for-the-badge" />
  </a>
 </p>
A program to automatically update your mods!

# How to setup
## Setting up the config
Create a folder and put the jar from [the latest release](https://github.com/DeDiamondPro/multimc-auto-updater/releases/latest) in the folder.
After that create a json file called "config.json" in the same folder, the folder should now look like this:

![image](https://user-images.githubusercontent.com/67508414/126784258-c431700c-7d92-434f-9c6b-cdea342ec3c9.png)

Now open the JSON file, in here you will need to specify the github links for the mods that the updater should update. Here is the format to do this:
```json
{
  "mods" : [
    {
      "github" : "https://github.com/DeDiamondPro/HyCord"
    }
  ]
}
```
You can add an other mod by just adding it underneath it like this:
```json
{
  "mods" : [
    {
      "github" : "https://github.com/DeDiamondPro/HyCord"
    },
    {
      "github" : "https://github.com/TGMDevelopment/Nick-Hider-Revamped-Forge"
    }
  ]
}
```
There are also some extra parameters one to use a prerelease which you do like so:
```json
{
  "mods" : [
    {
      "github" : "https://github.com/DeDiamondPro/HyCord",
      "usePre" : true
    }
  ]
}
```
And one to make it so in order to update to a version of a mod that mod's name needs to include something, this is usefull for when a mod puts releases for multiple versions in one release so it takes the mod for the right Minecraft version like so:
```json
{
  "mods" : [
    {
	  "github" : "https://github.com/QuickplayMod/quickplay",
	  "includes" : "1.8.9"
    }
  ]
}
```
Now you have configured the updater to update all the mods you want!
## Setting up with multimc
Multimc is required for this to work since multimc allows this program to run before it starts Minecraft, Multimc download: https://multimc.org/
Java is also required to run this program, to check if you have java installed you can open up a command prompt and type `java -version` if this that java isn't a known command please download it here: https://www.java.com/

Now open Multimc and go to the instance you want to automatically update your mods on and then click "edit instance" then on the left go the the "Settings" tab.
Now go to "Custom commands" and click on the checkmark next to it, then in "Pre-launch command" type `java -jar "path to jar file" "path to config file" "$INST_MC_DIR"`
Replace path to jar file with the absolute path to the jar file, to get this shift right click on the file and click "copy as path"
Also replace path to config file with the absolute path to the config file, you get this in the same way.

![Untitled](https://user-images.githubusercontent.com/67508414/126787570-1ed3711d-a487-4064-854d-ef6e2f591d7a.png)

The final result should about look like this:

![image](https://user-images.githubusercontent.com/67508414/126787841-96cc10f1-c21a-4bf4-a0db-fe66bda575cf.png)

Now you're ready to start automatically updating your mods!

# Question and support
If you have any questions or need support I'm willing to help you in the discord of my Minecraft mod HyCord, invite link: https://discord.gg/6v4ydWjDXu
