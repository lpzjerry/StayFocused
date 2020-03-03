package edu.dartmouth.stayfocus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.dartmouth.stayfocus.Focus.FocusTimeBean;
import edu.dartmouth.stayfocus.Focus.FocusingActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    public GoogleApiClient googleApiClient;

    // Time Picker
    private OptionsPickerView pvOptions;
    ArrayList<FocusTimeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, 0);
        }

        getOptionData();
        initOptionPicker();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_todo, R.id.nav_record,
                R.id.nav_settings, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Button buttonShare = (Button) findViewById(R.id.share_link);
        Button buttonLogout = (Button) findViewById(R.id.logout);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Stay Focus APP";
                String shareSub = "https://home.cs.dartmouth.edu/~pengze/stayfocus/";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseAuth.signOut();

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            }
                        });

                loadLogInView();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {

            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView email = headerView.findViewById(R.id.nav_user);
            email.setText(firebaseAuth.getCurrentUser().getEmail());


        }
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void OnSetTimerClicked(View view) {
        pvOptions.show();
    }

    private void getOptionData() {
        options1Items.add(new FocusTimeBean(0, "0", "hours", "others"));
        options1Items.add(new FocusTimeBean(1, "1", "minutes", "others"));

        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("1");
        options2Items_01.add("15");
        options2Items_01.add("30");
        options2Items_01.add("45");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("0");
        options2Items_02.add("15");
        options2Items_02.add("30");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
    }

    private void initOptionPicker() {

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int hour = Integer.parseInt(options1Items.get(options1).getPickerViewText());
                int minute = Integer.parseInt(options2Items.get(options1).get(options2));
                Intent intent = new Intent(MainActivity.this, FocusingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                bundle.putInt("second", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        })
                .setTitleText("Stay Focused")
                .setSubmitText("Focus")
                .build();
        pvOptions.setSelectOptions(0, 0);
        pvOptions.setPicker(options1Items, options2Items);
    }
}