package org.danielcastelao.otorradomiguez.pmdm_ejemplogooglemaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.security.AccessController;
import java.text.DecimalFormat;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Location currentLocation;
    private LocationManager locationManager;

    private LatLng objectiveLocation;

    private Polyline ruta=null;
    private PolylineOptions rutaOptions;


    private TextView textViewAccuracy;
    private TextView textViewLat;
    private TextView textViewLng;
    private TextView textViewDist;
    private TextView textViewDistancia;

    private Button boton1;
    final LatLng danielCastelao = new LatLng(42.236574, -8.714311);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_b);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textViewAccuracy=(TextView) findViewById(R.id.textViewAccuracy);
        textViewLat=(TextView) findViewById(R.id.textViewLat);
        textViewLng=(TextView) findViewById(R.id.textViewLng);
        textViewDist=(TextView) findViewById(R.id.textViewDist);
        textViewDistancia=(TextView) findViewById(R.id.textViewDistancia);

        textViewAccuracy.setText("Acc: ");
        textViewLat.setText("Lat: ");
        textViewLng.setText("Lng: ");
        textViewDist.setText("Distancia: ");
        textViewDistancia.setBackgroundColor(Color.parseColor("#bbdefb"));

        objectiveLocation=new LatLng(42.237436,-8.714226);

        boton1=(Button)findViewById(R.id.button1);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barcodeReader = new Intent(getApplicationContext(),QRActivity.class);
                startActivity(barcodeReader);
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        //Creamos un marcador en las coordenadas anteriores
        mMap.addMarker(new MarkerOptions().position(danielCastelao).title("CFP Daniel Castelao"));
        //todo Ocultar el objetivo tras las pruebas
        //mMap.addMarker(new MarkerOptions().position(objectiveLocation).title("Objetivo"));

        //Movermos la camara a esa posicion
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(danielCastelao, 15));

        //Elegir el tipo de mapa
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Oculta el boton my location
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //estilo del mapa
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));

        //activar brujula
        mMap.getUiSettings().setCompassEnabled(true);

        //Mostramos el area de busqueda
        mMap.addCircle(new CircleOptions().center(danielCastelao).radius(500).strokeColor(Color.WHITE));

        //Comprobamos los permisos de localizacion y se los pedimos en caso de no tenerlos antes de activar MyLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        //Mostra my location (lo ponemos aqui porque hemos comprobado los permisos justo antes)
        mMap.setMyLocationEnabled(true);

        //Cargando el locationManager
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(MapsActivity.this, "Lon: "+location.getLongitude()+" Lat: "+location.getLatitude(), Toast.LENGTH_SHORT).show();
                currentLocation=location;
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                /*
                Aqui generamos el icono a partir de la imagen vectorial usando el metodo de mas abajo (getIconFromDrawable)

                BitmapDescriptor markerIcon = getIconFromDrawable(getResources().getDrawable(R.drawable.ic_cityscape,null));

                mMap.addMarker(new MarkerOptions().position(latLng).title("Aqui estoy").icon(markerIcon));
                */


                //Creamos la polilinea si está vacia
                if(ruta==null&&currentLocation.getAccuracy()<=35) {
                    rutaOptions = new PolylineOptions();
                    rutaOptions.add(latLng);
                    rutaOptions.color(Color.RED);
                    ruta=mMap.addPolyline(rutaOptions);
                }else if(currentLocation.getAccuracy()<=35){
                    List linea=ruta.getPoints();
                    linea.add(latLng);
                    ruta.setPoints(linea);
                }else{
                    Toast.makeText(MapsActivity.this, "Precision insuficiente", Toast.LENGTH_SHORT).show();
                }

                //Formateador para las coordenadas a 4 decimales
                DecimalFormat df=new DecimalFormat("0.0000");

                //Mostramos los datos de localizacion en los textView
                textViewAccuracy.setText("Acc: "+location.getAccuracy());
                textViewLat.setText("Lat: "+df.format(location.getLatitude()));
                textViewLng.setText("Lng: "+df.format(location.getLongitude()));




                //Calculamos la distancia al objetivo y si estamos en el area de busqueda
                Location objective=new Location("");
                objective.setLatitude(objectiveLocation.latitude);
                objective.setLongitude(objectiveLocation.longitude);

                Location centerPosition=new Location("");
                centerPosition.setLatitude(danielCastelao.latitude);
                centerPosition.setLongitude(danielCastelao.longitude);

                textViewDist.setText("Distancia: "+df.format(location.distanceTo(objective)));

                if(location.distanceTo(centerPosition)>500){
                    textViewDistancia.setText("Has salido de la zona");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#bbdefb"));
                }else if(location.distanceTo(objective)<20){
                    textViewDistancia.setText("Estas a menos de 20m. Busca un QR");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#7f0000"));
                }else if(location.distanceTo(objective)<50){
                    textViewDistancia.setText("Muy caliente. Estas a menos de 50m");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#f44336"));
                }else if(location.distanceTo(objective)<100){
                    textViewDistancia.setText("Caliente. Estas a menos de 100m");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#ec407a"));
                }else if(location.distanceTo(objective)<200){
                    textViewDistancia.setText("Frio");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#ab47bc"));
                }else if(location.distanceTo(objective)<300){
                    textViewDistancia.setText("Muy frio");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#3f51b5"));
                }else if(location.distanceTo(objective)>=300) {
                    textViewDistancia.setText("Helado");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#42a5f5"));
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });



    }

    /*
    Esto lo tengo que hacer para convertir las imagenes vectoriales en bitmaps y devolver un BitmapDescriptor para generar el icono
     */
    public BitmapDescriptor getIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
            }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
    Guardar y cargar datos al pausar
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
