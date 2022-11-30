package pl.amon.tinymemory.block.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import pl.amon.tinymemory.setup.Registration;

public class ROM extends BlockEntity {
  public ROM(BlockPos pos, BlockState state) {
    super(Registration.ROM_BLOCK_ENTITY.get(), pos, state);
  }

  public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return (level1, blockPos, blockState, t) -> {
      if (t instanceof ROM rom) {
        //rom.tick();
      }
    };
  }

  

  @Override
  @Nullable
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

}