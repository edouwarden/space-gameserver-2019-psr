package fr.edouwarden.space.resources;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

/**
 * Terrain generator for the space layer.
 *
 * <ul>
 *   <li>{@code space}: an empty void with sparse asteroid clusters.</li>
 *   <li>{@code exoplanet}: a low-noise rocky surface with ore veins.</li>
 * </ul>
 *
 * <p>Any other world receives empty chunks; those worlds are expected to use the
 * server's normal generator.</p>
 */
public class CustomChunkGenerator extends ChunkGenerator {

    private static final int SPACE_FLOOR = 48;
    private static final int SPACE_CEILING = 96;
    private static final int PLANET_SEA_LEVEL = 64;

    /**
     * The noise field is sampled in world coordinates and seeded once for the
     * whole world, so adjacent chunks continue the same field. Re-seeding per
     * chunk would restart the field at every 16-block border and leave seams.
     */
    private static final long TERRAIN_SEED = 4701234567L;

    /** Side of an asteroid placement cell. One candidate rock per cell. */
    private static final int ASTEROID_CELL = 16;

    /** Probability that a cell contains a rock. */
    private static final double ASTEROID_DENSITY = 0.42D;

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ,
                                       BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        if ("space".equals(world.getName())) {
            generateSpace(chunk, random, chunkX, chunkZ, biome);
        } else if ("exoplanet".equals(world.getName())) {
            generatePlanet(chunk, random, chunkX, chunkZ, biome);
        }
        return chunk;
    }

    private void generateSpace(ChunkData chunk, Random random, int chunkX, int chunkZ,
                               BiomeGrid biome) {
        setBiome(biome, Biome.THE_END);

        int minCellY = SPACE_FLOOR / ASTEROID_CELL;
        int maxCellY = (SPACE_CEILING - 1) / ASTEROID_CELL;

        // A rock may be centred in a neighbouring cell and still overlap this chunk,
        // so every cell within one cell of the chunk is visited.
        for (int cellX = chunkX - 1; cellX <= chunkX + 1; cellX++) {
            for (int cellZ = chunkZ - 1; cellZ <= chunkZ + 1; cellZ++) {
                for (int cellY = minCellY; cellY <= maxCellY; cellY++) {
                    placeAsteroid(chunk, cellX, cellY, cellZ, chunkX, chunkZ);
                }
            }
        }
    }

    /**
     * Places at most one rock in a 16x16x16 cell.
     *
     * <p>The prototype derived the rock's vertical position from 2-D noise, so every
     * column above a threshold stacked a sphere on top of its neighbour and the field
     * became a forest of pillars. Here the cell decides whether a rock exists and
     * where its centre is, which keeps rocks separated and makes density independent
     * of height.</p>
     */
    private void placeAsteroid(ChunkData chunk, int cellX, int cellY, int cellZ,
                               int chunkX, int chunkZ) {
        Random cellRandom = new Random(cellSeed(cellX, cellY, cellZ));
        if (cellRandom.nextDouble() > ASTEROID_DENSITY) {
            return;
        }

        int radius = 2 + cellRandom.nextInt(4); // 2..5, so 5..11 blocks wide
        int centerX = cellX * ASTEROID_CELL + 4 + cellRandom.nextInt(8);
        int centerY = cellY * ASTEROID_CELL + 4 + cellRandom.nextInt(8);
        int centerZ = cellZ * ASTEROID_CELL + 4 + cellRandom.nextInt(8);
        centerY = Math.max(SPACE_FLOOR, Math.min(SPACE_CEILING, centerY));

        int chunkBaseX = chunkX * 16;
        int chunkBaseZ = chunkZ * 16;
        int minX = Math.max(0, centerX - radius - chunkBaseX);
        int maxX = Math.min(15, centerX + radius - chunkBaseX);
        int minZ = Math.max(0, centerZ - radius - chunkBaseZ);
        int maxZ = Math.min(15, centerZ + radius - chunkBaseZ);
        int minY = Math.max(1, centerY - radius);
        int maxY = Math.min(255, centerY + radius);
        int radiusSquared = radius * radius;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y++) {
                    int dx = chunkBaseX + x - centerX;
                    int dy = y - centerY;
                    int dz = chunkBaseZ + z - centerZ;
                    if (dx * dx + dy * dy + dz * dz > radiusSquared) {
                        continue;
                    }
                    if (chunk.getType(x, y, z) != Material.AIR) {
                        continue;
                    }
                    chunk.setBlock(x, y, z, asteroidMaterial(cellRandom, dy, radius));
                }
            }
        }
    }

    /**
     * Deterministic per-cell seed: the same cell always produces the same rock,
     * independent of generation order, and neighbouring cells stay decorrelated.
     */
    private static long cellSeed(int cellX, int cellY, int cellZ) {
        long hash = TERRAIN_SEED;
        hash ^= (long) cellX * 0x9E3779B97F4A7C15L;
        hash ^= (long) cellY * 0xC2B2AE3D27D4EB4FL;
        hash ^= (long) cellZ * 0x165667B19E3779F9L;
        hash ^= hash >>> 29;
        hash *= 0xBF58476D1CE4E5B9L;
        hash ^= hash >>> 32;
        return hash;
    }

    private Material asteroidMaterial(Random random, int dy, int radius) {
        if (dy >= radius - 1 && random.nextInt(4) == 0) {
            return Material.GRAVEL;
        }
        int roll = random.nextInt(100);
        if (roll < 3) {
            return Material.DIAMOND_ORE;
        }
        if (roll < 10) {
            return Material.IRON_ORE;
        }
        if (roll < 18) {
            return Material.COAL_ORE;
        }
        return random.nextBoolean() ? Material.STONE : Material.ANDESITE;
    }

    private void generatePlanet(ChunkData chunk, Random random, int chunkX, int chunkZ,
                                BiomeGrid biome) {
        setBiome(biome, Biome.PLAINS);

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(TERRAIN_SEED), 6);
        generator.setScale(0.008D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                double noise = generator.noise(worldX, worldZ, 2.0D, 0.5D);
                int height = PLANET_SEA_LEVEL + (int) Math.round(noise * 12.0D);
                height = Math.max(4, Math.min(120, height));

                chunk.setBlock(x, 0, z, Material.BEDROCK);
                for (int y = 1; y < height - 4; y++) {
                    chunk.setBlock(x, y, z, Material.STONE);
                }
                for (int y = height - 4; y < height; y++) {
                    chunk.setBlock(x, y, z,
                            random.nextInt(6) == 0 ? Material.GRAVEL : Material.ANDESITE);
                }
                chunk.setBlock(x, height, z, Material.END_STONE);

                if (random.nextInt(220) == 0) {
                    chunk.setBlock(x, height - 2, z, Material.IRON_ORE);
                } else if (random.nextInt(140) == 0) {
                    chunk.setBlock(x, height - 3, z, Material.COAL_ORE);
                }
            }
        }
    }

    private void setBiome(BiomeGrid biome, Biome value) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biome.setBiome(x, z, value);
            }
        }
    }
}
