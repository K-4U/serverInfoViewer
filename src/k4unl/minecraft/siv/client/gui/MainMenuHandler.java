package k4unl.minecraft.siv.client.gui;

import k4unl.minecraft.siv.lib.EnumValues;
import k4unl.minecraft.siv.network.Query;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class MainMenuHandler {
    public static final int BUTTON_ID = 404;

    public static void initGui(GuiScreen gui, List<GuiButton> buttonList) {
        for (GuiButton button : buttonList) {
            if (button instanceof GuiButtonRefresh) {
                return;
            }
        }
        GuiButtonRefresh button = new GuiButtonRefresh(BUTTON_ID, gui.width / 2 - 124, gui.height / 4 + 96);
        buttonList.add(button);
    }

    public static void onActionPerformed(GuiButton gui) {
        if (gui.id == BUTTON_ID) {
            //Get info from server.
            Query query = new Query(new InetSocketAddress("localhost", 25565));
            try {
                query.requestExtendedInfo(EnumValues.DAYNIGHT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
