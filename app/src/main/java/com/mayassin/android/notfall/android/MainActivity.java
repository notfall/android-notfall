package com.mayassin.android.notfall.android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mayassin.android.notfall.R;
import com.mayassin.android.notfall.plainjava.User;

import java.util.ArrayList;

/**
 * Created by mohamed on 1/29/17.
 */

public class MainActivity extends AppCompatActivity implements PopUpInterface {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 21;
    private static final int MY_PERMISSION_CALL_PHONE = 22;
    private static final int MY_PERMISSION_SEND_SMS = 18;
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private RecyclerView recyclerView;
    private Button call911Button;
    private FloatingActionMenu requestFabMenu;
    private FloatingActionButton call911Fab, requestRespondersFab;
    private boolean openedSearchSpinner;
    private SessionManager sess;
    private Location lastLocation;
    private User clinton, jbill, mohamed, mscott, tbob;
    private ArrayList<User> allCareTakers = new ArrayList<User>();
    private ArrayList<User> allFirstResponders = new ArrayList<User>();
    private ArrayList<User> allHospitals = new ArrayList<User>();

    private ArrayList<User> nearbyHelpers = new ArrayList<User>();

    private User currentUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private StorageReference storageRef;
    private RecycleViewAdapterHelpers adapter = new RecycleViewAdapterHelpers();
    private int currentlyViewing;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://notfall-aac12.appspot.com");
        sess = new SessionManager(this);
        getAllHelpers();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = "Hello, " + sess.getCurrentUser();
        recyclerView = (RecyclerView) findViewById(R.id.recyle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        call911Button = (Button) findViewById(R.id.call_911_button);
        requestFabMenu = (FloatingActionMenu) findViewById(R.id.main_fab_menu);
        call911Fab = (FloatingActionButton) findViewById(R.id.call_911_fab);
        requestRespondersFab = (FloatingActionButton) findViewById(R.id.request_fab);

        requestFabMenu.hideMenuButton(false);

        addDrawerItems();
        setupDrawer();
        pullDataAboutCurrentUser();

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSION_CALL_PHONE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mActivityTitle);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initializeApp();
            grabLocation();
        } else {
            checkPermissions();
        }

        requestRespondersFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearbyHelpersBuilder();
                requestFabMenu.close(true);
                postNearestToYou();

            }
        });

        sendTokenToDatabase();
        checkTextPermission();
    }

    private void sendTokenToDatabase() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").child(sess.getCurrentUser()).child("phone_token").setValue(tkn);
