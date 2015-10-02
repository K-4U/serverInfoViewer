package k4unl.minecraft.siv.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiButtonRefresh extends GuiButton {

    public GuiButtonRefresh(int id, int x, int y) {
        super(id, x, y, 20, 20, "Check");
        visible = true;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            //Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.GUI_BUTTON_UPDATE);
            /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int k = 0;
            if (flag)
            {
                k += this.height;
            }
            Gui.func_146110_a(this.xPosition, this.yPosition, 0, k, this.width, this.height, 20, 40);*/
            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.displayString, this.xPosition + this.width / 2 + 8, this.yPosition + this.height / 2 + 3, 0xFFFFFF);
        }
    }
}
