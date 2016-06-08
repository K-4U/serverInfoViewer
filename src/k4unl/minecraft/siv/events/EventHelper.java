package k4unl.minecraft.siv.events;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHelper {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHelper());
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent evt) {
        if(evt.getGui() instanceof GuiMultiplayer){
            evt.setGui(new k4unl.minecraft.siv.client.gui.GuiMultiplayer(((GuiMultiplayer)evt.getGui()).parentScreen));
        }
    }
}

