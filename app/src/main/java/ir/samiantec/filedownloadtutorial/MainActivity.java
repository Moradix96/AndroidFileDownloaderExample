package ir.samiantec.filedownloadtutorial;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    final String URL = "https://test.tld/a.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(view -> new RetrivePDFfromUrl().execute(URL));
    }

    private class RetriveFilefromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            //We are using inputstream for getting out PDF.
            InputStream inputStream = null;
            try {
                java.net.URL url = new URL(strings[0]);
                //Below is the step where we are creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    //Response is success.
                    //We are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }


                try {
                    File file = new File(getCacheDir(), "cacheFileAppeal.srl");
                    try (OutputStream output = new FileOutputStream(file)) {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }

                        output.flush();
                    }
                } finally {
                    inputStream.close();
                }



            } catch (IOException e) {
                //This is the method to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            //After the execution of our async task we are loading our pdf in our pdf view.
            //pdfView.fromStream(inputStream).load();

            File file = new File(getCacheDir(), "cacheFileAppeal.srl");

            Log.e("XXXXXX", file.toURI().toString());
            Log.e("XXXXXX", file.length()+"");

        }

    }
}