package se.sst_55t.betterthanelectricity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import se.sst_55t.betterthanelectricity.item.IChargeable;
import se.sst_55t.betterthanelectricity.item.ModItems;

/**
 * @author shadowfacts
 */
public class PacketToServerJetpack implements IMessage {

  private int chestCharge;

  public PacketToServerJetpack(int chestCharge) {
    this.chestCharge = chestCharge;
  }

  public PacketToServerJetpack() {
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(chestCharge);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    chestCharge = buf.readInt();
  }

  public static class Handler implements IMessageHandler<PacketToServerJetpack, IMessage> {

    @Override
    public IMessage onMessage(PacketToServerJetpack message, MessageContext ctx) {
      EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
      int chestChargeClient = message.chestCharge;
      ItemStack chestStackServer = serverPlayer.inventory.armorInventory.get(2);
      if (!chestStackServer.isEmpty() && chestStackServer.getItem() == ModItems.jetpack)
      {
        serverPlayer.getServerWorld().addScheduledTask(() -> {
          ((IChargeable) chestStackServer.getItem()).setCharge(chestChargeClient, chestStackServer);
        });
      }
      return null;
    }
  }
}