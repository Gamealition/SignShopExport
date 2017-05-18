package roycurtis.signshopexport.json;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/** Represents a JSON record for Gson serialization */
public class Record
{
    // ID
    public long id;

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

    @Override
    public String toString()
    {
        return String.format(
            "Record: %s sign at %d,%d,%d @ %s by %s (%s)",
            signType,
            locX, locY, locZ, locWorld,
            ownerName, ownerUuid
        );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(locWorld, locX, locY, locZ);
    }
}
