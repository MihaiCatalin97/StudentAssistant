package com.lonn.studentassistant.common.abstractClasses;

import android.app.Service;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.common.interfaces.IDatabaseController;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.lists.CustomList;
import com.lonn.studentassistant.services.coursesService.CourseService;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDatabaseController<T extends BaseEntity> implements IDatabaseController<T>
{
    private Class<T> type;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseReference;
    private Service boundService;

    public AbstractDatabaseController(Class<T> type, Service boundService)
    {
        this.type = type;
        this.boundService = boundService;
    }

    public void setAll(final CustomList<T> list)
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String key = snapshot.getKey();
                    int index = list.getIndexByKey(key);

                    T item = snapshot.getValue(type);
                    item.setKey(key);

                    if (index >= 0)
                    {
                        list.set(index, item);
                        Log.e("Updating " + getItemType() + ": " + item.getKey() + ". Total", Integer.toString(list.size()));
                    }
                    else {
                        list.add(item);
                        Log.e("Adding " + getItemType() + ": " + item.getKey() + ". Total", Integer.toString(list.size()));
                    }
                }

                for (int i=0;i<list.size();i++)
                {
                    T item = list.get(i);

                    if (!dataSnapshot.hasChild(item.getKey()))
                    {
                        Log.e("Removing " + getItemType() + ": " + item.getKey() + ". Total", Integer.toString(list.size()));
                        list.remove(item);
                        i--;
                    }
                }

                notifyDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String getItemType()
    {
        return type.getSimpleName();
    }

    public void add(T item)
    {
        databaseReference.child(item.getKey()).setValue(item);
    }

    public void add(CustomList<T> items)
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

    public void remove(CustomList<T> items)
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

    public void update(CustomList<T> items)
    {
        Map<String, Object> itemMap = new HashMap<>();

        for (T item : items)
        {
            itemMap.put(item.getKey(), item);
        }

        databaseReference.updateChildren(itemMap);
    }

    private void notifyDataChanged()
    {
        if (boundService != null)
            ((CourseService) boundService).getAll();
    }
}
