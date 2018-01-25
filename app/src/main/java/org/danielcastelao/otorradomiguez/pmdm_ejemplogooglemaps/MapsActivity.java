package org.danielcastelao.otorradomiguez.pmdm_ejemplogooglemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
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


import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Location currentLocation;
    private LocationManager locationManager;

    private LatLng objectiveLocation1;
    private LatLng objectiveLocation2;
    private LatLng objectiveLocation3;

    private Polyline ruta=null;
    private PolylineOptions rutaOptions;


    private TextView textViewAccuracy;
    private TextView textViewLat;
    private TextView textViewLng;
    private TextView textViewDist;
    private TextView textViewDistancia;

    private Button botonQR;
    final LatLng danielCastelao = new LatLng(42.236574, -8.714311);

    private Intent esteIntent;

    private TextView tiempo;
    private long tiempoRestante;

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

        esteIntent=this.getIntent();

        tiempo=(TextView)findViewById(R.id.tiempo);
        if(esteIntent.getExtras()!=null){
            tiempoRestante=esteIntent.getExtras().getLong("tiempo");
        }else {
            tiempoRestante = 0;
        }

        textViewAccuracy.setText("Acc: ");
        textViewLat.setText("Lat: ");
        textViewLng.setText("Lng: ");
        textViewDist.setText("Distancia: ");
        textViewDistancia.setBackgroundColor(Color.parseColor("#bbdefb"));
        textViewDistancia.setText("Sin ubicacion");

        objectiveLocation1 =new LatLng(42.237436,-8.714226);
        objectiveLocation2 =new LatLng(42.237154,-8.714602);
        objectiveLocation3 = new LatLng(42.23766,-8.715439);



        botonQR =(Button)findViewById(R.id.buttonQR);
        //  todo descomentar
        // botonQR.setVisibility(View.GONE);
        botonQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barcodeReader = new Intent(getApplicationContext(),QRActivity.class);
                //Si fuese nulo los inicializaré en el winActivity
                if(esteIntent.getExtras()!=null) {
                    barcodeReader.putExtra("t1",esteIntent.getExtras().getBoolean("t1"));
                    barcodeReader.putExtra("t2",esteIntent.getExtras().getBoolean("t2"));
                    barcodeReader.putExtra("t3",esteIntent.getExtras().getBoolean("t3"));
                    barcodeReader.putExtra("tiempo",esteIntent.getExtras().getLong("tiempo"));
                }else{
                    barcodeReader.putExtra("t1",false);
                    barcodeReader.putExtra("t2",false);
                    barcodeReader.putExtra("t3",false);
                    barcodeReader.putExtra("tiempo",tiempoRestante);
                }
                startActivity(barcodeReader);
            }
        });

        /*
        Comprobar si llegan los extras al volver al mapa desde el qr
         */
        System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡EXTRAS!!!!!!!!!!!!!!!!!");
        if(esteIntent.getExtras()!=null) {
            System.out.println(this.getIntent().getExtras().size());

            System.out.println(esteIntent.getExtras().getBoolean("t1"));
            System.out.println(esteIntent.getExtras().getBoolean("t2"));
            System.out.println(esteIntent.getExtras().getBoolean("t3"));

        }


        /*
        TIMER
         */
        long tiempoHastaAcabar;
        if(tiempoRestante!=0){
            tiempoHastaAcabar=tiempoRestante;
        }else{
            tiempoHastaAcabar=10000;
        }


