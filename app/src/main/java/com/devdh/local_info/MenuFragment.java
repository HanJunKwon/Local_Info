package com.devdh.local_info;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    Context context;

    Toolbar tb_menu;
    Button btn_restaurant;
    TextView tv_address;

    Geocoder geocoder;
    List<Address> addressResult;
    Address address;

    SharedPreferences pref;

    String latitude;
    String longitude;

    GpsInfo gps;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        context = container.getContext();

        // 툴바
        tb_menu = view.findViewById(R.id.tb_menu);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_address = view.findViewById(R.id.tv_address);

        // 내부 저장소
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = pref.edit();
        // 내부 저장소에서 위도 경도값 가져옴
        latitude = pref.getString("latitude", "0");
        longitude = pref.getString("longitude", "0");

        // 위도 경도 값으로 주소로 변환
        geocoder = new Geocoder(context);
        addressResult = new ArrayList<>();
        try{
            addressResult = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            address = addressResult.get(0);
        }catch (Exception e){ e.printStackTrace();}

        // 툴바에 위치 표시
        if(addressResult.size() < 1){
            tv_address.setText("위치정보 없음");
        }else{
            tv_address.setText(address.getLocality() + " " + address.getThoroughfare());
            editor.putString("locality", address.getLocality());
            editor.commit();
        }

        // (임시)툴바 타이틀 클릭시 위치정보 업데이트(추후에 액션바 오른쪽에 gps버튼 만들어서 구현)
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GpsInfo(context);
                // gps 사용 유무
                if(gps.isGetLocation()){

                    latitude = latitude = gps.getLatitude() + "";
                    longitude = longitude = gps.getLongitude() + "";

                    editor.putString("latitude", latitude);
                    editor.putString("longitude", longitude);
                    try{
                        addressResult = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                        address = addressResult.get(0);
                        tv_address.setText(address.getLocality() + " " + address.getThoroughfare());
                        editor.putString("locality", address.getLocality());
                        editor.commit();
                        Toast.makeText(context, address.getLocality() + " " + address.getThoroughfare(), Toast.LENGTH_LONG).show();
                    }catch (Exception e){e.printStackTrace();}

                }else{
                    // 사용할 수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        // 음식점 항목 이동
        btn_restaurant = view.findViewById(R.id.btn_restaurant);
        btn_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.contentContainer, new RestaurantFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }


}


