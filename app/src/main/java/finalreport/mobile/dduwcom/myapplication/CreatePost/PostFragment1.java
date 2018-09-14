package finalreport.mobile.dduwcom.myapplication.CreatePost;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.ReadPost.ReadPostPrmtActivity;
import io.antmedia.android.liveVideoBroadcaster.R;

import static android.content.Context.LOCATION_SERVICE;


public class PostFragment1 extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
        {

    public static final int GALLERY_CODE = 10;

    //firebase
    private FirebaseAuth auth;
    private DatabaseReference ref;

    //googlemapTest
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;
    private Marker buskingMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private static final int PLACE_PICKER_REQUEST = 1;

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocation;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;
    LatLng buskingPosition;

    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    //createPostPromote
    private EditText et_title;
    private EditText et_content;
    private EditText et_bTitle;
    private ImageView iv_postImg;
    private Button btnCreatePost;
    private Button btnSelectLocation;
    private DatePicker mDate;
    private Button bdate;
    String date;
    private TimePicker mTime;
    Calendar c;
    String time;

    //image
    private Uri postImageUri;
    private String imageUrl;

    public PostFragment1() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_post1, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference("/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("firebaseTest", "onChildChange>> " + dataSnapshot.getKey());
                Log.d("firebaseTest", "onChildChange>> " + dataSnapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("firebaseTest", "postSnapShot>> " + postSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //googlemapTest
        mActivity = (AppCompatActivity)getActivity();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.mapPostPromote);
        mapFragment.getMapAsync(this);

        //createPostPromote
        et_title = (EditText)view.findViewById(R.id.et_title);
        et_content = (EditText)view.findViewById(R.id.et_content);
        et_bTitle = (EditText)view.findViewById(R.id.et_bTitle);
        iv_postImg = view.findViewById(R.id.postfragment1_image);
        btnCreatePost = (Button)view.findViewById(R.id.btnCreatePost);
        btnSelectLocation = (Button)view.findViewById(R.id.btnSelectLocation);
        mDate = (DatePicker) view.findViewById(R.id.datepicker);
        bdate = (Button) view.findViewById(R.id.daypicker);
        mTime = (TimePicker) view.findViewById(R.id.timepicker);
        iv_postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPostPromote(et_title.getText().toString(), et_content.getText().toString(),
                        et_bTitle.getText().toString(), buskingPosition.latitude/*Double.parseDouble(et_bLat.getText().toString())*/, /*Double.parseDouble(et_bLon.getText().toString())*/buskingPosition.longitude, date, time);

                //startActivity(new Intent(getContext(), ReadPostPrmtActivity.class));
            }
        });

        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });



        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy년 MM월 dd일", Locale.KOREA );
        Date currentTime = new Date ( );
        String dTime = formatter.format ( currentTime );

        date = String.valueOf(dTime);
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),

                new DatePicker.OnDateChangedListener() {

                    @Override

                    public void onDateChanged(DatePicker view, int year, int monthOfYear,

                                              int dayOfMonth) {


                       date = String.format("%d년 %d월 %d일", year,monthOfYear + 1

                                , dayOfMonth);



                    }

                });

        c = Calendar.getInstance();
        int hour= c.get(c.HOUR_OF_DAY);
        int minute = c.get(c.MINUTE);
        int second = c.get(c.SECOND);

        if (hour >= 12)
            time = "오후 "+( hour-12)+"시 "+minute+"분";
        else
            time = "오전 "+ hour+"시 "+minute+"분";


        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {

                if (hour >= 12)
                    time = "오후 "+ hour+"시 "+min+"분";
                else
                    time = "오전"+ hour+"시 "+min+"분";


            }
        });




        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }

        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        MapsInitializer.initialize(getActivity());

        mGoogleMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d( TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("test")
                        .snippet("lat:" + latLng.latitude + "\nlon:" + latLng.longitude));

                buskingPosition = latLng;
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates){

                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;

            }
        });


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {

            }
        });

    }


    @Override
    public void onLocationChanged(Location location) {

        currentPosition
                = new LatLng( location.getLatitude(), location.getLongitude());


        Log.d(TAG, "onLocationChanged : ");

        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);

        mCurrentLocation = location;
    }


    @Override
    public void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    public void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }


    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더, GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getContext(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;

        if (currentMarker != null)
            currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
/*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        //구글맵의 디폴트 현재 위치는 파란색 동그라미로 표시
        //마커를 원하는 이미지로 변경하여 현재 위치 표시하도록 수정
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        currentMarker = mGoogleMap.addMarker(markerOptions);
*/
        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude() ) ;
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }


    public void setDefaultLocation() {

        mMoveMapByUser = false;

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);
    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if ( mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }



            } else {

                checkPermissions();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //finish();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(PostFragment1.this).commit();
                manager.popBackStack();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //finish();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(PostFragment1.this).commit();
                manager.popBackStack();
            }
        });
        builder.create().show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");


                        if ( mGoogleApiClient.isConnected() == false ) {

                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }
                break;

            case PLACE_PICKER_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(getContext(), data);
                    buskingPosition = place.getLatLng();
                    //선택한 장소에 마커 추가
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(buskingPosition)
                            .title(place.getName().toString()));
                    //선택한 장소로 화면 줌
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buskingPosition, 15));
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case GALLERY_CODE:
                if(resultCode == Activity.RESULT_OK && data != null) {

                    Log.d("PostFragment1", "Photo selected");
                    iv_postImg.setImageURI(data.getData()); // 가운데 뷰를 바꿈
                    postImageUri = data.getData();// 이미지 경로 원본


                }
                break;
        }
    }

    //createPostPromote
    private void createPostPromote(final String postPrmt_title, final String postPrmt_content,
                                   final String postPrmt_busking_title, final double postPrmt_busking_latitude, final double postPrmt_busking_longitude, final String date, final String time) {

        FirebaseStorage.getInstance().getReference().child("prmtPostImages").child(postPrmt_title).putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                @SuppressWarnings("VisibleForTests")
                String imageUrl= task.getResult().getDownloadUrl().toString();
                String path = ref.push().toString();
                String postId = path.substring(path.lastIndexOf("/") + 1);

                SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy년 MM월 dd일", Locale.KOREA );
                Date currentTime = new Date ( );
                String dTime = formatter.format ( currentTime );

                PostPromote postPrmt = new PostPromote();
                postPrmt.postPrmt_title = postPrmt_title;
                postPrmt.postPrmt_content = postPrmt_content;
                postPrmt.postPrmt_postID = postId;
                postPrmt.postPrmt_busking_title = postPrmt_busking_title;
                postPrmt.postPrmt_uid = auth.getCurrentUser().getUid();
                postPrmt.postPrmt_userID =  auth.getCurrentUser().getEmail();
                postPrmt.postPrmt_busking_latitude = postPrmt_busking_latitude;
                postPrmt.postPrmt_busking_longitude = postPrmt_busking_longitude;
                postPrmt.timeCreated = dTime;
                postPrmt.postPrmt_imageUrl = imageUrl;
                postPrmt.busking_date = date;
                postPrmt.busking_time = time;
                ref.child("post_promote").child(postId).setValue(postPrmt);
            }

        });

    }


}