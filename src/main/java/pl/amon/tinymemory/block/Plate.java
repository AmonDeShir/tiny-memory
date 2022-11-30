package pl.amon.tinymemory.block;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import pl.amon.tinymemory.BlockEntityCreator;
import pl.amon.tinymemory.RedstoneBlock;

import static net.minecraft.core.Direction.*;

public class Plate<Entity extends BlockEntity> extends RedstoneBlock<Entity> {
  private static final Map<Direction, VoxelShape> BASE = new HashMap<>();

  static {
    BASE.put(UP,    Block.box(0, 14, 0,16, 16, 16));
    BASE.put(DOWN,  Block.box(0,0,0,16,2,16));
    BASE.put(NORTH, Block.box(0, 0, 0,16, 16, 2));
    BASE.put(EAST,  Block.box(14,0,0,16,16,16));
    BASE.put(SOUTH, Block.box(0,0,14,16,16,16));
    BASE.put(WEST,  Block.box(0,0,0,2,16,16));
  }

  public Plate(BlockEntityCreator.Creator<Entity> creator, BlockEntityCreator.EntityTicker ticker) {
    super(Block.Properties.of(Material.STONE).strength(2f), creator, ticker);
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext context) {
    return BASE.get(state.getValue(BlockStateProperties.FACING));
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext context) {
    return BASE.get(state.getValue(BlockStateProperties.FACING));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace().getOpposite());
  }

  @Override
  public VoxelShape getOcclusionShape(BlockState state, BlockGetter source, BlockPos pos) {
    return BASE.get(state.getValue(BlockStateProperties.FACING));
  }
}