//2706000 45min en milis, si paso a la win activity, no me mantiene el contador
        new CountDownTimer(tiempoHastaAcabar, 1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                final String FORMAT="%02d:%02d:%02d";
                long tiempoHastaAcabar;
                if(tiempoRestante!=0){
                    tiempoHastaAcabar=tiempoRestante;
                }else{
                    tiempoHastaAcabar=millisUntilFinished;
                }

                tiempo.setText("Tiempo Restante :"+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(tiempoHastaAcabar),
                        TimeUnit.MILLISECONDS.toMinutes(tiempoHastaAcabar) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(tiempoHastaAcabar)),
                        TimeUnit.MILLISECONDS.toSeconds(tiempoHastaAcabar) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(tiempoHastaAcabar))));
                tiempoRestante=millisUntilFinished;
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                tiempo.setText("HAS PERDIDO!!!!");
                finish();
            }

        }.start();
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
        mMap.addMarker(new MarkerOptions().position(objectiveLocation1).title("Objetivo 1"));
        mMap.addMarker(new MarkerOptions().position(objectiveLocation2).title("Objetivo 2"));
        mMap.addMarker(new MarkerOptions().position(objectiveLocation3).title("Objetivo 3"));

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
        mMap.addCircle(new CircleOptions().center(danielCastelao).radius(250).strokeColor(Color.WHITE));

        //Comprobamos los permisos de localizacion y se los pedimos en caso de no tenerlos antes de activar MyLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
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
                Location objective1=new Location("");
                objective1.setLatitude(objectiveLocation1.latitude);
                objective1.setLongitude(objectiveLocation1.longitude);

                Location objective2=new Location("");
                objective2.setLatitude(objectiveLocation2.latitude);
                objective2.setLongitude(objectiveLocation2.longitude);

                Location objective3=new Location("");
                objective3.setLatitude(objectiveLocation3.latitude);
                objective3.setLongitude(objectiveLocation3.longitude);

                Location centerPosition=new Location("");
                centerPosition.setLatitude(danielCastelao.latitude);
                centerPosition.setLongitude(danielCastelao.longitude);

                textViewDist.setText("Distancia: "+df.format(location.distanceTo(objective1)));

                //todo Comentado para pruebas, descomentar despues

                if(location.distanceTo(objective1)>20&&location.distanceTo(objective2)>20&&location.distanceTo(objective3)>20){
                    botonQR.setVisibility(View.GONE);
                }


                if(location.distanceTo(centerPosition)>250){
                    textViewDistancia.setText("Has salido de la zona");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#bbdefb"));
                }else if((location.distanceTo(objective1)<20&&!esteIntent.getExtras().getBoolean("t1"))
                        ||(location.distanceTo(objective2)<20&&!esteIntent.getExtras().getBoolean("t2"))
                        ||(location.distanceTo(objective3)<20&&!esteIntent.getExtras().getBoolean("t3"))){
                    textViewDistancia.setText("Estas a menos de 20m. Busca un QR");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#7f0000"));
                    botonQR.setVisibility(View.VISIBLE);
                }else if((location.distanceTo(objective1)<50&&!esteIntent.getExtras().getBoolean("t1"))
                        ||(location.distanceTo(objective2)<50&&!esteIntent.getExtras().getBoolean("t2"))
                        ||(location.distanceTo(objective3)<50&&!esteIntent.getExtras().getBoolean("t3"))){
                    textViewDistancia.setText("Muy caliente. Estas a menos de 50m");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#f44336"));
                }else if((location.distanceTo(objective1)<90&&!esteIntent.getExtras().getBoolean("t1"))
                        ||(location.distanceTo(objective2)<90&&!esteIntent.getExtras().getBoolean("t2"))
                        ||(location.distanceTo(objective3)<90&&!esteIntent.getExtras().getBoolean("t3"))){
                    textViewDistancia.setText("Caliente. Estas a menos de 90m");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#ec407a"));
                }else if((location.distanceTo(objective1)<130&&!esteIntent.getExtras().getBoolean("t1"))
                        ||(location.distanceTo(objective2)<130&&!esteIntent.getExtras().getBoolean("t2"))
                        ||(location.distanceTo(objective3)<130&&!esteIntent.getExtras().getBoolean("t3"))){
                    textViewDistancia.setText("Frio");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#ab47bc"));
                }else if((location.distanceTo(objective1)<170&&!esteIntent.getExtras().getBoolean("t1"))
                        ||(location.distanceTo(objective2)<170&&!esteIntent.getExtras().getBoolean("t2"))
                        ||(location.distanceTo(objective3)<170&&!esteIntent.getExtras().getBoolean("t3"))){
                    textViewDistancia.setText("Muy frio");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#3f51b5"));
                }else if((location.distanceTo(objective1)<210&&!esteIntent.getExtras().getBoolean("t1"))
                        ||(location.distanceTo(objective2)<210&&!esteIntent.getExtras().getBoolean("t2"))
                        ||(location.distanceTo(objective3)<210&&!esteIntent.getExtras().getBoolean("t3"))) {
                    textViewDistancia.setText("Helado");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#42a5f5"));
                }else{
                    textViewDistancia.setText("No estas cerca de ningun tesoro");
                    textViewDistancia.setBackgroundColor(Color.parseColor("#bbdefb"));
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

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
