package com.semeshky.kvg.kvgapi;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

public final class StopsByCharacterResult {
    private final List<ShortStationInfo> mResults;

    private StopsByCharacterResult(Builder builder) {
        this.mResults=builder.mResults;
    }

    public List<ShortStationInfo> getResults() {
        return mResults;
    }

    public static class Builder{
        private List<ShortStationInfo> mResults;

        public List<ShortStationInfo> getResults() {
            return mResults;
        }

        public void setResults(List<ShortStationInfo> results) {
            mResults = results;
        }

        public StopsByCharacterResult build(){
            return new StopsByCharacterResult(this);
        }
    }

    public static class Converter extends TypeAdapter<StopsByCharacterResult>{


        private final TypeAdapter<List<ShortStationInfo>> mListConverter;
private final static String NAME_STOPS="stops";
        public Converter(Gson gson){
            this.mListConverter=gson.getAdapter(GeneralTypes.SHORT_STATION_INFO_LIST_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, StopsByCharacterResult value) throws IOException {

        }

        @Override
        public StopsByCharacterResult read(JsonReader in) throws IOException {
            if(in.peek()== JsonToken.NULL){
                in.skipValue();
                return null;
            }
            in.beginObject();
            String name;
            Builder builder=new Builder();
            while(in.hasNext()){
                name=in.nextName();
                if(NAME_STOPS.equalsIgnoreCase(name)
                        && in.peek()==JsonToken.BEGIN_OBJECT){
                    builder.setResults(this.mListConverter.read(in));
                }else{
                    in.skipValue();
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}
