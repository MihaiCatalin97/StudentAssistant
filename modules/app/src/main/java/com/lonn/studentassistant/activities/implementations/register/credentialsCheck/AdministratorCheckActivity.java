package com.lonn.studentassistant.activities.implementations.register.credentialsCheck;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.AdministratorCheckActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.viewModels.authentication.AdministratorCredentials;

public class AdministratorCheckActivity extends CredentialsCheckActivity {
    private AdministratorCredentials administratorCredentials = new AdministratorCredentials();

    protected void inflateLayout() {
        AdministratorCheckActivityLayoutBinding binding =
                DataBindingUtil.setContentView(this, R.layout.administrator_check_activity_layout);

        binding.setCredentials(administratorCredentials);
    }

    protected String getCredentialsHash() {
        return IdentificationHash.of(administratorCredentials.toAdministrator())
                .getHash();
    }
}
