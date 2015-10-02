package k4unl.minecraft.siv;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import k4unl.minecraft.siv.events.EventHelper;
import k4unl.minecraft.siv.lib.Log;
import k4unl.minecraft.siv.lib.config.ModInfo;
import k4unl.minecraft.siv.lib.config.SIVConfig;
import k4unl.minecraft.siv.proxy.CommonProxy;

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
    @Instance(value = ModInfo.ID)
    public static ServerInfoViewer instance;

    private ConfigHandler SIVConfigHandler = new ConfigHandler();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Log.init();

        if (event.getSide().equals(cpw.mods.fml.relauncher.Side.CLIENT)) {
            SIVConfig.INSTANCE.init();
            SIVConfigHandler.init(SIVConfig.INSTANCE, event.getSuggestedConfigurationFile());
        } else {
            canWork = false;
            Log.error("SQE IS A CLIENT ONLY MOD! IT WILL NOT WORK ON SERVERS!");
        }
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        EventHelper.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
