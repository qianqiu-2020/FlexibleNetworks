package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;
/*此活动为定位活动，目前采用百度地图API实现*/
public class LBSActivity extends BaseActivity {
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        setContentView(R.layout.activity_lbs);
        positionText = (TextView) findViewById(R.id.position_text_view);

        /*一次性获取多个权限的方法,所有权限均授权后调用requestLocation方法*/
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LBSActivity.this, permissions, 1);
        } else {
            Log.d("mark","已授权");
            requestLocation();
        }
    }



    private void requestLocation() {
        initLocation();//定位相关设置
        mLocationClient.start();//开始定位，获取到结果时回调MyLocationListener的方法
    }
    /*定位相关设置*/
    private void initLocation() {
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(3000);
//      option.setLocationMode(LocationClientOption.LocationMode. Device_Sensors);只使用GPS，如果未设置默认为gps+网络定位
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();//退出时停止定位，防止耗电
    }

    @Override
    public void processMessage(Message msg) {

    }
    /*处理权限请求结果*/
    @Override
    public  void onRequestPermissionsResult(int requestCode,String []permissions,int []grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用此功能",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    /*百度API获取到定位结果时调用此方法返回数据*/
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String adcode = location.getAdCode();    //获取adcode
            String town = location.getTown();    //获取乡镇信息
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            StringBuilder currentPosition=new StringBuilder();
            currentPosition.append("【您的位置信息如下】").append("\n");
            currentPosition.append("经纬度坐标类型 ").append(coorType).append("\n");
            currentPosition.append("维度 ").append(latitude).append("\n");
            currentPosition.append("经度 ").append(longitude).append("\n");
            currentPosition.append("精度 ").append(radius).append("\n");

            if(location.getLocType()==BDLocation.TypeGpsLocation){//判断定位方式
                currentPosition.append("定位方式 GPS").append("\n");
            }else  if(location.getLocType()==BDLocation.TypeNetWorkLocation){
                currentPosition.append("定位方式 网络/GPS").append("\n");
            }

            currentPosition.append("国家 ").append(country).append("\n");
            currentPosition.append("省份 ").append(province).append("\n");
            currentPosition.append("城市 ").append(city).append("\n");
            currentPosition.append("区县 ").append(district).append("\n");
            currentPosition.append("乡镇信息 ").append(town).append("\n");
            currentPosition.append("街道信息 ").append(street).append("\n");
            currentPosition.append("adcode ").append(adcode).append("\n");
            currentPosition.append("详细地址信息 ").append(addr).append("\n");
            currentPosition.append("---数据来自Baidu定位---");

            if(!currentPosition.toString().contains("null"))
            positionText.setText(currentPosition);//显示结果
            else positionText.setText("请打开GPS");
            /*向主服务器发送定位结果*/
            if(mark>0 && !currentPosition.toString().contains("null")) {
                Msg msg = new Msg(Msg.TYPE_LBS, user_id, currentPosition.toString());
                Log.d("msg", "位置消息构造完成");
                netThread.setMsg(msg);
                netThread.setHandler(handler);
                new Thread(netThread).start();//子线程开始运行
                mark=mark-1;
            }
        }
    }
}