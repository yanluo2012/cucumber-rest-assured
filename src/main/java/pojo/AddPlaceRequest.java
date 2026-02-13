package pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * POJO (Plain Old Java Object) class that models the request body structure for Add Place endpoint
 */
public class AddPlaceRequest {
    public Location location;
    public int accuracy;
    public String name;
    @JsonProperty("phone_number")
    public String phoneNumber;
    public String address;
    public List<String> types;
    public String website;
    public String language;
    public static class Location {
        public double lat;
        public double lng;
    }
}
