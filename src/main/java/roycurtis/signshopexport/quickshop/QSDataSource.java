package roycurtis.signshopexport.quickshop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.Shop.Shop;
import org.maxgamer.quickshop.Shop.ShopManager;
import roycurtis.signshopexport.DataSource;
import roycurtis.signshopexport.json.Record;

import java.util.ArrayList;
import java.util.Iterator;

public class QSDataSource implements DataSource
{
    private ArrayList<Shop> shops;

    public int prepare()
    {
        ShopManager    manager  = QuickShop.instance.getShopManager();
        Iterator<Shop> iterator = manager.getShopIterator();

        shops = new ArrayList<>();
        while ( iterator.hasNext() )
            shops.add( iterator.next() );

        return shops.size();
    }

    public Record createRecordForIndex(int idx)
    {
        Shop          shop   = shops.get(idx);
        Record        rec    = new Record();
        OfflinePlayer player = Bukkit.getOfflinePlayer(shop.getOwner());
        Location      loc    = shop.getLocation();
        Sign          state  = shop.getSigns().get(0);

        rec.locWorld = loc.getWorld().getName();
        rec.locX     = loc.getBlockX();
        rec.locY     = loc.getBlockY();
        rec.locZ     = loc.getBlockZ();
        rec.id       = rec.hashCode();

        rec.ownerName = player.getName();
        rec.ownerUuid = player.getUniqueId().toString();

        rec.signType  = shop.isSelling() ? "buy" : "sell";
        rec.signPrice = shop.getPrice();
        rec.signText  = new String[] { state.getLine(1), state.getLine(2) };

        // This is necessary as the event's items are sometimes CraftItemStack type. That type does
        // not populate its private fields, thus it is useless to Gson's serializer.
        ItemStack oldStack = shop.getItem();
        ItemStack newStack = new ItemStack(
            oldStack.getType(), oldStack.getAmount(), oldStack.getDurability());

        newStack.setData(oldStack.getData());
        newStack.setItemMeta(oldStack.getItemMeta());

        rec.invItems = new ItemStack[] { newStack };

        if ( shop.isSelling() )
            rec.invInStock = shop.getRemainingStock() > 0;
        else
            rec.invInStock = shop.getRemainingSpace() > 0;

        return rec;
    }

    public void free()
    {
        if (shops != null) shops.clear();

        shops = null;
    }
}
