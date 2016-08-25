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
        Seller   sign  = signs[idx];
        Record   rec   = new Record();
        Location loc   = sign.getSignLocation();
        Sign     state = (Sign) sign.getSign().getState();

        rec.locWorld = loc.getWorld().getName();
        rec.locX     = loc.getBlockX();
        rec.locY     = loc.getBlockY();
        rec.locZ     = loc.getBlockZ();

        rec.ownerName = sign.getOwner().getName();
        rec.ownerUuid = sign.getOwner().GetIdentifier().getStringIdentifier();

        rec.signType  = sign.getOperation();
        rec.signPrice = economyUtil.parsePrice(state.getLine(3));
        rec.signText  = new String[] { state.getLine(1), state.getLine(2) };

        rec.invItems = sign.getItems();

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
                rec.invInStock = itemUtil.stockOKForContainables(
                    sign.getContainables(), sign.getItems(), true);
                break;
            case "sell":
            case "isell":
                rec.invInStock = itemUtil.stockOKForContainables(
                    sign.getContainables(), sign.getItems(), false);
                break;
            default:
                rec.invInStock = true;
                break;
        }

        return rec;
    }

    public void free()
    {
        signs = null;
    }
}
