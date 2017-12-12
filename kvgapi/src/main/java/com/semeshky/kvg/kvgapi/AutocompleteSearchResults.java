package com.semeshky.kvg.kvgapi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutocompleteSearchResults {
    private final List<AutocompleteSearchResult> mSearchResults;

    private AutocompleteSearchResults(Builder builder) {
        this.mSearchResults=builder.mSearchResults;
    }

    public List<AutocompleteSearchResult> getSearchResults() {
        return mSearchResults;
    }

    public static final class Builder{

        private List<AutocompleteSearchResult> mSearchResults=new ArrayList<>();
        public AutocompleteSearchResults build(){
            return new AutocompleteSearchResults(this);
        }

        public void add(AutocompleteSearchResult result) {
            this.mSearchResults.add(result);
        }
    }

    static final class Converter extends TypeAdapter<AutocompleteSearchResults> {

        private final TypeAdapter<AutocompleteSearchResult> mTypeAdapter;

        public Converter(Gson gson){
            this.mTypeAdapter=gson.getAdapter(AutocompleteSearchResult.class);
        }
        @Override
        public void write(JsonWriter out, AutocompleteSearchResults value) throws IOException {

        }

        @Override
        public AutocompleteSearchResults read(JsonReader in) throws IOException {
            if(in.peek()== JsonToken.NULL){
                in.skipValue();
                return null;
            }
            Builder builder=new Builder();
            in.beginArray();
            while(in.hasNext()){
                final AutocompleteSearchResult result= this.mTypeAdapter.read(in);
                if(result==null||result.getType()==AutocompleteSearchResult.TYPE_DIVIDER)
                    continue;
                builder.add(result);
            }
            in.endArray();
            return builder.build();
        }
    }
}
