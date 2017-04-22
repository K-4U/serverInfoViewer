package k4unl.minecraft.siv;

import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.siv.events.EventHelper;
import k4unl.minecraft.siv.lib.Log;
import k4unl.minecraft.siv.lib.config.ModInfo;
import k4unl.minecraft.siv.lib.config.SIVConfig;
import k4unl.minecraft.siv.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
        modid = ModInfo.ID,
        name = ModInfo.NAME,
        version = ModInfo.VERSION,
        acceptableRemoteVersions = "*"
)

public class ServerInfoViewer {

    @SidedProxy(
            clientSide = "k4unl.minecraft.siv.proxy.ClientProxy",
            serverSide = "k4unl.minecraft.siv.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    private boolean canWork = true;
    @Mod.Instance(value = ModInfo.ID)
    public static ServerInfoViewer instance;

    private ConfigHandler SIVConfigHandler = new ConfigHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Log.init();

        if (event.getSide().equals(Side.CLIENT)) {
            SIVConfig.INSTANCE.init();
            SIVConfigHandler.init(SIVConfig.INSTANCE, event.getSuggestedConfigurationFile());
        } else {
            canWork = false;
            Log.error("SIV IS A CLIENT ONLY MOD! IT WILL NOT WORK ON SERVERS!");
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        Log.debug("Initializing event helper");
        EventHelper.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
