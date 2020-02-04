package com.lonn.studentassistant.activities.abstractions;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.NavHeaderMainBinding;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import static android.widget.Toast.LENGTH_SHORT;


public abstract class NavBarActivity<T extends EntityViewModel<? extends Person>> extends FileManagingActivity<T> implements NavigationView.OnNavigationItemSelectedListener {
	private int logoutCount = 0;

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
				delayHandler.postDelayed(() -> logoutCount = 0,
						1000);
			}
			else {
				logoutCount = 0;
				firebaseApi.getAuthenticationService()
						.logout();
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

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	protected void executeWhenLayoutSettles(Runnable runnable) {
		findViewById(R.id.layoutMain).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				viewTreeHandler.removeCallbacksAndMessages(runnable);
				viewTreeHandler.postDelayed(() -> {
					runnable.run();
					findViewById(R.id.layoutMain).getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}, 1000);
			}
		});
	}
}
