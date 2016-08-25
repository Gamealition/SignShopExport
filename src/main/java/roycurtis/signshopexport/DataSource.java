package roycurtis.signshopexport;

import roycurtis.signshopexport.json.Record;

public interface DataSource
{
    /** Makes this source fetch all known shops. Returns how many found. */
    int prepare();

    /** Creates a record for JSON serialization of the element at the given index */
    Record createRecordForIndex(int idx);

    /** Frees up any held references and resources */
    void free();
}
