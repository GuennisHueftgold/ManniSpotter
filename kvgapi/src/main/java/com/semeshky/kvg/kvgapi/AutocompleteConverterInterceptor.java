package com.semeshky.kvg.kvgapi;

import com.google.gson.stream.JsonWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.GzipSource;
import okio.Okio;
import timber.log.Timber;

public class AutocompleteConverterInterceptor implements Interceptor {
    private final static Pattern LI_MATCHER = Pattern.compile("<li.*</li>", Pattern.CASE_INSENSITIVE);
    private final static ByteString LI_OPEN_TAG = ByteString.encodeUtf8("<li");
    private final static ByteString LI_CLOSE_TAG = ByteString.encodeUtf8("</li>");

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final Response initialResponse = chain.proceed(request);
        if (initialResponse.code() != 200) {
            return initialResponse;
        }
        if (!request.method().equalsIgnoreCase("post")
                || !request.url().encodedPath().endsWith("lookup/autocomplete")) {
            return initialResponse;
        }

        BufferedSource source;
        if (initialResponse.header("Content-Encoding").equalsIgnoreCase("gzip")) {
            source = Okio.buffer(new GzipSource(initialResponse.body().source()));
        } else {
            source = initialResponse.body().source();
        }
        Document doc = getDomElement(source.inputStream());
        NodeList items = doc.getElementsByTagName("li");

        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"));
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.beginArray();
        for (int i = 0; i < items.getLength(); i++) {
            final String content = items.item(i).getTextContent();
            final Node stopShortName = items.item(i).getAttributes().getNamedItem("stop");
            if (content.equals("...") || stopShortName == null) {
                continue;
            }
            Timber.d("found " + stopShortName.getTextContent() + " - " + content);
            jsonWriter.beginObject();
            jsonWriter.name("shortName")
                    .value(stopShortName.getTextContent())
                    .name("name")
                    .value(content);
            jsonWriter.endObject();
            jsonWriter.flush();
        }
        jsonWriter.endArray();
        jsonWriter.flush();
        ResponseBody newResponseBody = ResponseBody.create(MediaType.parse("application/json"), buffer.size(), buffer);
        return initialResponse.newBuilder().body(newResponseBody).removeHeader("Content-Encoding").build();
    }

    private Document getDomElement(InputStream xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setCoalescing(true);
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setByteStream(xml);
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            return null;
        } catch (SAXException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return doc;

    }
}
