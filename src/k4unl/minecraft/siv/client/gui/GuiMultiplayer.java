package k4unl.minecraft.siv.client.gui;

import net.minecraft.client.gui.GuiScreen;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiMultiplayer extends net.minecraft.client.gui.GuiMultiplayer {

    public GuiMultiplayer(GuiScreen p_i1040_1_) {
        super(p_i1040_1_);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        super.initGui();
        ServerSelectionList list = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
        this.field_146803_h = list;
        list.func_148195_a(this.field_146804_i);
    }

}