//
    }

    private void pullDataAboutCurrentUser() {
        String username = sess.getCurrentUser();
        currentUser = new User(username);

        // PULL DATA FROM SERVER FROM USERNAME FROM SESSIONMANAGER

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            } else {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_CALL_PHONE);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSION_CALL_PHONE);


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    private void getAllHelpers() {
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference queryref = mFirebaseDatabaseReference.child("users");
        queryref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (((int) (long) child.child("type").getValue()) == 1) {
                        User tempUser = new User(child.getKey().toString());
                        allCareTakers.add(tempUser);
                    }
                    if (((int) (long) child.child("type").getValue()) == 2) {
                        User tempUser = new User(child.getKey().toString());
                        allFirstResponders.add(tempUser);
                    }
                    if (((int) (long) child.child("type").getValue()) == 3) {
                        User tempUser = new User(child.getKey().toString());
                        allHospitals.add(tempUser);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void nearbyHelpersBuilder() {
        nearbyHelpers.clear();
        ArrayList<User> allHelpers = new ArrayList<User>();
        if (currentlyViewing == 0) {
            allHelpers.addAll(allCareTakers);
            allHelpers.addAll(allFirstResponders);
        } else if (currentlyViewing == 1) {
            allHelpers.addAll(allCareTakers);
        } else if (currentlyViewing == 2) {
            allHelpers.addAll(allFirstResponders);
        } else if (currentlyViewing == 3) {
            allHelpers.addAll(allHospitals);
        }
        for (User helper : allHelpers) {
            double distance = distance(currentUser.getLatitude(), currentUser.getLongitude(), helper.getLatitude(), helper.getLongitude(), 'M');
            if (distance < 3) {
                nearbyHelpers.add(helper);
            }
        }
    }

    private void postNearestToYou() {
        String contentString = "";
        for (User helper : nearbyHelpers) {
            contentString += helper.fullName + " \n";
        }
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Request?")
                .content(contentString)
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .neutralColor(getResources().getColor(R.color.Gray))
                .positiveText("REQUEST")
                .neutralText("NEVERMIND")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sendTextToNearby();
                    }
                })
                .show();
    }

    private void sendTextToNearby() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                1);
        for (User helper : nearbyHelpers) {
            SmsManager.getDefault().sendTextMessage(helper.phoneNumber, null, currentUser.getFullName() + " needs help at Minneapolis,Minnesota(" + currentUser.getLocation() + ")\n \n \n https://www.google.com/maps/@" + currentUser.getLocation() + ",15z", null, null);
        }

    }

    public void checkTextPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS, Manifest.permission.SEND_SMS};
            final int REQUEST_CONTACTS = 10;
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }

    }

    private void initializeApp() {

        final MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.looking_for_spinner);
        spinner.setItems("....", "a care taker", "a first responder", "a nearby hospital");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (!openedSearchSpinner) {
                    call911Button.setVisibility(View.GONE);
                    requestFabMenu.showMenuButton(true);
                    openedSearchSpinner = true;
                }
                currentlyViewing = position;
                if (position == 0) {
                    spinner.setText("...all");
                    ArrayList<User> allHelpers = new ArrayList<User>();
                    allHelpers.addAll(allCareTakers);
                    allHelpers.addAll(allFirstResponders);
                    allHelpers.addAll(allHospitals);
                    adapter = new RecycleViewAdapterHelpers(allHelpers);
                }
                if (position == 1) {
                    adapter = new RecycleViewAdapterHelpers(allCareTakers);
                }
                if (position == 2) {
                    adapter = new RecycleViewAdapterHelpers(allFirstResponders);
                }
                if (position == 3) {
                    adapter = new RecycleViewAdapterHelpers(allHospitals);
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setPopUpInterface(MainActivity.this);
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        setUpOnClickListeners();
    }

    private void grabLocation() {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                lastLocation = location;
                postLocationToServer();
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

    }

    private void postLocationToServer() {

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").child(sess.getCurrentUser()).child("location").setValue(lastLocation.getLatitude() + "," + lastLocation.getLongitude());
//        mFirebaseDatabaseReference.child("users").child(sess.getCurrentUser()).child("full_name").setValue();

    }

    private void setUpOnClickListeners() {
        call911Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergency911PopUp();
            }
        });
        call911Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFabMenu.close(true);
                emergency911PopUp();
            }
        });

    }

    private void addDrawerItems() {
        String[] osArray = {"Profile", "Home", "Navigate", "Contact", "Logout", "Call 911"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "Position Clicked " + position, Toast.LENGTH_SHORT).show();
                getSupportActionBar().setTitle(mActivityTitle);
                mDrawerLayout.closeDrawer(Gravity.LEFT, true);

                if (position == 4) {
                    sess.destroySession();
                }

                if (position == 0) {
                    showProfilePopUp();
                }
                if (position == 5) {
                    emergency911PopUp();
                }
            }
        });
    }

    private void showProfilePopUp() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.profile_custom_dialog, true)
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .neutralColor(getResources().getColor(R.color.Gray))
                .build();

        dialog.show();
        TextView profileFullNameTextView = (TextView) dialog.getCustomView().findViewById(R.id.profile_full_name_text_view);
        TextView profilelocationTextView = (TextView) dialog.getCustomView().findViewById(R.id.profile_location_text_view);
        TextView profileAgeTextView = (TextView) dialog.getCustomView().findViewById(R.id.profile_age_text_view);

        final ImageView profilePictureImageView = (ImageView) dialog.getCustomView().findViewById(R.id.profile_image);
        profileFullNameTextView.setText(currentUser.getFullName());
        profilelocationTextView.setText("" + lastLocation.getLatitude() + " , " + lastLocation.getLongitude());
        profileAgeTextView.setText(currentUser.getAge() + "");

        StorageReference pathReference = storageRef.child("users").child(currentUser.getUsername()).child("profilepic.jpg");
        pathReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePictureImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // WHEN IMAGE FAILED
            }
        });
    }


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void emergency911PopUp() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("ARE YOU SURE?")
                .content("Clicking yes will call the police and contact any first responders near you.")
                .positiveColor(getResources().getColor(R.color.Red))
                .neutralColor(getResources().getColor(R.color.Gray))
                .positiveText("CALL 911")
                .neutralText("NEVERMIND")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:9542606118"));
                        currentlyViewing = 0;
                        nearbyHelpersBuilder();
                        sendTextToNearby();
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_SEND_SMS:
            case MY_PERMISSION_CALL_PHONE:
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initializeApp();
                    grabLocation();
                } else {
                    MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .title("Permission Denied")
                            .positiveColor(getResources().getColor(R.color.colorPrimary))
                            .neutralColor(getResources().getColor(R.color.Gray))
                            .content("This app is almost useless without location permission. Please grant it")
                            .positiveText("OKAY")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                                }
                            })
                            .show();
                }
                return;
            }

        }
    }

    @Override
    public void popUpHelper(User user) {
        double distance = distance(currentUser.getLatitude(), currentUser.getLongitude(), user.getLatitude(), user.getLongitude(), 'M');
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Request " + user.getFullName() + "?")
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .neutralColor(getResources().getColor(R.color.Gray))
                .content("You can request this helper manually, or request whoever is near you with the request button!\n\n This helper is " + String.format( "%.3f", distance ) + " miles away from you.")
                .positiveText("OKAY")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                    }
                })
                .show();
    }
}

