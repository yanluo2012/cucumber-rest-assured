package resources;

import pojo.AddPlaceRequest;

import java.util.Arrays;

public class TestDataBuild {
    public AddPlaceRequest addPlacePayload(String name, String language, String address) {
        AddPlaceRequest addPlaceRequest = new AddPlaceRequest();
        AddPlaceRequest.Location location = new AddPlaceRequest.Location();
        location.lat = -38.383494;
        location.lng = 33.427362;
        addPlaceRequest.location = location;
        addPlaceRequest.accuracy = 50;
        addPlaceRequest.name = name;
        addPlaceRequest.phoneNumber = "(+91) 983 893 3937";
        addPlaceRequest.address = address;
        addPlaceRequest.types = Arrays.asList("shoe park", "shop");
        addPlaceRequest.website = "http://google.com";
        addPlaceRequest.language = language;
        return addPlaceRequest;
    }

    public String deletePlacePayload(String placeId) {
        return "{\r\n    \"place_id\": \"" + placeId + "\"\r\n}";
    }

}
