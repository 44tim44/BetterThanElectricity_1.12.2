package se.sst_55t.betterthanelectricity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
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

    public static final int JUMP_BUTTON = 0;
    public static final int FORWARD_BUTTON = 1;
    public static final int FORWARD_AND_SPRINT_BUTTONS = 2;

    private int buttonPressed;
    private String playerName;

    public PacketToServerJetpack(int buttonPressed, String playerName) {
        this.buttonPressed = buttonPressed;
        this.playerName = playerName;
    }

    public PacketToServerJetpack() {
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(buttonPressed);
        ByteBufUtils.writeUTF8String(buf,playerName);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        buttonPressed = buf.readInt();
        playerName = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<PacketToServerJetpack, IMessage> {

        @Override
        public IMessage onMessage(PacketToServerJetpack message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            int buttonPressed = message.buttonPressed;
            String playerName = message.playerName;

            serverPlayer.getServerWorld().addScheduledTask(() -> {

                EntityPlayer jetpackPlayer = serverPlayer.getServerWorld().getPlayerEntityByName(playerName);
                ItemStack chestStackServer = jetpackPlayer.inventory.armorInventory.get(2);
                if (!chestStackServer.isEmpty() && chestStackServer.getItem() == ModItems.jetpack)
                {
                    if (((IChargeable) chestStackServer.getItem()).getCharge(chestStackServer) > 0)
                    {
                        switch (buttonPressed)
                        {
                            case (JUMP_BUTTON):
                                ((IChargeable) chestStackServer.getItem()).decreaseCharge(chestStackServer);
                                break;
                            case (FORWARD_BUTTON):
                                ((IChargeable) chestStackServer.getItem()).decreaseCharge(chestStackServer);
                                break;
                            case (FORWARD_AND_SPRINT_BUTTONS):
                                ((IChargeable) chestStackServer.getItem()).decreaseCharge(chestStackServer);
                                break;
                        }

                    }
                }
            });
            return null;
        }
    }
}