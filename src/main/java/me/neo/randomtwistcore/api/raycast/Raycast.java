package me.neo.randomtwistcore.api.raycast;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A class used for generating raycasts.
 *
 */
@SuppressWarnings("unused")
public class Raycast {

    private final double divider = 100.0D;

    private ArrayList<Material> passthroughMaterials = new ArrayList<>();

    private ArrayList<Location> testedLocations = new ArrayList<>();

    private World world;

    private double x;

    private double y;

    private double z;

    private double yaw;

    private double pitch;

    private double size;

    private RaycastType rayCastType;

    private Entity hurtEntity;

    private Block hurtBlock;

    private Location hurtLocation;

    private boolean showRayCast = false;

    private Entity owner;

    private Location rayCastLocation;

    public Raycast(Location loc, double size) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), size);
    }

    public Raycast(World world, double x, double y, double z, double yaw, double pitch, double size) {
        addPassthroughMaterial(Material.AIR);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.size = size;
    }

    /**
     * Calculated the raycast
     * @param rayCastType The {@link me.neo.randomtwistcore.api.raycast.Raycast.RaycastType} of the raycast.
     * @return If the raycast hit anything.
     */
    public boolean compute(RaycastType rayCastType) {
        this.testedLocations.clear();
        int length = 0;
        computeLocation(new Vector(0.0D, 0.0D, length + 50.0D));
        if (rayCastType == RaycastType.BLOCK) {
            this.rayCastType = RaycastType.BLOCK;
            while (this.passthroughMaterials.contains(this.rayCastLocation.getBlock().getType()) && length <= this.size * 100.0D) {
                this.testedLocations.add(this.rayCastLocation);
                length++;
                computeLocation(new Vector(0.0D, 0.0D, length + 50.0D));
                if (this.showRayCast)
                    this.world.spawnParticle(Particle.DRIP_LAVA, this.rayCastLocation.getX(), this.rayCastLocation.getY(), this.rayCastLocation.getZ(), 0, 0.0D, 0.0D, 0.0D);
            }
            if (!this.passthroughMaterials.contains(this.rayCastLocation.getBlock().getType())) {
                this.hurtBlock = this.rayCastLocation.getBlock();
                this.hurtLocation = this.rayCastLocation;
                return true;
            }
        } else if (rayCastType == RaycastType.ENTITY) {
            this.rayCastType = RaycastType.ENTITY;
            Collection<Entity> entities = this.world.getNearbyEntities(this.rayCastLocation, 0.01D, 0.01D, 0.01D);
            while ((entities.size() <= 0 || entities.contains(this.owner)) && length <= this.size * 100.0D) {
                this.testedLocations.add(this.rayCastLocation);
                length++;
                computeLocation(new Vector(0.0D, 0.0D, length + 50.0D));
                entities = this.world.getNearbyEntities(this.rayCastLocation, 0.01D, 0.01D, 0.01D);
                if (this.showRayCast) {
                    this.world.spawnParticle(Particle.DRIP_LAVA, this.rayCastLocation.getX(), this.rayCastLocation.getY(), this.rayCastLocation.getZ(), 0, 0.0D, 0.0D, 0.0D);
                }
            }
            if (entities.size() > 0) {
                for (Entity entity : entities) {
                    this.hurtEntity = entity;
                    this.hurtLocation = this.rayCastLocation;
                }
                return true;
            }
        } else if (rayCastType == RaycastType.ENTITY_AND_BLOCK) {
            Collection<Entity> entities = this.world.getNearbyEntities(this.rayCastLocation, 0.01D, 0.01D, 0.01D);
            while (this.passthroughMaterials.contains(this.rayCastLocation.getBlock().getType()) && (entities.size() <= 0 || entities.contains(this.owner)) && length <= this.size * 100.0D) {
                this.testedLocations.add(this.rayCastLocation);
                length++;
                computeLocation(new Vector(0.0D, 0.0D, length + 50.0D));
                entities = this.world.getNearbyEntities(this.rayCastLocation, 0.01D, 0.01D, 0.01D);
                if (this.showRayCast)
                    this.world.spawnParticle(Particle.DRIP_LAVA, this.rayCastLocation.getX(), this.rayCastLocation.getY(), this.rayCastLocation.getZ(), 0, 0.0D, 0.0D, 0.0D);
            }
            if (!this.passthroughMaterials.contains(this.rayCastLocation.getBlock().getType())) {
                this.rayCastType = RaycastType.BLOCK;
                this.hurtBlock = this.rayCastLocation.getBlock();
                this.hurtLocation = this.rayCastLocation;
                return true;
            }
            if (entities.size() > 0) {
                this.rayCastType = RaycastType.ENTITY;
                for (Entity entity : entities) {
                    this.hurtEntity = entity;
                    this.hurtLocation = this.rayCastLocation;
                }
                return true;
            }
        }
        return false;
    }

    private void computeLocation(Vector rayCastPos) {
        rayCastPos = RaycastAPIMath.rotate(rayCastPos, this.yaw, this.pitch);
        this.rayCastLocation = (new Location(this.world, this.x, this.y, this.z)).clone().add(rayCastPos.getX() / 100.0D, rayCastPos.getY() / 100.0D, rayCastPos.getZ() / 100.0D);
    }

    /**
     * Returns an {@link java.util.ArrayList} of {@link org.bukkit.Material} that the raycast can pass through.
     * @return The list of materials.
     */
    public ArrayList<Material> getPassthroughMaterials() {
        return this.passthroughMaterials;
    }

    /**
     * Sets the {@link java.util.ArrayList} of {@link org.bukkit.Material} that the raycast can pass through.
     * @param passthroughMaterials The list of materials.
     */
    public void setPassthroughMaterials(ArrayList<Material> passthroughMaterials) {
        this.passthroughMaterials = passthroughMaterials;
    }

    /**
     * Adds a {@link org.bukkit.Material} to the list of pass through materials.
     * @param mat The material to add.
     */
    public void addPassthroughMaterial(Material mat) {
        if (!this.passthroughMaterials.contains(mat))
            this.passthroughMaterials.add(mat);
    }

    /**
     * An {@link java.util.ArrayList} of {@link org.bukkit.Location}s that did not have a collision when tested.
     * @return A list of locations.
     */
    public ArrayList<Location> getTestedLocations() {
        return this.testedLocations;
    }

    /**
     * Returns the {@link org.bukkit.World} that the raycast is in.
     * @return The world.
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Sets the {@link org.bukkit.World} that the raycast is in.
     * @param world The world to set the raycast to.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Returns the X coordinate of the raycast.
     * @return The X coordinate.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Sets the X coordinate of the raycast.
     * @param x The X coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the Y coordinate of the raycast.
     * @return The Y coordinate.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets the X coordinate of the raycast.
     * @param y The Y coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the Z coordinate of the raycast.
     * @return The Z coordinate.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Sets the Z coordinate of the raycast.
     * @param z The Z coordinate.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Returns the yaw (horizontal rotation) of the raycast.
     * @return The yaw.
     */
    public double getYaw() {
        return this.yaw;
    }

    /**
     * Sets the yaw (horizontal rotation) of the raycast.
     * @param yaw The new yaw.
     */
    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    /**
     * Returns the pitch (vertical rotation) of the raycast.
     * @return The pitch.
     */
    public double getPitch() {
        return this.pitch;
    }

    /**
     * Sets the pitch (vertical rotation) of the raycast.
     * @param pitch The new pitch.
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets the length (in meters) of the raycast.
     * @return The Length.
     */
    public double getSize() {
        return this.size;
    }

    /**
     * Sets the length (in meters) of the raycast.
     * @param size The length.
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Returns the {@link me.neo.randomtwistcore.api.raycast.Raycast.RaycastType} of the raycast.
     * @return The RaycastType.
     */
    public RaycastType getRayCastType() {
        return this.rayCastType;
    }


    /**
     * Sets the {@link me.neo.randomtwistcore.api.raycast.Raycast.RaycastType} of the raycast.
     * It will determine what the raycast counts as a "hit"
     * @param rayCastType The RaycastType to set the raycast as.
     */
    public void setRayCastType(RaycastType rayCastType) {
        this.rayCastType = rayCastType;
    }

    /**
     * Gets the {@link org.bukkit.entity.Entity} that was hit.
     * Can be null if the {@link me.neo.randomtwistcore.api.raycast.Raycast.RaycastType} is Block only or if the raycast does not hit anything.
     * @return The hit entity.
     */
    public Entity getHurtEntity() {
        return this.hurtEntity;
    }

    /**
     * Sets the {@link org.bukkit.entity.Entity} that was hit.
     * @param hurtEntity The entity.
     */
    public void setHurtEntity(Entity hurtEntity) {
        this.hurtEntity = hurtEntity;
    }

    /**
     * Gets the {@link org.bukkit.block.Block} that was hit.
     * Can be null if the {@link me.neo.randomtwistcore.api.raycast.Raycast.RaycastType} is Entity only or if the raycast does not hit anything.
     * @return The hit block.
     */
    public Block getHurtBlock() {
        return this.hurtBlock;
    }

    /**
     * Sets the {@link org.bukkit.block.Block} that was hit.
     * @param hurtBlock The block.
     */
    public void setHurtBlock(Block hurtBlock) {
        this.hurtBlock = hurtBlock;
    }

    /**
     * Gets the {@link org.bukkit.Location} where the hit happened.
     * Can be null if the raycast does not hit anything.
     * @return The location where the raycast hit something.
     */
    public Location getHurtLocation() {
        return this.hurtLocation;
    }

    /**
     * Sets the {@link org.bukkit.Location} where the hit happeed.
     * @param hurtLocation The location.
     */
    public void setHurtLocation(Location hurtLocation) {
        this.hurtLocation = hurtLocation;
    }

    /**
     * Returns if the raycast spawns a row of particles in its path.
     * If this is false then the rayast is invisible.
     * @return If the raycast is visible.
     */
    public boolean isShowRayCast() {
        return this.showRayCast;
    }

    /**
     * Sets if the raycast spawns particles in its path.
     * @param showRayCast If the raycast should be shown or not.
     */
    public void setShowRayCast(boolean showRayCast) {
        this.showRayCast = showRayCast;
    }

    /**
     * Gets the "step" of the raycast.
     * The higher the number the more precise but resource-intensive the raycast will be.
     * @return The Divider.
     */
    public double getDivider() {
        return divider;
    }

    /**
     * Returns the {@link org.bukkit.entity.Entity} that owns the raycast.
     * @return The owner of the raycast.
     */
    public Entity getOwner() {
        return this.owner;
    }

    /**
     * Sets the {@link org.bukkit.entity.Entity} as the owner of the raycast.
     * @param owner The new owner of the raycast.
     */
    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    /**
     * An enum containing the RaycastTypes
     */
    public enum RaycastType {
        /**
         * The raycast will only compute when it hits a "valid" {@link org.bukkit.block.Block} or an {@link org.bukkit.entity.Entity}.
         * A "valid" block is a block whose {@link org.bukkit.Material} is not defined in the passThrough materials list.
         * Use {@link #getPassthroughMaterials()} for a list of materials the raycast can pass through.
         */
        ENTITY_AND_BLOCK,
        /**
         * The raycast will only compute when it hits an {@link org.bukkit.entity.Entity}.
         */
        ENTITY,
        /**
         * The raycast will only compute when it hits a "valid" {@link org.bukkit.block.Block}
         * A "valid" block is a block whose {@link org.bukkit.Material} is not defined in the passThrough materials list.
         * Use {@link #getPassthroughMaterials()} for a list of materials the raycast can pass through.
         */
        BLOCK
    }
}
