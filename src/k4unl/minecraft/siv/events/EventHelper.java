package k4unl.minecraft.siv.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.siv.client.gui.MainMenuHandler;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class EventHelper {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHelper());
        FMLCommonHandler.instance().bus().register(new EventHelper());
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent evt) {
        if (evt.gui instanceof GuiMainMenu) {
            MainMenuHandler.initGui(evt.gui, evt.buttonList);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent evt) {
        if (evt.gui instanceof GuiMainMenu) {
            MainMenuHandler.onActionPerformed(evt.button);
        }
    }
}

