package network.walrus.ubiquitous.bukkit.tracker.trackers.base;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import network.walrus.ubiquitous.bukkit.tracker.base.AbstractTracker;
import network.walrus.ubiquitous.bukkit.tracker.trackers.ExplosiveTracker;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;

/**
 * @author Overcast Network
 * @see ExplosiveTracker
 */
public class SimpleExplosiveTracker extends AbstractTracker implements ExplosiveTracker {

  private final Map<Block, OfflinePlayer> placedBlocks = Maps.newHashMap();
  private final Map<TNTPrimed, OfflinePlayer> ownedTNTs = Maps.newHashMap();

  public boolean hasOwner(@Nonnull TNTPrimed entity) {
    Preconditions.checkNotNull(entity, "tnt entity");

    return this.ownedTNTs.containsKey(entity);
  }

  public @Nullable OfflinePlayer getOwner(@Nonnull TNTPrimed entity) {
    Preconditions.checkNotNull(entity, "tnt entity");

    return this.ownedTNTs.get(entity);
  }

  public @Nullable OfflinePlayer setOwner(
      @Nonnull TNTPrimed entity, @Nullable OfflinePlayer player) {
    Preconditions.checkNotNull(entity, "tnt entity");

    if (player != null) {
      return this.ownedTNTs.put(entity, player);
    } else {
      return this.ownedTNTs.remove(entity);
    }
  }

  public boolean hasPlacer(@Nonnull Block block) {
    Preconditions.checkNotNull(block, "block");

    return this.placedBlocks.containsKey(block);
  }

  public @Nullable OfflinePlayer getPlacer(@Nonnull Block block) {
    Preconditions.checkNotNull(block, "block");

    return this.placedBlocks.get(block);
  }

  public @Nullable OfflinePlayer setPlacer(@Nonnull Block block, @Nullable OfflinePlayer player) {
    Preconditions.checkNotNull(block, "block");

    if (player != null) {
      return this.placedBlocks.put(block, player);
    } else {
      return this.placedBlocks.remove(block);
    }
  }

  public void clear(@Nonnull World world) {
    Preconditions.checkNotNull(world, "world");

    // clear information about blocks in that world
    for (Iterator<Map.Entry<Block, OfflinePlayer>> it = this.placedBlocks.entrySet().iterator();
        it.hasNext(); ) {
      Block block = it.next().getKey();
      if (block.getWorld().equals(world)) {
        it.remove();
      }
    }

    // clear information about entities in that world
    for (Iterator<Map.Entry<TNTPrimed, OfflinePlayer>> it = this.ownedTNTs.entrySet().iterator();
        it.hasNext(); ) {
      TNTPrimed tnt = it.next().getKey();
      if (tnt.getWorld().equals(world)) {
        it.remove();
      }
    }
  }

  @Override
  public void transferOwnership(OfflinePlayer original, OfflinePlayer newOwner) {
    this.placedBlocks.replaceAll(
        (b, p) -> {
          if (p.getUniqueId().equals(original.getUniqueId())) {
            return newOwner;
          } else {
            return p;
          }
        });
    this.ownedTNTs.replaceAll(
        (b, p) -> {
          if (p.getUniqueId().equals(original.getUniqueId())) {
            return newOwner;
          } else {
            return p;
          }
        });
  }
}
