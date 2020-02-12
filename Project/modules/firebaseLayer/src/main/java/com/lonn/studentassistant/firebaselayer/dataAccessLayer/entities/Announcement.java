package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Announcement extends BaseEntity {
    private String administratorKey;
    private List<AccountType> targetedGroups = new LinkedList<>();
    private String title;
    private String message;
    private Date date;

    @Override
    public Announcement setKey(String key) {
        super.setKey(key);
        return this;
    }
}
