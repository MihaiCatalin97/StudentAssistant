package com.lonn.studentassistant.activities.abstractions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.NavHeaderMainBinding;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.viewModels.UserViewModel;


public abstract class NavBarActivity<T extends BaseEntity> extends ServiceBoundActivity<T> implements NavigationView.OnNavigationItemSelectedListener {
    private int logoutCount = 0;

    public NavBarActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeNavBar();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    public abstract void handleNavBarAction(int id);

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        handleNavBarAction(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_sensors: {
                return true;
            }
            case R.id.action_terms: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("These are the terms and conditions")
                        .setTitle("Terms and Conditions");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
            case R.id.action_logout: {
                FirebaseAuth.getInstance().signOut();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (logoutCount == 0) {
                Toast.makeText(getBaseContext(), "Press twice to log out!", Toast.LENGTH_SHORT).show();
            }
            else {
                logoutCount = 0;
                FirebaseAuth.getInstance().signOut();
                super.onBackPressed();
            }
        }
        logoutCount++;
    }

    private void initializeNavBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar("Refreshing everything...");

//                businessLayer.refreshAll();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavHeaderMainBinding binding = NavHeaderMainBinding.bind(navigationView.getHeaderView(0));
        binding.setPassedUsed(new UserViewModel(FirebaseAuth.getInstance().getCurrentUser()));
    }
}
