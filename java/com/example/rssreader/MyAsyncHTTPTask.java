package com.example.rssreader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class MyAsyncHTTPSTask extends AsyncTask<String, Void, ArrayList<RSSItem>> {
    private final String TAG = "MyAsyncHTTPSTask";
    private MainActivity activity;
    private String str;

    public MyAsyncHTTPSTask(MainActivity activity)
    {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<RSSItem> doInBackground(String... urls) {
        try {
            str = urls[0];
            return getRSSItems(urls[0]);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d(TAG, "Parsing error");

        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<RSSItem> result) {
        super.onPostExecute(result);
        if (result != null)
        {
            activity.onLoadFinished(result);
            activity.saveToDb(result, str);
        }
        else{
            Toast.makeText(activity, "Rarsing error", Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    protected void onProgressUpdate() {
        super.onProgressUpdate();

    }*/

    private ArrayList<RSSItem> getRSSItems(String url)throws SAXException,
            IOException, ParserConfigurationException
    {
            ArrayList<RSSItem> items = new ArrayList<>();
            String title = "", link = "", description = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' hh.mm", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(url);
            document.getDocumentElement().normalize();
            NodeList itemList = document.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                String URL = "", extraURL = "";
                if (i > 10)
                    break;
                Element item = (Element) itemList.item(i);
                NodeList list = item.getElementsByTagName("link");
                if (list.getLength() > 0) {
                    Element linkElement = (Element) list.item(0);
                    link = linkElement.getTextContent();
                    ;
                }

                list = item.getElementsByTagName("title");
                if (list.getLength() > 0) {
                    Element titleElement = (Element) list.item(0);
                    title = titleElement.getTextContent();
                } else
                    title = "";

                list = item.getElementsByTagName("description");
                if (list.getLength() > 0) {
                    Element descriptionElement = (Element) list.item(0);
                    description = descriptionElement.getTextContent();


                    Pattern p = Pattern.compile("src=\".+?\"");
                    Matcher m = p.matcher(description);
                    if (m.find()) {
                        String str = m.group();
                        extraURL = str.substring(5, str.length() - 1);
                        Log.d(TAG, extraURL);
                    }


                    Pattern pattern = Pattern.compile("<.+?>");
                    Matcher matcher = pattern.matcher(description);
                    String newStr = matcher.replaceAll("");
                    description = newStr;
                } else
                    description = "";


                list = item.getElementsByTagName("enclosure");
                if (list.getLength() > 0) {
                    Element enclosureElement = (Element) list.item(0);
                    String type = enclosureElement.getAttribute("type");
                    if (type.equals("image/jpeg") || type.equals("image/png")) {
                        URL = enclosureElement.getAttribute("url");
                        items.add(new RSSItem(title, currentDate, description, link, URL));
                    } else {
                        if (extraURL.equals("")) {
                            items.add(new RSSItem(title, currentDate, description, link, ""));
                        } else
                            items.add(new RSSItem(title, currentDate, description, link, extraURL));
                    }
                } else {
                    if (extraURL.equals("")) {
                        items.add(new RSSItem(title, currentDate, description, link, ""));
                    } else
                        items.add(new RSSItem(title, currentDate, description, link, extraURL));
                }


            }
            return items;
    }

/*
    private String getContent(String path) throws IOException {
        URL url = new URL(path);
        BufferedReader reader = null;
        try {
            HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }*/
}
