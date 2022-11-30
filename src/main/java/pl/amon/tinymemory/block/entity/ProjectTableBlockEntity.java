package pl.amon.tinymemory.block.entity;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.amon.tinymemory.screen.ProjectTableMenu;
import pl.amon.tinymemory.setup.Registration;

public class ProjectTableBlockEntity extends BlockEntity implements MenuProvider {
  private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
    };
  };

  public static final int MAX_BITES = 6120;
  protected final ContainerData data;
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private int bites = 1;
  private int layer = 0;
  private int[] values = new int[MAX_BITES];

  public ProjectTableBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.PROJECT_TABLE_BLOCK_ENTITY.get(), pos, state);

    this.data = new ContainerData() {
      /**
       * 0 = bites
       * 1 = layer
       * n = layer1, layer2, layer3 ...
       */
      @Override
      public int get(int index) {
        return switch(index) {
          case 0 -> ProjectTableBlockEntity.this.bites;
          case 1 -> ProjectTableBlockEntity.this.layer;
          default -> ProjectTableBlockEntity.this.values[index - 2];
        };
      }

      @Override
      public void set(int index, int value) {
        switch(index) {
          case 0 -> ProjectTableBlockEntity.this.bites = value;
          case 1 -> ProjectTableBlockEntity.this.layer = value;
          default -> ProjectTableBlockEntity.this.values[index - 2] = value;
        };
      }

      @Override
      public int getCount() {
        return MAX_BITES + 2;
      }
    };
  }

  protected void remapValueArray(int from, int to) {
    int[] remapped = new int[MAX_BITES];
    int max = Math.max(from, to);

    Arrays.fill(remapped, 0);

    if (from == to) {
      return;
    }

    for (int i = 0; i < values.length; i++) {
      int n = i + Math.floorDiv(i, 32 - max);
      
      if (from > to) {
        remapped[n] = values[i];
      }
      else {
        remapped[i] = values[n];
      }
    }
  }

  public int getPortValue(int index) {
    return values[(32 - bites) * layer + index];
  }

  public void setPortValue(int index, int value) {
    values[(32 - bites) * layer + index] = value;
  }

  @Override
  public Component getDisplayName() {
    return Component.literal("Project Table");
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new ProjectTableMenu(id, inventory, this, this.data);
  }

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return lazyItemHandler.cast();
    }
    
    return super.getCapability(cap, side);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
  }

  @Override
  protected void saveAdditional(CompoundTag nbt) {
    nbt.put("inventory", itemHandler.serializeNBT());
    super.saveAdditional(nbt);
  }

  @Override
  public void load(CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("inventory"));
  }

  public void drops() {
    SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
    inventory.setItem(0, itemHandler.getStackInSlot(0));

    Containers.dropContents(this.level, this.worldPosition, inventory);
  }

  public static void tick(Level level, BlockPos pos, BlockState state, ProjectTableBlockEntity entity) {
    if (level.isClientSide()) {
      return;
    } 
  }
}
