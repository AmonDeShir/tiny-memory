package pl.amon.tinymemory.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.amon.tinymemory.block.entity.ProjectTableBlockEntity;
import pl.amon.tinymemory.screen.components.BlueprintSlot;
import pl.amon.tinymemory.screen.components.IOPort;
import pl.amon.tinymemory.setup.Registration;

public class ProjectTableMenu extends AbstractContainerMenu {
  public final ProjectTableBlockEntity blockEntity;
  public final IOPort[] ports = new IOPort[32];
  private final Level level;
  private final ContainerData data;


  public ProjectTableMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
    this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
  }

  public ProjectTableMenu(int id, Inventory inv, BlockEntity entity) {
    super(Registration.PROJECT_TABLE_MENU.get(), id);
    
    checkContainerSize(inv, 1);
    blockEntity = (ProjectTableBlockEntity) entity;

    this.level = inv.player.level;
    this.data = new SimpleContainerData(0);

    addPlayerInventory(inv);
    addPlayerHotbar(inv);

    this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
      this.addSlot(new BlueprintSlot(handler, 0, 17, 17));
    });

    addDataSlots(data);

    for (int i = 0; i < ports.length; i++) {
      final int index = i;

      ports[i] = new IOPort(i);
      ports[i].value = blockEntity.getPortValue(i);
      ports[i].onUpdate = (int value) -> blockEntity.setPortValue(index, value);
    }
  }

  // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
  // must assign a slot number to each of the slots used by the GUI.
  // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
  // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
  //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
  //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
  //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
  private static final int HOTBAR_SLOT_COUNT = 9;
  private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
  private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
  private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
  private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
  private static final int VANILLA_FIRST_SLOT_INDEX = 0;
  private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must be the number of slots you have!

  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    Slot sourceSlot = slots.get(index);
    if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
    ItemStack sourceStack = sourceSlot.getItem();
    ItemStack copyOfSourceStack = sourceStack.copy();

    if (!sourceSlot.getItem().is(Registration.MEMORY_BLUEPRINT_ITEM.get())) {
      return ItemStack.EMPTY;
    }

    // Check if the slot clicked is one of the vanilla container slots
    if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
      // This is a vanilla container slot so merge the stack into the tile inventory
      if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
        return ItemStack.EMPTY;  // EMPTY_ITEM
      }
    } 
    else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
      // This is a TE slot so merge the stack into the players inventory
      if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
        return ItemStack.EMPTY;
      }
    } 
    else {
      System.out.println("Invalid slotIndex:" + index);
      return ItemStack.EMPTY;
    }

    // If stack size == 0 (the entire stack was moved) set slot contents to null
    if (sourceStack.getCount() == 0) {
      sourceSlot.set(ItemStack.EMPTY);
    } 
    else {
      sourceSlot.setChanged();
    }
    
    sourceSlot.onTake(playerIn, sourceStack);
    
    return copyOfSourceStack;
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, Registration.PROJECT_TABLE_BLOCK.get());
  }

  private void addPlayerInventory(Inventory playerInventory) {
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 15 + x * 18, 137 + y * 18));
      }
    }
  }

  private void addPlayerHotbar(Inventory playerInventory) {
    for (int x = 0; x < 9; ++x) {
      this.addSlot(new Slot(playerInventory, x, 15 + x * 18, 195));
    }
  }
}
