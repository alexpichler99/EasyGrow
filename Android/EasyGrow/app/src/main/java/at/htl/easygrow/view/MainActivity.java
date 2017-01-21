package at.htl.easygrow.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import at.htl.easygrow.R;
import at.htl.easygrow.model.PlantModel;
import at.htl.easygrow.view.fragments.HumidityFragment;
import at.htl.easygrow.view.fragments.MoistureFragment;
import at.htl.easygrow.view.fragments.OverViewFragment;
import at.htl.easygrow.view.fragments.SettingsFragment;
import at.htl.easygrow.view.fragments.TemperatureFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private OverViewFragment overViewFragment;
    private MoistureFragment moistureFragment;
    private SettingsFragment settingsFragment;
    private HumidityFragment humidityFragment;
    private TemperatureFragment temperatureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        overViewFragment = new OverViewFragment();
        moistureFragment = new MoistureFragment();
        settingsFragment = new SettingsFragment();
        humidityFragment = new HumidityFragment();
        temperatureFragment = new TemperatureFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.root, overViewFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getFragmentManager().getBackStackEntryCount() > 1)
                getFragmentManager().popBackStack();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_overview) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.root, overViewFragment).addToBackStack("overview")
                    .commit();
        }
        else if (id == R.id.nav_moisture) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.root, moistureFragment).addToBackStack("moisture")
                    .commit();
        }
        else if (id == R.id.nav_humidity) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.root, humidityFragment).addToBackStack("humidity")
                    .commit();
        }
        else if (id == R.id.nav_temperature) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.root, temperatureFragment).addToBackStack("temperature")
                    .commit();
        }
        else if (id == R.id.nav_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.root, settingsFragment).addToBackStack("settings")
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_settings);
        menuItem.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    public void moistureOnClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.root, moistureFragment).addToBackStack("moisture")
                .commit();
        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setCheckedItem(R.id.nav_moisture);
    }

    public void humidityOnClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.root, humidityFragment).addToBackStack("humidity")
                .commit();
        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setCheckedItem(R.id.nav_humidity);
    }

    public void temperatureOnClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.root, temperatureFragment).addToBackStack("temperature")
                .commit();
        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setCheckedItem(R.id.nav_temperature);
    }

    public void btnFindArduinoOnClick(View view) {
        final AsyncTask<Void, Boolean, Boolean> task = new AsyncTask<Void, Boolean, Boolean>() {
            private ProgressDialog dialog;

            @Override
            protected void onPreExecute()
            {
                this.dialog = new ProgressDialog(MainActivity.this, 0);
                this.dialog.setTitle("Searching for Arduino");
                this.dialog.setMessage("Scanning network...");
                this.dialog.setCancelable(true);
                this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        // cancel AsyncTask
                        cancel(false);
                    }
                });

                this.dialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params)
            {
                if (PlantModel.getInstance().getPlant().findArduino()) {
                    return true;
                }
                else
                return false;

            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                //called on ui thread
                if (result.booleanValue() == true) {
                    Toast.makeText(getApplicationContext(),
                            "Arduino found!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this,
                            "Arduino not found!", Toast.LENGTH_LONG).show();

                //Set ip on EditText
                EditText editText = (EditText) findViewById(R.id.et_arduino_ip);
                if (editText != null)
                    editText.setText(PlantModel.getInstance().getPlant().getIp());

                dismissDialog();
            }

            private void dismissDialog() {
                if(dialog != null) {
                    if(dialog.isShowing()) {
                        Context context = ((ContextWrapper)dialog.getContext()).getBaseContext();

                        if(context instanceof Activity) {
                            if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                                dialog.dismiss();
                        }
                        else
                            dialog.dismiss();
                    }
                    dialog = null;
                }
            }

            @Override
            protected void onCancelled()
            {
                //called on ui thread
                dismissDialog();
                Toast.makeText(MainActivity.this,
                        "Arduino not found!", Toast.LENGTH_LONG).show();
            }
        };
        task.execute();
    }

    public void btnTestArduinoConnection(View view) {
        final AsyncTask<Void, Boolean, Boolean> task = new AsyncTask<Void, Boolean, Boolean>() {
            private ProgressDialog dialog;
            private AlertDialog alertDialog;

            @Override
            protected void onPreExecute()
            {
                this.alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                this.alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                });

                this.dialog = new ProgressDialog(MainActivity.this, 0);
                this.dialog.setMessage("Testing connection...");
                this.dialog.setCancelable(true);
                this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        // cancel AsyncTask
                        cancel(false);
                    }
                });

                this.dialog.show();

            }

            @Override
            protected Boolean doInBackground(Void... params)
            {
                return PlantModel.getInstance().getPlant().testConnection();
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                //called on ui thread
                if (result.booleanValue() == true) {
                    this.alertDialog.setMessage("Connection successful!");
                }
                else
                    this.alertDialog.setMessage("Connection unsuccessful!");
                alertDialog.show();

                dismissDialog();
            }

            private void dismissDialog() {
                if(dialog != null) {
                    if(dialog.isShowing()) {
                        Context context = ((ContextWrapper)dialog.getContext()).getBaseContext();

                        if(context instanceof Activity) {
                            if(!((Activity)context).isFinishing() && !((Activity)context).isDestroyed())
                                dialog.dismiss();
                        }
                        else
                            dialog.dismiss();
                    }
                    dialog = null;
                }
            }

            @Override
            protected void onCancelled()
            {
                //called on ui thread
                dismissDialog();
                alertDialog.setMessage("Connection unsuccessful!");
                alertDialog.show();
            }
        };
        task.execute();
    }
}
