package pl.amon.tinymemory.setup;

import java.util.function.Supplier;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import pl.amon.tinymemory.TinyMemory;
import pl.amon.tinymemory.block.MemoryAssembler;
import pl.amon.tinymemory.block.Plate;
import pl.amon.tinymemory.block.ProjectTable;
import pl.amon.tinymemory.block.entity.ProjectTableBlockEntity;
import pl.amon.tinymemory.block.entity.ROM;
import pl.amon.tinymemory.items.MemoryBlueprint;
import pl.amon.tinymemory.items.ToolTippedBlockItem;
import pl.amon.tinymemory.screen.ProjectTableMenu;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
  //Registers
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TinyMemory.MODID);
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TinyMemory.MODID);
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TinyMemory.MODID);
  public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, TinyMemory.MODID);

  //Items
  public static final RegistryObject<Item> MEMORY_BLUEPRINT_ITEM  = ITEMS.register("memory_blueprint", MemoryBlueprint::new);

  //Blocks
  public static final RegistryObject<Block> ROM_BLOCK = BLOCKS.register("rom", () -> new Plate<ROM>(ROM::new, ROM::getTicker));
  public static final RegistryObject<Block> MEMORY_ASSEMBLER_BLOCK = BLOCKS.register("memory_assembler", MemoryAssembler::new);
  public static final RegistryObject<Block> PROJECT_TABLE_BLOCK = BLOCKS.register("project_table", ProjectTable::new);
  
  //Block Entities
  public static final RegistryObject<BlockEntityType<ROM>> ROM_BLOCK_ENTITY = BLOCK_ENTITIES.register("rom", () -> BlockEntityType.Builder.of(ROM::new, ROM_BLOCK.get()).build(null));
  public static final RegistryObject<BlockEntityType<ProjectTableBlockEntity>> PROJECT_TABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("project_table", () -> BlockEntityType.Builder.of(ProjectTableBlockEntity::new, PROJECT_TABLE_BLOCK.get()).build(null));

  //Menus
  public static final RegistryObject<MenuType<ProjectTableMenu>> PROJECT_TABLE_MENU = MENUS.register("project_table", () -> IForgeMenuType.create(ProjectTableMenu::new));


  public static void register() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  public static void registerPanelCells(){
    //TinyRedstone.registerPanelCell(ROM.class, TINY_ROM.get());
  }

  //Register an item for all blocks
  @SubscribeEvent
  public static void onRegisterItems(final RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
      BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
        Block block = blockRegistryObject.get();
        Item.Properties properties = new Item.Properties().tab(ModSetup.ITEM_GROUP);
        Supplier<Item> blockItemFactory = () -> new ToolTippedBlockItem(block, blockRegistryObject.getId(), properties);
        event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
      });
    }
  }
}
