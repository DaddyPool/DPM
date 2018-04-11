import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpResponcAsync extends AsyncTask<Void, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // doInBackground前処理
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection con = null;
        URL url = null;
        String urlSt = "http://sample.jp";

        try {
            // URLの作成
            url = new URL(urlSt);
            // 接続用HttpURLConnectionオブジェクト作成
            con = (HttpURLConnection)url.openConnection();
            // リクエストメソッドの設定
            con.setRequestMethod("POST");
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(false);
            // URL接続からデータを読み取る場合はtrue
            con.setDoInput(true);
            // URL接続にデータを書き込む場合はtrue
            con.setDoOutput(true);

            // 接続
            con.connect(); // ①
            // 本文の取得
            InputStream in = con.getInputStream();
            String readSt = readInputStream(in);


            JSONObject jsonData = new JSONObject(readSt).getJSONObject("オブジェクト名");
            // 配列を取得する場合
            JSONArray jsonArray = new JSONObject(readSt).getJSONArray("オブジェクト名");
            // String型の場合
            String st = jsonData.getString("オブジェクト名");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String readInputStream(InputStream in) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while((st = br.readLine()) != null)
        {
            sb.append(st);
        }
        try
        {
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // doInBackground後処理
    }

}
