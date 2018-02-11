package com.example.lukasz.wypozyczalnia;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.example.lukasz.wypozyczalnia.adapters.CategoryAdapter;
import com.example.lukasz.wypozyczalnia.models.AuthorModel;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.CategoryModel;
import com.example.lukasz.wypozyczalnia.models.LendDates;
import com.example.lukasz.wypozyczalnia.models.NewsModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Łukasz on 2016-12-28.
 */

public class RESTHelper extends AppCompatActivity {
    Context mContext;


    private static final String QUERY_URL = "http://192.168.0.251:3000/";

    //JSONArray mJsonArray = new JSONArray();

    public RESTHelper(Context context){
        mContext = context;

    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void getLend(final List<BookModel> books, final List<LendDates> dates, final ArrayAdapter adapter, int status, int user_id){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(QUERY_URL + "lend?user_id="+user_id+"&status_id="+status,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray mJsonArray) {
                        JSONObject objjs;
                        //String query="?isbn=-1&";
                        books.clear();
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            objjs = mJsonArray.optJSONObject(i);
                            Long isbn = new Long(0);
                            if (objjs.has("isbn")) {
                                isbn = objjs.optLong("isbn");

                                queryBooks("?isbn="+isbn, books, adapter);
                            }
                            Date lend = null;
                            Date term = null;
                            if (objjs.has("lend_date")) {
                                String dat = objjs.optString("lend_date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                if(dat.compareTo("")!=0) {
                                    try {
                                        lend = dateFormat.parse(dat);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (objjs.has("return_date")) {
                                String dat = objjs.optString("return_date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                if(dat.compareTo("")!=0) {
                                    try {
                                        term = dateFormat.parse(dat);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }

                            dates.add(new LendDates(lend, term));
                            adapter.notifyDataSetChanged();
                        }


                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });

    }

    public void addLend(final List<BookModel> books, final HashMap<Long, BookModel> inbin, final ArrayAdapter adapter, Context con){//final ProgressDialog mDialog) {

        final ProgressDialog mDialog = new ProgressDialog(con);
        mDialog.setMessage(mContext.getString(R.string.lending));
        mDialog.setCancelable(false);
        mDialog.show();
        final boolean good = true;
        for(final BookModel o : books) {
            try {
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("isbn", o.getIsbn().toString());
                jsonObject.put("user_id", ((StorageApp) con.getApplicationContext()).getUser_id());
                Date date = new Date();
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                String ldate = sd.format(date);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DAY_OF_MONTH, 14);
                String todate = sd.format(c.getTime());
                jsonObject.put("return_date", "");
                jsonObject.put("lend_date", "");
                jsonObject.put("status_id", "1");
                jsonObject.put("id", "");
                StringEntity json = new StringEntity(jsonObject.toString());


                client.post(mContext, QUERY_URL + "lend", json, "application/json", new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(mContext, R.string.succes, Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        books.remove(inbin.remove(o.getIsbn()));

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Toast.makeText(mContext, mContext.getString(R.string.failure) + "(" + statusCode + ")", Toast.LENGTH_LONG).show();

                        mDialog.dismiss();

                    }
                });

            } catch (JSONException e) {
                Log.d("oppppps", e.getLocalizedMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void getCategory(final List<CategoryModel> category, final CategoryAdapter adapter) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(QUERY_URL+"category",new JsonHttpResponseHandler() {
            @Override
            public void onSuccess ( int statusCode, Header[] headers, JSONArray mJsonArray){
                JSONObject objjs;
                //String query="?isbn=-1&";
                category.clear();
                int id=0;
                String name="";
                Bitmap imga= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_info_black_24dp);;
                for (int i = 0; i < mJsonArray.length(); i++) {

                    objjs=mJsonArray.optJSONObject(i);
                    if (objjs.has("image")) {
                        byte[] img = Base64.decode(objjs.optString("image"), Base64.DEFAULT);
                        imga = BitmapFactory.decodeByteArray(img, 0, img.length);

                    }
                    if (objjs.has("id")){
                        id = objjs.optInt("id");

                    }
                    if(Locale.getDefault().getLanguage().compareTo("pl")==0) {
                        if (objjs.has("name")) {
                            name = objjs.optString("name");
                        }
                    } else {
                        if (objjs.has("name_en")) {
                            name = objjs.optString("name_en");
                        }
                    }
                    category.add(new CategoryModel(name,id,imga));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure ( int statusCode, Header[] headers, Throwable
            throwable, JSONObject errorResponse){
                Log.e("omg android", statusCode + " " + throwable.getMessage());
            }
        });
    }

    private class getByteFromStream extends AsyncTask<String, Void, byte[]> {
        byte [] bits;
        @Override
        protected byte[] doInBackground(String... params) {
            InputStream is = null;
            try {
                is = (InputStream) new URL(params[0]).getContent();
                return IOUtils.toByteArray(is);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(byte[] result) {
            bits = result;
        }
    }

    public byte[] getByteFromStream(final String url) {
        try {
            return new getByteFromStream().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void populateProdukt(JSONArray mJsonArray, final List<BookModel> books, final ArrayAdapter adapter){

        Bitmap tmp = null;//BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cover);
        String bookTitle="";
        final String[] publisher = {"",""};
        final List<AuthorModel> authors = new ArrayList<>();
        String category = "";
        String description = "";

        int quantity = 0;
        Date date = new Date();
        Long isbn = new Long(0);
        //Log.d("sad", img.toString());
        JSONObject objjs;
        //books.clear();
        int size = books.size();
        final int max = size+mJsonArray.length()-1;

        for (int i = size; i < size+mJsonArray.length(); i++) {
            books.add(new BookModel());
            books.get(i).setImage(tmp);
            authors.clear();
            objjs = mJsonArray.optJSONObject(i-size);
            if (objjs.has("image")) {
                byte[] img = Base64.decode(objjs.optString("image"), Base64.DEFAULT) ;
                Bitmap imga = BitmapFactory.decodeByteArray(img, 0, img.length);
                books.get(i).setImage(imga);

            }

            if (objjs.has("isbn")) {
                books.get(i).setIsbn(objjs.optLong("isbn"));
                isbn =objjs.optLong("isbn");

            }

            if (objjs.has("quantity")) {
                books.get(i).setQuantity(objjs.optInt("quantity"));
            }
            if(Locale.getDefault().getLanguage().compareTo("pl")==0) {
                if (objjs.has("title")) {
                    books.get(i).setTitle(objjs.optString("title"));
                }
            } else {
                if (objjs.has("title_en")) {
                    books.get(i).setTitle(objjs.optString("title_en"));
                }
            }

            if (objjs.has("description")) {
                books.get(i).setDescription(objjs.optString("description"));
            }

            if (objjs.has("publisher_id")) {
                int publisher_id = objjs.optInt("publisher_id");
                AsyncHttpClient client = new AsyncHttpClient();

                final int finalI = i;
                client.get(QUERY_URL+"publisher/" + publisher_id,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject objjs) {
                                int id = 0;
                                String name = "";
                                if(Locale.getDefault().getLanguage().compareTo("pl")==0) {
                                    if (objjs.has("name")) {
                                        name = objjs.optString("name");
                                        books.get(finalI).setPublisher(name);
                                    }
                                } else {
                                    if (objjs.has("name_en")) {
                                        name = objjs.optString("name_en");
                                        books.get(finalI).setPublisher(name);
                                    }
                                }
                                if (objjs.has("id")) {
                                    id = objjs.optInt("id");
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                                Log.e("omg android", statusCode + " " + throwable.getMessage());
                            }
                        });
            }

            if (objjs.has("category_id")) {
                int cat = objjs.optInt("category_id");
                AsyncHttpClient client = new AsyncHttpClient();

                final int finalI = i;
                client.get(QUERY_URL+"category/" + cat,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject objjs) {
                                int id = 0;
                                String name = "";
                                if(Locale.getDefault().getLanguage().compareTo("pl")==0) {
                                    if (objjs.has("name")) {
                                        name = objjs.optString("name");
                                        books.get(finalI).setCategory(name);
                                    }
                                } else {
                                    if (objjs.has("name_en")) {
                                        name = objjs.optString("name_en");
                                        books.get(finalI).setCategory(name);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                                Log.e("omg android", statusCode + " " + throwable.getMessage());
                            }
                        });

            }
            
            if(objjs.has("add_date")){
                String dat = objjs.optString("add_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = dateFormat.parse(dat);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            AsyncHttpClient client = new AsyncHttpClient();

            final int finalI1 = i;
            client.get(QUERY_URL+"book_author?isbn=" + isbn,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray mJsonArray) {
                            JSONObject objjs;
                            String query="?id=-1";
                            for (int i = 0; i < mJsonArray.length(); i++) {
                                objjs = mJsonArray.optJSONObject(i);
                                if (objjs.has("author_id")) {
                                    query += "&id="+objjs.optInt("author_id");
                                }
                            }
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(QUERY_URL+"author" + query,
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                                            JSONObject objjs;
                                            final List<AuthorModel> authors = new ArrayList<>();
                                            String name="";
                                            String surname="";
                                            for(int i=0;i<array.length();i++) {
                                                objjs=array.optJSONObject(i);
                                                if (objjs.has("name")) {
                                                    name = objjs.optString("name");

                                                }
                                                if (objjs.has("surname")) {
                                                    surname = objjs.optString("surname");
                                                }
                                                authors.add(new AuthorModel(name, surname));
                                            }
                                            books.get(finalI1).setAuthors(authors);
                                            adapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                                            Log.e("omg android", statusCode + " " + throwable.getMessage());
                                        }
                                    });
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                            Log.e("omg android", statusCode + " " + throwable.getMessage());
                        }
                    });
            if(i==max||(i+1)%3==0)
                adapter.notifyDataSetChanged();
        }

        adapter.notifyDataSetChanged();

    }



    public void queryBooks(String searchString, final List news, final ArrayAdapter adapter) {

        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(QUERY_URL+"book" + urlString,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray mJsonArray) {

                        populateProdukt(mJsonArray, news, adapter);
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject mJsonArray) {
                        JSONArray arr = new JSONArray();
                        try {
                            mJsonArray.toJSONArray(arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        populateProdukt(arr, news, adapter);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    public void queryNews(int from, int to, final List news, final ArrayAdapter adapter) {
        String urlString = "";
        try {
            urlString = URLEncoder.encode("news/", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray mJsonArray) {
                        String title;
                        String desc;
                        Date date = new Date();
                        JSONObject objjs;
                        news.clear();
                        for (int i = 0; i < 10 && i < mJsonArray.length(); i++) {
                            objjs = mJsonArray.optJSONObject(i);
                            title = "";
                            desc = "";

                            if (objjs.has("title")) {
                                title = objjs.optString("title");
                            }

                            if (objjs.has("description")) {
                                desc = objjs.optString("description");
                            }

                            if(objjs.has("pub_date")){
                                String dat = objjs.optString("pub_date");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    date = dateFormat.parse(dat);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            

                            news.add(new NewsModel(title, desc, date));
                            news.get(0);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                        Log.d("eh", "onFailure: ");
                    }
                });
    }
}
