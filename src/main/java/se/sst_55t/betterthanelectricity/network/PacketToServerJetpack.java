package se.sst_55t.betterthanelectricity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import se.sst_55t.betterthanelectricity.item.IChargeable;

/**
 * @author shadowfacts
 */
public class PacketToServerJetpack implements IMessage {

  private ItemStack chestStackClient;

  public PacketToServerJetpack(ItemStack chestStackClient) {
    this.chestStackClient = chestStackClient;
  }

  public PacketToServerJetpack() {
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeItemStack(buf, chestStackClient);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    chestStackClient = ByteBufUtils.readItemStack(buf);
  }

  public static class Handler implements IMessageHandler<PacketToServerJetpack, IMessage> {

    @Override
    public IMessage onMessage(PacketToServerJetpack message, MessageContext ctx) {
      EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
      ItemStack chestStackClient = message.chestStackClient;
      ItemStack chestStackServer = serverPlayer.inventory.armorInventory.get(2);
      serverPlayer.getServerWorld().addScheduledTask(() -> {
        ((IChargeable)chestStackServer.getItem()).setCharge(
                ((IChargeable)chestStackClient.getItem()).getCharge(chestStackClient), chestStackServer);
      });
      return null;
    }

  }

}