package com.lonn.studentassistant.activities.abstractions;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.NavHeaderMainBinding;

import static android.widget.Toast.LENGTH_SHORT;


public abstract class NavBarActivity extends FirebaseConnectedActivity implements NavigationView.OnNavigationItemSelectedListener {
	private int logoutCount = 0;
	private Handler handler = new Handler();

	public NavBarActivity() {
		super();
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
			case R.id.action_sensors:
			case R.id.action_settings: {
				return true;
			}
			case R.id.action_terms: {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage("These are the terms and conditions")
						.setTitle("Terms and Conditions");

				builder.setPositiveButton("OK", (dialog, dialog_id) -> {
					// User clicked OK button
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
				Toast.makeText(getBaseContext(),
						"Press twice to log out!",
						LENGTH_SHORT).show();
				handler.postDelayed(() -> logoutCount = 0,
						1000);
			}
			else {
				logoutCount = 0;
				FirebaseAuth.getInstance().signOut();
				super.onBackPressed();
			}
		}
		logoutCount++;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeNavBar();

		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			NavUtils.navigateUpFromSameTask(this);
		}
	}

	private void initializeNavBar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener((view) -> {
			showSnackBar("Refreshing everything...");
//          businessLayer.refreshAll();
		});

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		NavHeaderMainBinding binding = NavHeaderMainBinding.bind(navigationView.getHeaderView(0));
		FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
		//TODO: Load user
	}
}
