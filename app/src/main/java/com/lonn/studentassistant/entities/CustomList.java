package com.lonn.studentassistant.entities;

import java.util.ArrayList;
import java.util.List;

public class CustomList<T extends BaseEntity> extends ArrayList<T>
{
    public CustomList(List<T> items)
    {
        super(items);
    }

    public CustomList()
    {
        super();
    }

    public int indexOfKey(String key)
    {
        for(int i =0;i<size();i++)
        {
            if(get(i).getKey().equals(key))
                return i;
        }

        return -1;
    }
}
