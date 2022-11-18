package pl.amon.tinymemory.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;
import pl.amon.tinymemory.TinyMemory;

import com.dannyandson.tinyredstone.blocks.PanelTile;
import com.dannyandson.tinyredstone.network.PanelCellSync;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;

public class ModNetworkHandler {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = "1.2";

    private static int nextID() {
      return ID++;
    }

    public static void registerMessages() {
      INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(TinyMemory.MODID, "tinyredstone"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
      );

      INSTANCE.messageBuilder(PanelCellSync.class,nextID())
        .encoder(PanelCellSync::toBytes)
        .decoder(PanelCellSync::new)
        .consumer(PanelCellSync::handle)
        .add();
    }

    public static void sendToClient(Object packet, PanelTile panelTile) {
      BlockPos pos = panelTile.getBlockPos();
      for (Player player : panelTile.getLevel().players()) {
        if (player instanceof ServerPlayer && player.distanceToSqr(pos.getX(),pos.getY(),pos.getZ()) < 64d) {
          INSTANCE.sendTo(packet, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
      }
    }

    public static void sendToServer(Object packet) {
      INSTANCE.sendToServer(packet);
    }

}