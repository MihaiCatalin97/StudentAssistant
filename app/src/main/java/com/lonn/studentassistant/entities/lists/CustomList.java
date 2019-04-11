package com.lonn.studentassistant.entities.lists;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class CustomList<T extends BaseEntity> extends ArrayList<T>
{
    public CustomList() { super();}
    public CustomList(List<T> items)
    {
        super(items);
    }

    public T getByKey(String key)
    {
        for(int i=0;i<size();i++)
        {
            if(get(i).getKey().equals(key))
                return get(i);
        }

        return null;
    }

    public int getIndexByKey(String key)
    {
        for(int i=0;i<size();i++)
        {
            if(get(i).getKey().equals(key))
                return i;
        }

        return -1;
    }
}
