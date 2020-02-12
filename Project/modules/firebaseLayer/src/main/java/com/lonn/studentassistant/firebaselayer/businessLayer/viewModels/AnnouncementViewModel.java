package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Announcement;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AnnouncementViewModel extends EntityViewModel<Announcement> {
    private String administratorKey;
    private List<AccountType> targetedGroups;
    private String title;
    private String message;
    private Date date;

    @Override
    public AnnouncementViewModel setKey(String key) {
        super.setKey(key);
        return this;
    }
}
