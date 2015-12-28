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
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        if (index < this.field_148198_l.size()) {
            return (GuiListExtended.IGuiListEntry) this.field_148198_l.get(index);
        } else {
            index = index - this.field_148198_l.size();

            if (index == 0) {
                return this.lanScanEntry;
            } else {
                --index;
                return (GuiListExtended.IGuiListEntry) this.field_148199_m.get(index);
            }
        }
    }


    public void func_148195_a(ServerList p_148195_1_) {
        this.field_148198_l.clear();

        for (int i = 0; i < p_148195_1_.countServers(); ++i) {
            this.field_148198_l.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
        }
    }

}
