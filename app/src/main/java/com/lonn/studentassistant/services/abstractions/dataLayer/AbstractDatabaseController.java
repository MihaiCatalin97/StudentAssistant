package com.lonn.studentassistant.services.abstractions.dataLayer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.CustomList;
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
    private DatabaseService<T> boundService;

    public AbstractDatabaseController(Class<T> type)
    {
        this.type = type;
    }

    public Class getType()
    {
        return type;
    }

    public void populateRepository(final CustomList<T> list, final String child, final ICallback<DatabaseResponse<T>> callback)
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
                    boundService.sendResponse(new GetByIdResponse<>("Get by id failed", (T)null), callback);
                    return;
                }

                item.setKey(dataSnapshot.getKey());
                list.add(item);

                boundService.sendResponse(new GetByIdResponse<>("success", item), callback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                boundService.sendResponse(new GetByIdResponse<>("Get by id failed", (T)null), callback);
            }
        });
    }

    public void populateRepository(final CustomList<T> list, final ICallback<DatabaseResponse<T>> callback)
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
                        Log.e("Added " + type.getSimpleName(), key);
                    }
                }

                listenForChanges(list);
                boundService.sendResponse(new GetAllResponse<>("success", list), callback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                boundService.sendResponse(new GetAllResponse<>("Get all failed", (T)null), callback);
            }
        });
    }

    private void listenForChanges(final CustomList<T> list)
    {
        for(T item : list)
        {
            databaseReference.child(item.getKey()).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    T item = dataSnapshot.getValue(type);

                    if (item != null)
                    {
                        item.setKey(dataSnapshot.getKey());

                        if (!list.contains(item))
                        {
                            Log.e("Data changed", dataSnapshot.getKey());

                            int indexOfKey = list.indexOfKey(item.getKey());

                            if(indexOfKey == -1)
                            {
                                list.remove(indexOfKey);
                                Log.e("Created " + type.getSimpleName(), dataSnapshot.getKey());

                                boundService.sendResponse(new CreateResponse<T>("success", item), null);
                            }
                            else
                            {
                                list.set(indexOfKey, item);
                                Log.e("Updated " + type.getSimpleName(), dataSnapshot.getKey());

                                boundService.sendResponse(new EditResponse<T>("success", item), null);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }
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

        Log.e("Enrolling courses 3", Integer.toString(items.size()));
        databaseReference.updateChildren(itemMap);
    }

    public void bindService(DatabaseService<T> service)
    {
        if (service != null && (boundService == null || !boundService.equals(service)))
        {
            boundService = service;
        }
    }

    public void unbindService(DatabaseService<T> service)
    {
        if (boundService != null && boundService.equals(service))
        {
            boundService = service;
        }
    }
}
