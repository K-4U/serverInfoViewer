package k4unl.minecraft.siv.client.gui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cpw.mods.fml.client.FMLClientHandler;
import k4unl.minecraft.siv.client.IconSupplier;
import k4unl.minecraft.siv.lib.ExtendedServerData;
import k4unl.minecraft.siv.lib.Log;
import k4unl.minecraft.siv.lib.QueryGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
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
            if(!extendedServerData.isHasData()) {
                //Do query
                ServerAddress serveraddress = ServerAddress.func_78860_a(ServerListEntryNormal.this.field_148301_e.serverIP);
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

    public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
        if (!this.field_148301_e.field_78841_f)
        {
            this.field_148301_e.field_78841_f = true;
            this.field_148301_e.pingToServer = -2L;
            this.field_148301_e.serverMOTD = "";
            this.field_148301_e.populationInfo = "";
            field_148302_b.submit(new Runnable()
            {
                private static final String __OBFID = "CL_00000818";
                public void run()
                {
                    try
                    {
                        ServerListEntryNormal.this.field_148303_c.func_146789_i().func_147224_a(ServerListEntryNormal.this.field_148301_e);
                        threadPoolExecutor.submit(serverQueryRequester);
                    }
                    catch (UnknownHostException unknownhostexception)
                    {
                        ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
                        ServerListEntryNormal.this.field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                    }
                    catch (Exception exception)
                    {
                        ServerListEntryNormal.this.field_148301_e.pingToServer = -1L;
                        ServerListEntryNormal.this.field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                    }
                }
            });
        }

        boolean flag1 = this.field_148301_e.field_82821_f > 5;
        boolean flag2 = this.field_148301_e.field_82821_f < 5;
        boolean flag3 = flag1 || flag2;

        if(this.extendedServerData.isHasData()){
            int textWidth = this.field_148300_d.fontRenderer.getStringWidth(this.extendedServerData.isDay() ? "Day":"Night");
            //this.field_148300_d.fontRenderer.drawString(this.extendedServerData.isDay() ? "Day" : "Night", p_148279_2_ + p_148279_4_ - textWidth - 6, p_148279_3_ + p_148279_5_ - this.field_148300_d.fontRenderer.FONT_HEIGHT, 16777215);

            if(this.extendedServerData.isDay()) {
                this.field_148300_d.getTextureManager().bindTexture(IconSupplier.sun);
            }else{
                this.field_148300_d.getTextureManager().bindTexture(IconSupplier.moon);
            }
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Gui.func_146110_a(p_148279_2_ + p_148279_4_ - 15, p_148279_3_ + 22, 0.0F, 0.0F, 10, 10, 10.0F, 10.0F);

            if(this.extendedServerData.getWeather().equals("rain") || this.extendedServerData.getWeather().equals("thunder")){
                if(this.extendedServerData.getWeather().equals("rain")) {
                    this.field_148300_d.getTextureManager().bindTexture(IconSupplier.rain);
                }else{
                    this.field_148300_d.getTextureManager().bindTexture(IconSupplier.thunder);
                }
                Gui.func_146110_a(p_148279_2_ + p_148279_4_ - 28, p_148279_3_ + 22, 0.0F, 0.0F, 10, 10, 10.0F, 10.0F);

            }
        }

        this.field_148300_d.fontRenderer.drawString(this.field_148301_e.serverName, p_148279_2_ + 32 + 3, p_148279_3_ + 1, 16777215);
        List list = this.field_148300_d.fontRenderer.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.field_148301_e.serverMOTD), p_148279_4_ - 48 - 2);

        for (int l1 = 0; l1 < Math.min(list.size(), 2); ++l1)
        {
            this.field_148300_d.fontRenderer.drawString((String)list.get(l1), p_148279_2_ + 32 + 3, p_148279_3_ + 12 + this.field_148300_d.fontRenderer.FONT_HEIGHT * l1, 8421504);
        }

        String s2 = flag3 ? EnumChatFormatting.DARK_RED + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo;
        int i2 = this.field_148300_d.fontRenderer.getStringWidth(s2);
        this.field_148300_d.fontRenderer.drawString(s2, p_148279_2_ + p_148279_4_ - i2 - 15 - 2, p_148279_3_ + 1, 8421504);
        byte b0 = 0;
        String s = null;
        int j2;
        String s1;

        if (flag3)
        {
            j2 = 5;
            s1 = flag1 ? "Client out of date!" : "Server out of date!";
            s = this.field_148301_e.field_147412_i;
        }
        else if (this.field_148301_e.field_78841_f && this.field_148301_e.pingToServer != -2L)
        {
            if (this.field_148301_e.pingToServer < 0L)
            {
                j2 = 5;
            }
            else if (this.field_148301_e.pingToServer < 150L)
            {
                j2 = 0;
            }
            else if (this.field_148301_e.pingToServer < 300L)
            {
                j2 = 1;
            }
            else if (this.field_148301_e.pingToServer < 600L)
            {
                j2 = 2;
            }
            else if (this.field_148301_e.pingToServer < 1000L)
            {
                j2 = 3;
            }
            else
            {
                j2 = 4;
            }

            if (this.field_148301_e.pingToServer < 0L)
            {
                s1 = "(no connection)";
            }
            else
            {
                s1 = this.field_148301_e.pingToServer + "ms";
                s = this.field_148301_e.field_147412_i;
            }
        }
        else
        {
            b0 = 1;
            j2 = (int)(Minecraft.getSystemTime() / 100L + (long)(p_148279_1_ * 2) & 7L);

            if (j2 > 4)
            {
                j2 = 8 - j2;
            }

            s1 = "Pinging...";
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_148300_d.getTextureManager().bindTexture(Gui.icons);
        Gui.func_146110_a(p_148279_2_ + p_148279_4_ - 15, p_148279_3_, (float)(b0 * 10), (float)(176 + j2 * 8), 10, 8, 256.0F, 256.0F);

        if (this.field_148301_e.getBase64EncodedIconData() != null && !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g))
        {
            this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
            this.func_148297_b();
            this.field_148303_c.func_146795_p().saveServerList();
        }

        if (this.field_148305_h != null)
        {
            this.field_148300_d.getTextureManager().bindTexture(this.field_148306_i);
            Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        }




        int k2 = p_148279_7_ - p_148279_2_;
        int l2 = p_148279_8_ - (p_148279_3_-4);

        String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this, this.field_148301_e, p_148279_2_, p_148279_4_, p_148279_3_-4, k2, l2);
        l2 = p_148279_8_ - p_148279_3_;
        if (tooltip != null)
        {
            this.field_148303_c.func_146793_a(tooltip);
        } else
        if (k2 >= p_148279_4_ - 15 && k2 <= p_148279_4_ - 5 && l2 >= 0 && l2 <= 8)
        {
            this.field_148303_c.func_146793_a(s1);
        }
        else if (k2 >= p_148279_4_ - i2 - 15 - 2 && k2 <= p_148279_4_ - 15 - 2 && l2 >= 0 && l2 <= 8)
        {
            this.field_148303_c.func_146793_a(s);
        }
    }
}
