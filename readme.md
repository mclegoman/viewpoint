![](https://raw.githubusercontent.com/mclegoman/viewpoint/refs/heads/1.21.4/assets/logo.png)
[Download from Modrinth](https://modrinth.com/mod/viewpoint)

## About
Viewpoint is a minimal version of [Perspective](https://modrinth.com/mod/mclegoman-perspective) with only the zoom and hold perspective features.

## Dependencies
- [Fabric API](https://modrinth.com/mod/fabric-api) or [Quilted Fabric API (QFAPI)](https://modrinth.com/mod/qsl)
    - `fabric-key-binding-api-v1`
    - `fabric-lifecycle-events-v1`
- Java 21 or later. (Built with the Microsoft build of OpenJDK 21.0.2)

## Features  
- Zoom  
  - Logarithmic and Linear Zoom Scales  
  - Smooth and Instant Transitions  
  - Scaled and Unscaled Mouse and FOV effects  
  - Cinematic Mouse  
- Hold Perspective  
  - Switch to a perspective whilst using a keybinding  
  - Distance Multiplier  
- Set Perspective  
  - Switch to a perspective after using a keybinding  
- Take Panoramic Screenshot  
  - Saves screenshots required for a panorama at the following directory: `%rundir%/panoramas/`  
    - Unlike Perspective, viewpoint **does not** save the screenshots as a resource pack.  
- Overlays  
  - Version    
    - This renders the current version in the top left of the screen similarly to how beta minecraft did  
  - Position  
    - Renders current position similarly to bedrock  
  - Time  
    - Renders the current time in the same style as the position overlay  
  - Day
    - Renders the current day count in the same style as the position overlay  
  - Biome  
    - Renders the current biome in the same style as the position overlay  
  - CPS  
    - Renders the current cps in the same style as the position overlay

#
Licensed under LGPL-3.0-or-later.

**This mod is not affiliated with/endorsed by Mojang Studios or Microsoft.**  
Some game assets are property of Mojang Studios and fall under Minecraft EULA.  
