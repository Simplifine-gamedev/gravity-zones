package com.orca.gravityzones.mixin;

import com.orca.gravityzones.GravityZonesMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "travel", at = @At("HEAD"))
    private void onTravel(Vec3d movementInput, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // Only affect players
        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        ChunkPos chunkPos = entity.getChunkPos();

        if (GravityZonesMod.isGravityZone(chunkPos)) {
            // Get current velocity
            Vec3d velocity = entity.getVelocity();

            // Invert gravity effect - instead of falling down, fall up
            // Normal gravity adds about -0.08 per tick, so we counteract and reverse it
            double gravityInversion = 0.16; // Double the normal gravity but upward

            // Apply inverted gravity
            entity.setVelocity(velocity.x, velocity.y + gravityInversion, velocity.z);

            // Limit upward speed to prevent going too fast
            Vec3d newVel = entity.getVelocity();
            if (newVel.y > 2.0) {
                entity.setVelocity(newVel.x, 2.0, newVel.z);
            }

            // Check if player hit the ceiling (build limit at Y=320)
            if (entity.getY() >= 318) {
                // Stop at ceiling, allow walking on it
                entity.setVelocity(newVel.x, 0, newVel.z);
                entity.setOnGround(true);
            }

            // Check if there's a block above the player's head
            if (!entity.getWorld().isAir(entity.getBlockPos().up(2))) {
                // Player has a ceiling - can walk on it
                if (newVel.y > 0) {
                    entity.setVelocity(newVel.x, 0, newVel.z);
                }
            }
        }
    }
}
