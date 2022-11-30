package pl.amon.tinymemory.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import pl.amon.tinymemory.block.entity.ProjectTableBlockEntity;
import pl.amon.tinymemory.setup.Registration;

public class ProjectTable extends BaseEntityBlock {
  public ProjectTable() {
    super(Block.Properties.of(Material.METAL).strength(2f));
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ProjectTableBlockEntity(pos, state);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity blockEntity = level.getBlockEntity(pos);

      if (blockEntity instanceof ProjectTableBlockEntity entity) {
        entity.drops();
      } 
    }
    
    super.onRemove(state, level, pos, newState, isMoving);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    if (!level.isClientSide()) {
      BlockEntity blockEntity = level.getBlockEntity(pos);
      openGUI(blockEntity, (ServerPlayer)player, pos);
    }

    return super.use(state, level, pos, player, hand, hitResult);
  }

  protected void openGUI(BlockEntity blockEntity, ServerPlayer player, BlockPos pos) {
    if (blockEntity instanceof ProjectTableBlockEntity entity) {
      NetworkHooks.openGui(player, entity, pos);
    }
    else {
      throw new IllegalStateException("Missing container provider!");
    }
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, Registration.PROJECT_TABLE_BLOCK_ENTITY.get(), ProjectTableBlockEntity::tick);
  }
}
