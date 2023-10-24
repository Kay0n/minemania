
package online.refract.minemania;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Main extends JavaPlugin implements Listener{


    Map<EntityType, Double> entityAreaMap = new HashMap<>();
    Map<Material, Double> blockAreaMap = new HashMap<>();
    Map<Location, List<ItemStack>> playerGraveMap;
    WorldBorder border;

    double borderDiameter;
    double genericBlockValue;
    double genericMobValue;
    double playerDeathPenalty;

    /*  TODO:

     - playtest for values and math
     - start/stop/set command

    */
    


    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("Starting plugin");
        this.getServer().getPluginManager().registerEvents(this, this);
        border = Bukkit.getWorlds().get(0).getWorldBorder();

        borderDiameter = border.getSize();

        genericBlockValue = 0.02;
        genericMobValue = 0.03;
        playerDeathPenalty = 7.0;
        
        entityAreaMap.put(EntityType.ZOMBIE, 0.04);
        entityAreaMap.put(EntityType.SKELETON, 0.04);
        entityAreaMap.put(EntityType.CREEPER, 0.09);
        entityAreaMap.put(EntityType.ENDERMAN, 0.5);
        entityAreaMap.put(EntityType.BLAZE, 0.5);
        entityAreaMap.put(EntityType.GHAST, 3.0);
        entityAreaMap.put(EntityType.WITCH, 0.9);
        entityAreaMap.put(EntityType.WITHER_SKELETON, 1.6);
        entityAreaMap.put(EntityType.WITHER, 30.0);
        entityAreaMap.put(EntityType.ENDER_DRAGON, 30.0);
        entityAreaMap.put(EntityType.SLIME, 0.06);
        entityAreaMap.put(EntityType.PHANTOM, 0.07);

        blockAreaMap.put(Material.COPPER_ORE, 0.06);
        blockAreaMap.put(Material.COAL_ORE, 0.07);
        blockAreaMap.put(Material.IRON_ORE, 0.08);
        blockAreaMap.put(Material.REDSTONE_ORE, 0.09);
        blockAreaMap.put(Material.LAPIS_ORE, 0.12);
        blockAreaMap.put(Material.GOLD_ORE, 0.2);
        blockAreaMap.put(Material.EMERALD, 1.5);
        blockAreaMap.put(Material.DIAMOND_ORE, 1.6);
        blockAreaMap.put(Material.ANCIENT_DEBRIS, 6.0);
        blockAreaMap.put(Material.NETHER_QUARTZ_ORE, 0.08);
        blockAreaMap.put(Material.SPAWNER, 8.0);

    }


    
    public void updateBorder(){
        for(World world : Bukkit.getWorlds()){
            world.getWorldBorder().setSize(borderDiameter, 1);
        }
    }

    
    
    
    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event){

        ItemStack toolInHand = event.getPlayer().getInventory().getItemInMainHand();
        if(toolInHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
            return;
        }

        Material blockType = event.getBlock().getType();
        Double specialBlockBonus = blockAreaMap.get(blockType);

        if(specialBlockBonus != null){
            borderDiameter += specialBlockBonus;
        }
        else {
            borderDiameter += genericBlockValue;

        }
        
        updateBorder();
        
    }

    
    
    @EventHandler
    public void onKillMob(EntityDeathEvent event){
        
        Double entityAreaIncrease = entityAreaMap.get(event.getEntityType());

        if(entityAreaIncrease != null){
            borderDiameter += entityAreaIncrease;
        }

        else {
            borderDiameter += genericMobValue;
        }
        
        updateBorder();

    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        
        borderDiameter -= playerDeathPenalty;
        if(borderDiameter < 5){
            borderDiameter = 5;
        }

        updateBorder();


    }

    

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length < 1){
            return false;
        }

        if(args[0].equalsIgnoreCase("query")){
            sender.sendMessage("The current border diameter is " + (int) borderDiameter + " blocks");
            return true;
        }

        return false;

    }





    // public Location getEmptyNearbyBlock(Location center){
        
    //     int radius = 3;

    //     for (int x = -radius; x <= radius; x++) {
    //         for (int y = -radius; y <= radius; y++) {
    //             for (int z = -radius; z <= radius; z++) {

    //                 Block loopBlock = center.clone().add(x, y, z).getBlock();

    //                 if(Tag.REPLACEABLE.isTagged(loopBlock.getType())){
    //                     return loopBlock.getLocation();
    //                 }
    //             }
    //         }
    //     }

    //     return center;
    // }
    
    // @EventHandler
    // public void onClickWithStick(PlayerInteractEvent event){
    //     if(event.getItem().getType() != Material.STICK){
    //         return;
    //     }
    //     if(event.getClickedBlock().getType() != Material.COBBLESTONE){
    //         return;
    //     }

    //     Block targetBlock = event.getClickedBlock();

    //     targetBlock.setType(Material.PLAYER_HEAD);
    //     Skull skull = (Skull) targetBlock.getState();
    //     skull.setOwningPlayer(event.getPlayer());
    //     skull.update(true);

    //     Rotatable rotatable = (Rotatable) targetBlock.getBlockData();
    //     rotatable.setRotation(BlockFace.NORTH_EAST);
    //     targetBlock.setBlockData(rotatable);

    // }

}
