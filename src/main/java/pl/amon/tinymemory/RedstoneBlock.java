package pl.amon.tinymemory;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RedstoneBlock<Entity extends BlockEntity> extends Block implements EntityBlock {
  private BlockEntityCreator.Creator<Entity> entityCreator;
  private BlockEntityCreator.EntityTicker entityTicker;

  public RedstoneBlock(Properties props, BlockEntityCreator.Creator<Entity> entityCreator, BlockEntityCreator.EntityTicker entityTicker) {
    super(props);
    this.entityCreator = entityCreator;
    this.entityTicker = entityTicker;
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return entityCreator.newBlockEntity(pos, state);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return entityTicker.getTicker(level, state, type);
  }

  @Override
  public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
    // TODO change to false if the block isn't panelTile.
    
    return true;
  }

  @Override
  public boolean shouldCheckWeakPower(BlockState state, LevelReader world, BlockPos pos, Direction directionFromNeighborToThis) {
    //returning false to override default behavior and allow the block entity to specify its redstone output
    return false;
  }


}
