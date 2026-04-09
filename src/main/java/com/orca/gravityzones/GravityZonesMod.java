package com.orca.gravityzones;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class GravityZonesMod implements ModInitializer {
    public static final String MOD_ID = "gravity-zones";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final long SEED = 0xDEADBEEF;
    private static final Set<UUID> particlesDisabled = new HashSet<>();

    @Override
    public void onInitialize() {
        LOGGER.info("Gravity Zones mod initialized!");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(dispatcher);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (!particlesDisabled.contains(player.getUuid())) {
                    spawnChunkBorderParticles(player);
                }
            }
        });
    }

    private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gravityzones")
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player != null) {
                    UUID uuid = player.getUuid();
                    if (particlesDisabled.contains(uuid)) {
                        particlesDisabled.remove(uuid);
                        context.getSource().sendFeedback(() -> Text.literal("Gravity zone particles enabled"), false);
                    } else {
                        particlesDisabled.add(uuid);
                        context.getSource().sendFeedback(() -> Text.literal("Gravity zone particles disabled"), false);
                    }
                }
                return 1;
            }));
    }

    private void spawnChunkBorderParticles(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        ChunkPos chunkPos = player.getChunkPos();

        // Check surrounding chunks for gravity zones
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int cx = chunkPos.x + dx;
                int cz = chunkPos.z + dz;

                if (isGravityZone(cx, cz)) {
                    // Spawn particles at chunk borders
                    double baseX = cx * 16;
                    double baseZ = cz * 16;
                    double playerY = player.getY();

                    // Only spawn a few particles per tick to reduce lag
                    if (world.getTime() % 5 == 0) {
                        Random rand = new Random();
                        for (int i = 0; i < 3; i++) {
                            // Particles along chunk edges
                            double y = playerY + rand.nextDouble() * 10 - 5;

                            // North edge
                            world.spawnParticles(ParticleTypes.END_ROD,
                                baseX + rand.nextDouble() * 16, y, baseZ,
                                1, 0, 0.1, 0, 0.01);
                            // South edge
                            world.spawnParticles(ParticleTypes.END_ROD,
                                baseX + rand.nextDouble() * 16, y, baseZ + 16,
                                1, 0, 0.1, 0, 0.01);
                            // West edge
                            world.spawnParticles(ParticleTypes.END_ROD,
                                baseX, y, baseZ + rand.nextDouble() * 16,
                                1, 0, 0.1, 0, 0.01);
                            // East edge
                            world.spawnParticles(ParticleTypes.END_ROD,
                                baseX + 16, y, baseZ + rand.nextDouble() * 16,
                                1, 0, 0.1, 0, 0.01);
                        }
                    }
                }
            }
        }
    }

    public static boolean isGravityZone(int chunkX, int chunkZ) {
        // Use deterministic random based on chunk coordinates
        // 1 in 20 chunks is a gravity zone
        Random rand = new Random((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L + SEED);
        return rand.nextInt(20) == 0;
    }

    public static boolean isGravityZone(ChunkPos pos) {
        return isGravityZone(pos.x, pos.z);
    }
}
