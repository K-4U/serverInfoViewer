package k4unl.minecraft.siv.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerList;

/**
 * @author Koen Beckers (K-4U)
 */
public class ServerSelectionList extends net.minecraft.client.gui.ServerSelectionList {

    public ServerSelectionList(GuiMultiplayer ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(ownerIn, mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public IGuiListEntry getListEntry(int index) {
        if (index < this.serverListInternet.size()) {
            return (IGuiListEntry) this.serverListInternet.get(index);
        } else {
            index = index - this.serverListInternet.size();

            if (index == 0) {
                return this.lanScanEntry;
            } else {
                --index;
                return (IGuiListEntry) this.serverListLan.get(index);
            }
        }
    }

    public void updateOnlineServers(ServerList p_148195_1_) {
        this.serverListInternet.clear();

        for (int i = 0; i < p_148195_1_.countServers(); ++i) {
            this.serverListInternet.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
        }
    }

}
