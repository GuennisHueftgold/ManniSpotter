package com.semeshky.kvg.kvgapi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import timber.log.Timber;

public class AutocompleteSearchResult {
    public final static int TYPE_DIVIDER = 1, TYPE_STOP = 2, TYPE_UNKNOWN = 0;
    private final String mShortName;
    private final String mName;
    private final int mType;

    private AutocompleteSearchResult(Builder builder) {
        this.mShortName=builder.mShortName;
        this.mName=builder.mName;
        this.mType=builder.mType;
    }

    public int getType() {
        return mType;
    }

    public String getShortName() {
        return mShortName;
    }

    public String getName() {
        return mName;
    }

    public static final class Builder{
        private String mShortName;
        private String mName;
        private int mType=TYPE_UNKNOWN;

        public String getShortName() {
            return mShortName;
        }

        public void setShortName(String shortName) {
            mShortName = shortName;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public int getType() {
            return mType;
        }

        public void setType(int type) {
            mType = type;
        }

        public AutocompleteSearchResult build(){
            return new AutocompleteSearchResult(this);
        }
    }

    static final class Converter extends TypeAdapter<AutocompleteSearchResult> {

        private final static String NAME_NAME="name",NAME_ID="id",NAME_TYPE="type";
        @Override
        public void write(JsonWriter out, AutocompleteSearchResult value) throws IOException {

        }

        @Override
        public AutocompleteSearchResult read(JsonReader in) throws IOException {
            if(in.peek()== JsonToken.NULL){
                in.skipValue();
                return null;
            }
            Builder builder=new Builder();
            in.beginObject();
            String name;
            while(in.hasNext()) {
                name=in.nextName().toLowerCase();
                switch (name){
                    case NAME_NAME:
                        builder.setName(in.nextString());
                        break;
                    case NAME_ID:
                        builder.setShortName(in.nextString());
                        break;
                    case NAME_TYPE:
                        final String typeString=in.nextString().toLowerCase();
                        switch (typeString){
                            case "stop":
                                builder.setType(TYPE_STOP);
                                break;
                            case "divider":
                                builder.setType(TYPE_DIVIDER);
                                break;
                            default:
                                Timber.d("Unknown type %s",typeString);
                                break;
                        }
                        break;
                    default:
                        Timber.d("Unknown key %s with type: %s",name,in.peek().toString());
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}
