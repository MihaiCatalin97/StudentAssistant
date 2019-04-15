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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDatabaseController<T extends BaseEntity> implements IDatabaseController<T>
{
    private Class<T> type;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseReference;
    private List<LocalService<T>> boundServices = new ArrayList<>();

    public AbstractDatabaseController(Class<T> type)
    {
        this.type = type;
    }

    public Class getType()
    {
        return type;
    }

    public void populateRepository(final CustomList<T> list, final String child)
    {
        databaseReference.child(child).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                T item = dataSnapshot.getValue(type);
                item.setKey(dataSnapshot.getKey());
                list.add(item);
                Log.e("Adding " + getItemType() + ": " + item.getKey() + ". Total", Integer.toString(list.size()));

                for (LocalService<T> service : boundServices)
                {
                    service.respondOneItem("getById", "success", list.getByKey(child));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                for (LocalService<T> service : boundServices)
                {
                    service.respondOneItem("getById", "Get by id failed", null);
                }
            }
        });
    }

    public void populateRepository(final CustomList<T> list)
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapsot : dataSnapshot.getChildren())
                {
                    String key = snapsot.getKey();
                    T item = snapsot.getValue(type);

                    if (item != null)
                    {
                        item.setKey(key);

                        list.add(item);
                        Log.e("Adding " + getItemType() + ": " + item.getKey() + ". Total", Integer.toString(list.size()));
                    }
                }

                for (LocalService<T> service : boundServices)
                {
                    service.respondMultipleItems("getAll", "success", list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                for (LocalService<T> service : boundServices)
                {
                    service.respondMultipleItems("getAll", "Get all failed", null);
                }
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

    public void bindService(LocalService service)
    {
        if (service != null)
        {
            boundServices.add(service);
        }
    }

    public void unbindService(LocalService service)
    {
        if (service != null)
        {
            boundServices.remove(service);
        }
    }
}
