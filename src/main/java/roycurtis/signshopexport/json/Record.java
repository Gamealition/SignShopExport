package roycurtis.signshopexport.json;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.wargamer2010.signshop.Seller;
import org.wargamer2010.signshop.util.economyUtil;
import org.wargamer2010.signshop.util.itemUtil;

/** Represents a JSON record for a SignShop, for serialization */
public class Record
{
    // Location
    public String locWorld;
    public int    locX;
    public int    locY;
    public int    locZ;

    // Owner
    public String ownerName;
    public String ownerUuid;

    // Sign data
    public String[] signText;
    public String   signType;
    public double   signPrice;

    // Inventory data
    public ItemStack[] invItems;
    public boolean     invInStock;

    /** Creates a record from a SignShop "seller", using SignShop's utility functions for data */
    public static Record fromSeller(Seller sign)
    {
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
}
