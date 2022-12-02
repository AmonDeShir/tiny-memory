package pl.amon.tinymemory.block.entity;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.screen.ProjectTableMenu;
import pl.amon.tinymemory.setup.Registration;

public class ProjectTableBlockEntity extends BlockEntity implements MenuProvider {
  private static final String KEY_INVENTORY = "inventory";

  private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      setChanged();
    };
  };

  public static final int MAX_BITES = 6120;
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private int bites = 1;
  private int layer = 0;
  private int[] values = new int[MAX_BITES];

  public ProjectTableBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.PROJECT_TABLE_BLOCK_ENTITY.get(), pos, state);
  }


  public int getBites() {
    return this.bites;
  }
  
  public void setBites(int value) {
    this.remapData(this.bites, value);
    this.bites = value;

    setChanged();
  }

  private void remapData(int from, int to) {
    int[] remapped = new int[ProjectTableBlockEntity.MAX_BITES];
    int max = Math.max(from, to);

    Arrays.fill(remapped, 0);

    if (from == to) {
      return;
    }

    for (int i = 0; i < this.values.length; i++) {
      int n = i + Math.floorDiv(i, 32 - max);
      
      if (from > to) {
        remapped[n] = this.values[i];
      }
      else {
        remapped[i] = this.values[n];
      }
    }

    this.values = remapped;
  }

  public int getLayer() {
    return this.values[1];
  }

  public void setLayer(int value) {
    this.layer = value;
    setChanged();
  }

  public int getPortValue(int index) {
    return this.values[(32 - getBites()) * getLayer() + index];
  }

  public void setPortValue(int index, int value) {
    TinyMemory.LOGGER.info(String.format("Update port value - Menu, value: %d index: %d. The final data index: %d", value, index, (32 - getBites()) * getLayer() + index));

    this.values[(32 - getBites()) * getLayer() + index] = value;
    setChanged();
  }

  @Override
  public Component getDisplayName() {
    return Component.literal("Project Table");
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new ProjectTableMenu(id, inventory, this);
  }

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return lazyItemHandler.cast();
    }
    
    return super.getCapability(cap, side);
  }

  /* When the world loads from disk, the server needs to send the TileEntity information to the client
  //  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
  //  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
  //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
  */

  @Override
  @Nullable
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    if (this.level.isClientSide) {
      this.bites = 0;
      this.layer = 0;
      this.values = new int[MAX_BITES];
    }
    this.load(pkt.getTag());   // read from the nbt in the packet
  }

  /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client*/
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag compoundTag = new CompoundTag();
    this.saveAdditional(compoundTag);
    
    return compoundTag;
  }

  // This is where you load the data that you saved in writeToNBT
  @Override
  public void load(CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound(ProjectTableBlockEntity.KEY_INVENTORY));
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
    nbt.put(ProjectTableBlockEntity.KEY_INVENTORY, itemHandler.serializeNBT());

    super.saveAdditional(nbt);
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
