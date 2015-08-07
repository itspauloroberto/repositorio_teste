package biz.loginbiz.meuapartamento;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21/07/2015.
 */
public class AFragmentTab extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    static final LatLng LOBIZ = new LatLng(-28.6833745, -49.3640052);
    JSONObject objEmpreendimentos = null;
    JSONArray aryEmpreendimentos = null;
    JSONArray  aryEmpGPSLatitude  = null;
    JSONArray  aryEmpGPSLongitude = null;
    JSONArray  aryEmpTitulo       = null;
    Context cnt = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        cnt = inflater.getContext();
        Msg.showMessage(cnt, "Aviso", "Não há empreendimentos cadastrados.");
        mMap.addMarker(new MarkerOptions().position(LOBIZ).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOBIZ, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        new TheTask().execute("http://192.168.1.60:8080/lista-empreendimentos");
        return inflater.inflate(R.layout.activity_map, container, false);
    }


    class TheTask extends AsyncTask<String,String,String>
    {

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // update textview here
            if ("lista-vazia".equals(result)){
                Msg.showMessage(cnt, "Aviso", "Não há empreendimentos cadastrados.");
//                MapActivity.this.setContentView(R.layout.activity_login);
//                MapActivity.this.onCreate(null);
//		this.cancel(false);
            }
            else {
                try {
                    aryEmpreendimentos  = new JSONArray(result);
//                    aryEmpreendimentos  = objEmpreendimentos.getJSONArray("id");
//                    aryEmpGPSLatitude   = objEmpreendimentos.getJSONArray("gps_latitude");
//                    aryEmpGPSLongitude  = objEmpreendimentos.getJSONArray("gps_longitude");
//                    aryEmpTitulo        = objEmpreendimentos.getJSONArray("titulo");
                    for (int i=0; i < aryEmpreendimentos.length(); i++){
                        objEmpreendimentos = aryEmpreendimentos.getJSONObject(i);
                        Double lat         = objEmpreendimentos.getDouble("gps_latitude");
                        Double lng         = objEmpreendimentos.getDouble("gps_longitude");
                        LatLng pos         = new LatLng(lat, lng);
                        String titulo      = objEmpreendimentos.getString("titulo");
                        mMap.addMarker(new MarkerOptions().position(pos).title(titulo));
                    }
//                    MapActivity.this.setContentView(R.layout.activity_map);
//                Intent i = new Intent(MapActivity.this, HomeActivity.class);
//                i.putExtra("DATA", result);
//                startActivityForResult(i, 1);
//		this.cancel(false);
                }catch(Exception e){
                    Msg.showMessage(cnt, "Aviso", "Erro: "+e.getMessage());
                }
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            MapActivity.this.setContentView(R.layout.activity_loading);

        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
//                HttpClient httpclient = new DefaultHttpClient();
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//                pairs.add(new BasicNameValuePair("user", String.valueOf(edtUsuario.getText())));
//                pairs.add(new BasicNameValuePair("pass", String.valueOf(edtSenha.getText())));
//                pairs.add(new BasicNameValuePair("remember-me", "false")); //padrao false
//			pairs.add(new BasicNameValuePair("email", "testezin@email.com"));
//			pairs.add(new BasicNameValuePair("country", "Brazil"));
//			pairs.add(new BasicNameValuePair("user", "teste"));
//			pairs.add(new BasicNameValuePair("txtUser", "blanco"));
//			pairs.add(new BasicNameValuePair("txtPass", "1"));
//			pairs.add(new BasicNameValuePair("chkLembrar", "false"));
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
//                CloseableHttpResponse response = httpclient.execute(method);
                HttpResponse response = httpclient.execute(httppost);

//                try {
                HttpEntity entity = response.getEntity();
//                } finally {
//                    response.close();
//                }
                if(entity != null){
//	            Msg.showToast(cnt, EntityUtils.toString(entity));
//	            if ("invalid-password".equals(EntityUtils.toString(entity))){}
//	            	Msg.showToast(getApplicationContext(), "Usuário/senha inválido.");
//	            else{
//	            }
                    return EntityUtils.toString(entity);
                }
                else{
//	        	Msg.showToast(getApplicationContext(), "Erro desconhecido, tente novamente.");
                    return "No String";
                }
            } catch(Exception e){
//	    	 Msg.showToast(getApplicationContext(), "Problema na conexão, verifique sua conexão e tente novamente.");
                return "Network problem";
            }
//            } finally {
//                httpclient.close();
//
//            }
        }
    }
}
