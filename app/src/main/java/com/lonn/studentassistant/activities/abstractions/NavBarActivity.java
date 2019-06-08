package com.lonn.studentassistant.activities.abstractions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.NavHeaderMainBinding;
import com.lonn.studentassistant.services.abstractions.BasicService;


public abstract class NavBarActivity extends ServiceBoundActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int logoutCount = 0;

    public NavBarActivity()
    {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeNavBar();

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            NavUtils.navigateUpFromSameTask(this);
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

        switch (id)
        {
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_sensors:
            {
                return true;
            }
            case R.id.action_terms:
            {
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
            case R.id.action_logout:
            {
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
        } else {
            if (logoutCount == 0)
                Toast.makeText(getBaseContext(), "Press twice to log out!", Toast.LENGTH_SHORT).show();
            else
            {
                logoutCount = 0;
                FirebaseAuth.getInstance().signOut();
                unbindServices();
                super.onBackPressed();
            }
        }
        logoutCount++;
    }

    protected abstract void refreshAll();

    private void initializeNavBar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar("Refreshing everything...");

                refreshAll();
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
        binding.setPassedUsed(new UserObservable(FirebaseAuth.getInstance().getCurrentUser()));
    }

    public class UserObservable extends BaseObservable
    {
        @Bindable
        public String email, name;

        UserObservable(FirebaseUser user)
        {
            this.email = user.getEmail();
            this.name = user.getDisplayName();
        }
    }
}
