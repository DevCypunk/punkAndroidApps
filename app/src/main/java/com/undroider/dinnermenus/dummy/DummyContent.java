package com.undroider.dinnermenus.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent
{

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DinnerItem> ITEMS = new ArrayList<DinnerItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DinnerItem> ITEM_MAP = new HashMap<String, DinnerItem>();

    private static final int COUNT = 0;

    static
    {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static void addItem(DinnerItem item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DinnerItem createDummyItem(int position)
    {
        return new DinnerItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++)
        {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DinnerItem
    {
        private String  id,
                        din_name,
                        din_price;

        public DinnerItem() {};

        public DinnerItem(String din_name, String din_price)
        {
            this.din_name = din_name;
            this.din_price = din_price;
        }

        public DinnerItem(String id, String din_name, String din_price)
        {
            this.id = id;
            this.din_name = din_name;
            this.din_price = din_price;
        }


        //Define the Getters and Setters

        public String getDin_id()
        {
            return this.id;
        }

        public void setDin_id(String din_id)
        {
            this.id = din_id;
        }

        public String getDin_name()
        {
            return this.din_name;
        }

        public void setDin_name(String din_name)
        {
            this.din_name = din_name;
        }

        public String getDin_price()
        {
            return this.din_price;
        }

        public void setDin_price(String din_price)
        {
            this.din_price = din_price;
        }

        // end Getters and Setters
        //---- ----

        @Override
        public String toString()
        {
            return din_name;
        }
        // end mth toString


    }
    // end construct Inner Cls Item
}