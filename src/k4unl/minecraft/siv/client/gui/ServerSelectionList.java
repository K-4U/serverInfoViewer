package k4unl.minecraft.siv.client.gui;

import k4unl.minecraft.siv.lib.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerList;

/**
 * @author Koen Beckers (K-4U)
 */
public class ServerSelectionList extends net.minecraft.client.gui.ServerSelectionList {

    public ServerSelectionList(net.minecraft.client.gui.GuiMultiplayer p_i45049_1_, Minecraft p_i45049_2_, int p_i45049_3_, int p_i45049_4_, int p_i45049_5_, int p_i45049_6_, int p_i45049_7_) {
        super(p_i45049_1_, p_i45049_2_, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
        Log.info("DUBBEL TEST");
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
        if (p_148180_1_ < this.field_148198_l.size()) {
            return (GuiListExtended.IGuiListEntry) this.field_148198_l.get(p_148180_1_);
        } else {
            p_148180_1_ -= this.field_148198_l.size();

            if (p_148180_1_ == 0) {
                return this.field_148196_n;
            } else {
                --p_148180_1_;
                return (GuiListExtended.IGuiListEntry) this.field_148199_m.get(p_148180_1_);
            }
        }
    }

    public void func_148195_a(ServerList p_148195_1_) {
        this.field_148198_l.clear();

        for (int i = 0; i < p_148195_1_.countServers(); ++i) {
            this.field_148198_l.add(new ServerListEntryNormal(this.field_148200_k, p_148195_1_.getServerData(i)));
        }
    }

}
