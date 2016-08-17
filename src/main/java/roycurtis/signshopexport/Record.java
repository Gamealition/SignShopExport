package roycurtis.signshopexport;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Record
{
    public enum SignType
    {
        Buy,
        Sell
    }

    // Location
    public String locWorld;
    public int    locX;
    public int    locY;
    public int    locZ;

    // Owner
    public String ownerName;
    public UUID   ownerUuid;

    // Sign data
    public SignType signType;
    public double   signPrice;

    // Inventory data
    public ItemStack[] invItems;
    public boolean     invInStock;
}
