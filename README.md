## Vampirism Integrations Mod - WIP

Adds integrations for Vampirism with several other mods.

These integrations are implemented in this additional mod to:
- Keep Vampirism's project cleaner by having less dependencies
- Allow integrations to be updated seperate from the main mod
- Allow Vampirism to be updated seperate from the integrations (Allows faster updates of Vampirism for new MC version)
- Test Vampirism's API

Only the most important (and deeply integrated) ones are included in the main mod [JEI, GuideAPI]

### Reasons
a) Mod integrations are a lot of work regarding Minecraft updates. If MC updates, but the other mods are not finished yet, I have to uncomment all the relevant parts in Vampirism codes, remove the dependencies and redo all these things once the mod eventually updates.
b) If a other mods changes and requires changes, I don't want to release an update for Vampirism each time
c) It keeps Vampirism's code slightly more organized
d) It verifies that Vampirism's API is working, shows me areas for impovement and can act as an example.
e) It makes it more straight-forward for others contribute and add more integrations (hope this is gonna happen, but I did not have any luck so far)

The most important integrations will say in Vampirism (JEI, GuideAPI,?), but any additional (hopefully many) will be added into the seperate mod.
If you are already installing many mods or a modpack it should not make that much a difference anyway.

### Stuff
TODO
