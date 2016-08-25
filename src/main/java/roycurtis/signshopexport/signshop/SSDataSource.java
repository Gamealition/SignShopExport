package roycurtis.signshopexport.signshop;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.wargamer2010.signshop.Seller;
import org.wargamer2010.signshop.configuration.Storage;
import org.wargamer2010.signshop.util.economyUtil;
import org.wargamer2010.signshop.util.itemUtil;
import roycurtis.signshopexport.DataSource;
import roycurtis.signshopexport.json.Record;

public class SSDataSource implements DataSource
{
    private Seller[] signs;

    public int prepare()
    {
        int total = Storage.get().shopCount();

        signs = Storage.get().getSellers().toArray(new Seller[total]);

        return total;
    }

    /** Creates a record from a SignShop "seller", using SignShop's utility functions for data */
    public Record createRecordForIndex(int idx)
    {
        Seller   seller = signs[idx];
        Record   rec    = new Record();
        Location loc    = seller.getSignLocation();
        Sign     sign   = (Sign) seller.getSign().getState();

        rec.locWorld = loc.getWorld().getName();
        rec.locX     = loc.getBlockX();
        rec.locY     = loc.getBlockY();
        rec.locZ     = loc.getBlockZ();

        rec.ownerName = seller.getOwner().getName();
        rec.ownerUuid = seller.getOwner().GetIdentifier().getStringIdentifier();

        rec.signType  = seller.getOperation();
        rec.signPrice = economyUtil.parsePrice(sign.getLine(3));
        rec.signText  = new String[] { sign.getLine(1), sign.getLine(2) };

        rec.invItems = seller.getItems();

        // This is necessary as the event's items are sometimes CraftItemStack type. That type does
        // not populate its private fields, thus it is useless to Gson's serializer.
        ItemStack[] plainStack = new ItemStack[rec.invItems.length];
        for (int i = 0; i < rec.invItems.length; i++)
        {
            ItemStack oldItem = rec.invItems[i];

            plainStack[i] = new ItemStack(
                oldItem.getType(), oldItem.getAmount(), oldItem.getDurability());

            plainStack[i].setData(oldItem.getData());
            plainStack[i].setItemMeta(oldItem.getItemMeta());
        }

        rec.invItems = plainStack;

        switch(rec.signType)
        {
            case "buy":
            case "ibuy":
                rec.invInStock = checkStock(seller, true, 0);
                break;
            case "sell":
            case "isell":
                rec.invInStock = checkStock(seller, false, rec.signPrice);
                break;
            default:
                rec.invInStock = true;
                break;
        }

        return rec;
    }

    private boolean checkStock(Seller seller, boolean take, double price)
    {
        // Check balance of shop owner if sell sign
        if ( !take && !seller.getOwner().hasMoney(price) )
            return false;

        // For both signs, check stock space/availability
        return itemUtil.stockOKForContainables(seller.getContainables(), seller.getItems(), take);
    }

    public void free()
    {
        signs = null;
    }
}
