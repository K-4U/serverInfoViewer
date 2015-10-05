package k4unl.minecraft.siv.client.gui;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;

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
        Field serverSelectionField = ReflectionHelper.findField(net.minecraft.client.gui.GuiMultiplayer.class, "field_146803_h");
        try {
            ServerSelectionList list = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            serverSelectionField.set(this, list);
            list.func_148195_a(this.field_146804_i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
