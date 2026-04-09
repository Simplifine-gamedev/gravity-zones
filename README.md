# Gravity Zones

A Minecraft Fabric mod that creates random chunks with inverted gravity throughout your world.

## Features

- **Inverted Gravity Zones**: Approximately 1 in 20 chunks in your world are designated as gravity zones where gravity is completely inverted
- **Fall Upward**: When you enter a gravity zone, you'll start falling upward toward the sky limit instead of downward
- **Walk on Ceilings**: Build ceilings inside gravity zones and walk on them upside-down! Create unique bases and structures that defy conventional Minecraft physics
- **Visual Indicators**: Gravity zone chunk borders are marked with subtle floating End Rod particles, making them easy to identify
- **Toggle Particles**: Use the `/gravityzones` command to toggle particle visibility on or off per player

## How It Works

### Gravity Zone Generation
- Gravity zones are deterministically generated based on chunk coordinates
- The same chunks will always be gravity zones across all players and server restarts
- About 5% of all chunks (1 in 20) become gravity zones
- Zone locations are calculated using a seeded hash of chunk coordinates for consistency

### Entering a Gravity Zone
1. Walk into any gravity zone chunk
2. You'll immediately start falling upward
3. Keep rising until you hit the world height limit (Y=320) or a ceiling block

### Building in Gravity Zones
- Place blocks above you to create ceilings
- These ceilings become your new "floor" in the gravity zone
- You can build elaborate upside-down structures
- Exit the chunk to return to normal gravity

### Particle Indicators
- Floating End Rod particles spawn at the borders of gravity zone chunks
- These particles help you identify where gravity zones begin and end
- Players can toggle particle visibility using the `/gravityzones` command

## Commands

| Command | Description |
|---------|-------------|
| `/gravityzones` | Toggles gravity zone particle visibility for the player |

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.0+
- Fabric API
- Java 21

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.1
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download `gravity-zones-1.0.0.jar` from the releases
4. Place the JAR file in your `mods` folder
5. Launch Minecraft with the Fabric profile

## Building from Source

```bash
git clone https://github.com/Simplifine-gamedev/gravity-zones.git
cd gravity-zones
./gradlew build
```

The compiled JAR will be in `build/libs/gravity-zones-1.0.0.jar`

## Tips for Playing

- **Survival Mode**: Be careful! Falling upward can be just as deadly as falling downward if you exit a gravity zone at high altitude
- **Building**: Plan your structures with gravity zones in mind - upside-down farms, mob traps, and bases are all possible
- **Exploration**: Use the particle indicators to map out gravity zones in your world
- **Multiplayer**: Each player can independently toggle particle visibility

## Technical Details

- Uses Fabric Mixin to modify player movement/gravity calculations
- Gravity inversion is applied in the `LivingEntity.travel()` method
- Zone calculation uses a deterministic pseudo-random algorithm based on chunk coordinates
- Particle spawning occurs server-side for all nearby gravity zone chunks

## Author

Created by ali77sina

## License

This mod is provided as-is for personal and server use.
