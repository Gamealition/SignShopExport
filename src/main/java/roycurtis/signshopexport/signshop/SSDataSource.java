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

import java.util.HashMap;

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
        rec.id       = rec.hashCode();

        rec.ownerName = seller.getOwner().getName();
        rec.ownerUuid = seller.getOwner().GetIdentifier().getStringIdentifier();

        rec.signType  = seller.getOperation();
        rec.signPrice = economyUtil.parsePrice(sign.getLine(3));
        rec.signText  = new String[] { sign.getLine(1), sign.getLine(2) };

        rec.invItems = processStacks( seller.getItems() );

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

    /**
     * Processes a sign's item stacks by:
     * * Properly filling in all their private fields
     * * Combining stacks of same type into one, with proper quantity
     * @param stacks ItemStacks to process
     * @return Processed array of ItemStacks, or null if given null stacks
     */
    private ItemStack[] processStacks(ItemStack[] stacks)
    {
        if (stacks == null)
            return null;

        // Copied from SignShop's itemUtil.itemStackToString()
        HashMap<ItemStack, Integer> items = new HashMap<>(stacks.length);

        for (ItemStack stack : stacks)
        {
            if (stack == null) continue;

            // This is necessary as the sign's items are sometimes CraftItemStack type. That type
            // does not populate its private fields, thus it is useless to Gson's serializer.
            ItemStack isBackup = new ItemStack( stack.getType(), 1, stack.getDurability() );

            isBackup.setData( stack.getData() );
            isBackup.setItemMeta( stack.getItemMeta() );

            int amount = items.containsKey(isBackup)
                ? items.get(isBackup) + stack.getAmount()
                : stack.getAmount();

            items.put(isBackup, amount);
        }

        // Feeds each integer into the item stack's setAmount
        items.forEach(ItemStack::setAmount);

        return items.keySet().toArray(new ItemStack[ items.size() ]);
    }

    public void free()
    {
        signs = null;
    }
}
