package pl.amon.tinymemory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockEntityCreator {
  interface Creator<Entity extends BlockEntity> {
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state);
  }

  interface EntityTicker {
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type);
  }
}
