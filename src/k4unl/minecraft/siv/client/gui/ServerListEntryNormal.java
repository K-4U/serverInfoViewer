package k4unl.minecraft.siv.client.gui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import k4unl.minecraft.siv.client.IconSupplier;
import k4unl.minecraft.siv.lib.ExtendedServerData;
import k4unl.minecraft.siv.lib.Log;
import k4unl.minecraft.siv.lib.QueryGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Koen Beckers (K-4U)
 */
public class ServerListEntryNormal extends net.minecraft.client.gui.ServerListEntryNormal {

    public static final ThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Query Checker #%d").setDaemon(true).build());

    private QueryGetter queryGetter;
    public ExtendedServerData extendedServerData = new ExtendedServerData();

    private Runnable serverQueryRequester = new Runnable() {
        @Override
        public void run() {
            if (!extendedServerData.isHasData()) {
                //Do query
                ServerAddress serveraddress = ServerAddress.fromString(ServerListEntryNormal.this.server.serverIP);
                queryGetter = new QueryGetter(serveraddress, extendedServerData);
                Log.debug("Getting");
                queryGetter.run();
                Log.debug("Done with getting");
                threadPoolExecutor.remove(this);
            }
        }
    };

    protected ServerListEntryNormal(net.minecraft.client.gui.GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_) {
        super(p_i45048_1_, p_i45048_2_);
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        if (!this.server.pinged) {
            this.server.pinged = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            EXECUTOR.submit(new Runnable() {
                public void run() {
                    try {
                        ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
                        threadPoolExecutor.submit(serverQueryRequester);
                    } catch (UnknownHostException unknownhostexception) {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + "Can\'t resolve hostname";
                    } catch (Exception exception) {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + "Can\'t connect to server.";
                    }
                }
            });
        }
        boolean flag = this.server.version > 210;
        boolean flag1 = this.server.version < 210;
        boolean flag2 = flag || flag1;

        /** CUSTOM **/
        if(this.extendedServerData.isHasData()){
            int textWidth = this.mc.fontRendererObj.getStringWidth(this.extendedServerData.isDay() ? "Day":"Night");
            //this.field_148300_d.fontRenderer.drawString(this.extendedServerData.isDay() ? "Day" : "Night", p_148279_2_ + listWidth - textWidth - 6, y + slotHeight - this.field_148300_d.fontRenderer.FONT_HEIGHT, 16777215);

            if(this.extendedServerData.isDay()) {
                this.mc.getTextureManager().bindTexture(IconSupplier.sun);
            }else{
                this.mc.getTextureManager().bindTexture(IconSupplier.moon);
            }
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y + 22, 0.0F, 0.0F, 10, 10, 10.0F, 10.0F);

            if(this.extendedServerData.getWeather().equals("rain") || this.extendedServerData.getWeather().equals("thunder")){
                if(this.extendedServerData.getWeather().equals("rain")) {
                    this.mc.getTextureManager().bindTexture(IconSupplier.rain);
                }else{
                    this.mc.getTextureManager().bindTexture(IconSupplier.thunder);
                }
                Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 28, y + 22, 0.0F, 0.0F, 10, 10, 10.0F, 10.0F);

            }
        }


        this.mc.fontRendererObj.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
        List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.server.serverMOTD), listWidth - 48 - 2);

        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            this.mc.fontRendererObj.drawString((String) list.get(i), x + 32 + 3, y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 8421504);
        }

        String s2 = flag2 ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo;
        int j = this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
        int k = 0;
        String s = null;
        int l;
        String s1;

        if (flag2) {
            l = 5;
            s1 = flag ? "Client out of date!" : "Server out of date!";
            s = this.server.playerList;
        } else if (this.server.pinged && this.server.pingToServer != -2L) {
            if (this.server.pingToServer < 0L) {
                l = 5;
            } else if (this.server.pingToServer < 150L) {
                l = 0;
            } else if (this.server.pingToServer < 300L) {
                l = 1;
            } else if (this.server.pingToServer < 600L) {
                l = 2;
            } else if (this.server.pingToServer < 1000L) {
                l = 3;
            } else {
                l = 4;
            }

            if (this.server.pingToServer < 0L) {
                s1 = "(no connection)";
            } else {
                s1 = this.server.pingToServer + "ms";
                s = this.server.playerList;
            }
        } else {
            k = 1;
            l = (int) (Minecraft.getSystemTime() / 100L + (long) (slotIndex * 2) & 7L);

            if (l > 4) {
                l = 8 - l;
            }

            s1 = "Pinging...";
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, (float) (k * 10), (float) (176 + l * 8), 10, 8, 256.0F, 256.0F);

        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64)) {
            this.lastIconB64 = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }

        if (this.icon != null) {
            this.drawTextureAt(x, y, this.serverIcon);
        } else {
            this.drawTextureAt(x, y, UNKNOWN_SERVER);
        }

        int i1 = mouseX - x;
        int j1 = mouseY - (y - 4);

        String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this, this.server, x, listWidth, y-4, i1, j1);
        if (tooltip != null) {
            this.owner.setHoveringText(tooltip);
        } else if (i1 >= listWidth - 15 && i1 <= listWidth - 5 && j1 >= 0 && j1 <= 8) {
            this.owner.setHoveringText(s1);
        } else if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8) {
            this.owner.setHoveringText(s);
        }

        if (this.mc.gameSettings.touchscreen || isSelected) {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int k1 = mouseX - x;
            int l1 = mouseY - y;

            if (this.canJoin()) {
                if (k1 < 32 && k1 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.owner.canMoveUp(this, slotIndex)) {
                if (k1 < 16 && l1 < 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.owner.canMoveDown(this, slotIndex)) {
                if (k1 < 16 && l1 > 16) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
        }
    }
}
