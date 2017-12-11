package com.semeshky.kvg.kvgapi;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KvgApiService {
    String PATH_STATION_LOCATIONS = "geoserviceDispatcher/services/stopinfo/stops";
    String PATH_PATH_INFO_BY_TRIP_ID = "geoserviceDispatcher/services/pathinfo/trip";
    String PATH_PATH_INFO_BY_ROUTE_ID = "geoserviceDispatcher/services/pathinfo/route";

    /**
     * @param stop
     * @param mode
     * @return
     */
    @FormUrlEncoded
    @POST("services/passageInfo/stopPassages/stop")
    Call<Station> getStation(@Field("stop") String stop,
                             @Field("mode") String mode);

    /**
     * @param stop
     * @param mode
     * @param routeId
     * @param authority
     * @param direction
     * @return
     */
    @FormUrlEncoded
    @POST("services/passageInfo/stopPassages/stop")
    Call<Station> getStationWithRoute(@Field("stop") String stop,
                                      @Field("mode") String mode,
                                      @Field("routeId") String routeId,
                                      @Field("authority") String authority,
                                      @Field("direction") String direction);

    /**
     * @param query
     * @return
     */
    @FormUrlEncoded
    @POST("services/lookup/autocomplete")
    Call<List<AutocompleteSearchResult>> getAutocomplete(@Field("query") String query);

    /**
     * @param tripId
     * @param vehicleId
     * @param mode
     * @return
     */
    @FormUrlEncoded
    @POST("services/tripInfo/tripPassages")
    Call<TripPassages> getTripPassages(@Field("tripId") String tripId, @Field("vehicleId") String vehicleId, @Field("mode") String mode);

    /**
     * @param tripId
     * @param mode
     * @return
     */
    @FormUrlEncoded
    @POST("services/tripInfo/tripPassages")
    Call<TripPassages> getTripPassages(@Field("tripId") String tripId, @Field("mode") String mode);

    /**
     * @return
     */
    @FormUrlEncoded
    @POST("services/lookup/stopsByCharacter?character=")
    Call<StopsByCharacterResult> getAllStops();

    @FormUrlEncoded
    @POST("services/routeInfo/routeStops")
    Call<StopsByCharacterResult> getRouteStops(@Field("routeId") String routeId);

    /**
     * @param character
     * @return
     */
    @FormUrlEncoded
    @POST("services/lookup/stopsByCharacter")
    Call<StopsByCharacterResult> getStopsByCharacter(@Query("character") String character);

    /**
     * @param search
     * @return
     */
    @FormUrlEncoded
    @POST("services/lookup/fulltext")
    Call<FulltextSearch> getStopsByName(@Field("search") String search);

    @GET("geoserviceDispatcher/services/vehicleinfo/vehicles?positionType=CORRECTED&colorType=ROUTE")
    Call<VehicleLocations> getVehicleLocations();

    @GET(PATH_STATION_LOCATIONS + "?left=-648000000&bottom=-324000000&right=648000000&top=324000000")
    Call<StationLocations> getStationLocations();

    @GET("geoserviceDispatcher/services/stopinfo/stopPoints?left=-648000000&bottom=-324000000&right=648000000&top=324000000")
    Call<StopPoints> getStopPoints();

    @GET("geoserviceDispatcher/services/pathinfo/vehicle")
    Call<VehiclePathInfo> getPathInfoByVehicleId(@Query("id") final String vehicleId);

    @GET(PATH_PATH_INFO_BY_TRIP_ID)
    Call<VehiclePathInfo> getPathInfoByTripId(@Query("id") final String tripId);

    @GET(PATH_PATH_INFO_BY_ROUTE_ID)
    Call<VehiclePathInfo> getPathInfoByRouteId(@Query("id") final String routeId);
}