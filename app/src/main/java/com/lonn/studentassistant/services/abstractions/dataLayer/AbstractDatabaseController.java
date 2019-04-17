package com.lonn.studentassistant.services.abstractions.dataLayer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.services.abstractions.DatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDatabaseController<T extends BaseEntity> implements IDatabaseController<T>
{
    private Class<T> type;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseReference;
    private List<DatabaseService<T>> boundServices = new ArrayList<>();

    public AbstractDatabaseController(Class<T> type)
    {
        this.type = type;
    }

    public Class getType()
    {
        return type;
    }

    public void populateRepository(final List<T> list, final String child)
    {
        Log.e("Setting listener on", databaseReference.getKey() + "/" + child);

        databaseReference.child(child).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                T item = dataSnapshot.getValue(type);

                if (item == null)
                {
                    for (DatabaseService<T> service : boundServices)
                    {
                        service.sendResponse(new GetByIdResponse<>("Get by id failed", (T)null));
                    }
                    return;
                }

                item.setKey(dataSnapshot.getKey());
                list.add(item);

                for (DatabaseService<T> service : boundServices)
                {
                    service.sendResponse(new GetByIdResponse<>("success", item));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                for (DatabaseService<T> service : boundServices)
                {
                    service.sendResponse(new GetByIdResponse<>("Get by id failed", (T)null));
                }
            }
        });
    }

    public void populateRepository(final List<T> list)
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

                for (DatabaseService<T> service : boundServices)
                {
                    service.sendResponse(new GetAllResponse<>("success", list));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                for (DatabaseService<T> service : boundServices)
                {
                    service.sendResponse(new GetAllResponse<>("Get all failed", (T)null));
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

    public void bindService(DatabaseService<T> service)
    {
        if (service != null)
        {
            boundServices.add(service);
        }
    }

    public void unbindService(DatabaseService<T> service)
    {
        if (service != null)
        {
            boundServices.remove(service);
        }
    }
}
