package com.lonn.studentassistant.common.interfaces;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDatabaseController<T extends BaseEntity> implements IDatabaseController<T>
{
    private Class<T> type;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseReference;

    public AbstractDatabaseController(Class<T> type)
    {
        this.type = type;
    }

    public void setAll(final List<T> list)
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Snap count", Long.toString(dataSnapshot.getChildrenCount()));
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    int index = -1;

                    for (int i=0;i<list.size();i++)
                    {
                        if (list.get(i).getKey().equals(snapshot.getKey()))
                        {
                            index = i;
                            break;
                        }
                    }

                    T item = snapshot.getValue(type);
                    if (snapshot.exists() && item != null)
                    {
                        item.setKey(snapshot.getKey());

                        if (!list.contains(item))
                        {
                            if (index >= 0)
                            {
                                list.set(index, item);
                                Log.e("Updating item", item.getKey());
                            }
                            else {
                                list.add(item);
                                Log.e("Adding item " + Integer.toString(list.size()), item.getKey());
                            }
                        }
                    }
                }

                for (int i=0;i<list.size();i++)
                {
                    T item = list.get(i);
                    boolean found = false;

                    for (DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        if (item.getKey().equals(snap.getKey()))
                        {
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                    {
                        Log.e("Removing item", item.getKey());
                        list.remove(item);
                        i--;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void add(T item)
    {
        databaseReference.child(item.getKey()).setValue(item);
    }

    public void add(List<T> items)
    {
        Map<String, Object> itemMap = new HashMap<>();

        for (T item : items)
        {
            itemMap.put(item.getKey(), item);
        }

        databaseReference.updateChildren(itemMap);
    }

    public void remove(T item)
    {
        databaseReference.child(item.getKey()).setValue(null);
    }

    public void remove(List<T> items)
    {
        HashMap<String, Object> itemMap = new HashMap<>();

        for (T item : items)
        {
            itemMap.put(item.getKey(), null);
        }

        databaseReference.updateChildren(itemMap);
    }

    public void update(T item)
    {
        databaseReference.child(item.getKey()).setValue(item);
    }

    public void update(List<T> items)
    {
        Map<String, Object> itemMap = new HashMap<>();

        for (T item : items)
        {
            itemMap.put(item.getKey(), item);
        }

        databaseReference.updateChildren(itemMap);
    }
}
