package com.binaa.company.server;

import android.content.Context;

import com.android.volley.Request;
import com.binaa.company.models.BaseModel;
import com.binaa.company.models.Car;
import com.binaa.company.models.Contact;
import com.binaa.company.models.Hotel;
import com.binaa.company.models.HotelOffer;
import com.binaa.company.models.Property;
import com.binaa.company.models.PropertyOffer;
import com.binaa.company.models.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static com.binaa.company.ReservationActivity.apartments;

/**
 * Created by Muhammad on 07/29/2017
 */
public abstract class ContentVolley extends BaseVolley {

    private final String url = Constants.getApiUrl();

    public ContentVolley(String TAG, Context context) {
        super(TAG, VolleySingleton.getInstance(context));
    }

    public void getProperties() {
        actionType = ActionType.GetApartments;
        requestAction(Request.Method.POST, url + "apartments", false);
    }

    public void getHotels(String priceFrom, String priceTo, String govId, String cityId) {
        actionType = ActionType.GetHotels;
        params = new HashMap<>();
        params.put("price_from", priceFrom);
        params.put("price_to", priceTo);
        params.put("governorate_id", govId);
        params.put("city_id", cityId);
        params.put("number_of_bedrooms", "");

        requestAction(Request.Method.POST, url + "hotels", false);
    }

    public void getCities() {
        actionType = ActionType.getCities;
        requestAction(Request.Method.GET, url + "all/cities", false);
    }

    public void getHotelOffers() {
        actionType = ActionType.getHotelOffers;
        requestAction(Request.Method.GET, url + "all/offers", false);
    }

    public void getPropertyOffers() {
        actionType = ActionType.getPropertyOffers;
        requestAction(Request.Method.GET, url + "all/offers", false);
    }

    public void getGovs() {
        actionType = ActionType.getGovs;
        requestAction(Request.Method.GET, url + "all/governorates", false);
    }

    public void getCars() {
        actionType = ActionType.GetCars;
        requestAction(Request.Method.POST, url + "cars", false);
    }

    public void getServices() {
        actionType = ActionType.GetServices;
        requestAction(Request.Method.POST, url + "services", false);
    }

    public void getContacts() {
        actionType = ActionType.getContacts;
        requestAction(Request.Method.POST, url + "contact", false);
    }

    public void getAbout() {
        actionType = ActionType.about;
        requestAction(Request.Method.POST, url + "about", false);
    }

    public void getPropertyDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");

        actionType = ActionType.GetPropertyDetails;
        requestAction(Request.Method.POST, url + "apartment/show", false);
    }

    public void getCarDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");

        actionType = ActionType.GetCarDetails;
        requestAction(Request.Method.POST, url + "car/show", false);
    }

    public void getHotelDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");

        actionType = ActionType.GetHotelDetails;
        requestAction(Request.Method.POST, url + "hotel/show", false);
    }

    public void getServiceDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");

        actionType = ActionType.GetServiceDetails;
        requestAction(Request.Method.POST, url + "service/show", false);
    }

    public void search(String type, String minPrice, String maxPrice, String bedrooms) {
        params = new HashMap<>();
        params.put("type", type);
        params.put("min_price", minPrice);
        params.put("max_price", maxPrice);
        params.put("number_of_bedrooms", bedrooms);

        if (type.equals(apartments)) {
            actionType = ActionType.searchProperties;
        } else {
            actionType = ActionType.searchHotels;
        }
        requestAction(Request.Method.POST, url + "search", false);
    }

    public void searchCars(String minPrice, String maxPrice) {
        params = new HashMap<>();
        params.put("min_price", minPrice);
        params.put("max_price", maxPrice);

        actionType = ActionType.searchCars;
        requestAction(Request.Method.POST, url + "car/search", false);
    }

    public void getRelatedApartments(int id) {
        params = new HashMap<>();
        params.put("id", id + "");

        actionType = ActionType.GetRelatedApartment;
        requestAction(Request.Method.POST, url + "related/apartments", false);
    }

    public void getRelatedHotels(int id) {
        params = new HashMap<>();
        params.put("id", id + "");

        actionType = ActionType.GetRelatedHotels;
        requestAction(Request.Method.POST, url + "related/hotels", false);
    }

    public void bookHotel(int hotel_id, String name, String email, String country, String passport, String arrival_date
            , String leave_date, int airport_pick, int car_rent, int travel_prog, String phone_number) {
        params = new HashMap<>();
        params.put("hotel_id", hotel_id + "");
        params.put("name", name);
        params.put("email", email);
        params.put("country", country);
        params.put("passport_number", passport);
        params.put("arrival_date", arrival_date);
        params.put("leave_date", leave_date);
        params.put("airport_pick", airport_pick + "");
        params.put("car_rent", car_rent + "");
        params.put("travel_prog", travel_prog + "");
        params.put("phone_number", phone_number);

        actionType = ActionType.bookHotel;
        requestAction(Request.Method.POST, url + "store/hotel/reservation", false);
    }

    public void bookApartment(int apartment_id, String name, String email, String country, String passport, String arrival_date
            , String leave_date, int airport_pick, int car_rent, int travel_prog, String phone_number) {
        params = new HashMap<>();
        params.put("apartment_id", apartment_id + "");
        params.put("name", name);
        params.put("email", email);
        params.put("country", country);
        params.put("passport_number", passport);
        params.put("arrival_date", arrival_date);
        params.put("leave_date", leave_date);
        params.put("airport_pick", airport_pick + "");
        params.put("car_rent", car_rent + "");
        params.put("travel_prog", travel_prog + "");
        params.put("phone_number", phone_number);

        actionType = ActionType.bookApartment;
        requestAction(Request.Method.POST, url + "store/apartment/reservation", false);
    }

    public void bookCar(int car_id, String name, String email, String country, String passport, String arrival_date
            , String leave_date, int driver, String phone_number) {
        params = new HashMap<>();
        params.put("car_id", car_id + "");
        params.put("name", name);
        params.put("email", email);
        params.put("country", country);
        params.put("passport_number", passport);
        params.put("arrival_date", arrival_date);
        params.put("leave_date", leave_date);
        params.put("airport_pick", driver + "");
        params.put("phone_number", phone_number);

        actionType = ActionType.bookCar;
        requestAction(Request.Method.POST, url + "store/car/reservation", false);
    }

    public void addRegistration(String token) {

        params = new HashMap<>();
        params.put("device_id", token);

        actionType = ActionType.addRegistration;

        requestAction(Request.Method.POST, url + "store/token", false);
    }

    @Override
    protected void onPreExecute(BaseVolley.ActionType actionType) {
        ActionType action = (ActionType) actionType;
        onPreExecute(action);
    }

    protected abstract void onPreExecute(ActionType actionType);

    @Override
    protected void getResponseParameters(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray dataArray;
        JSONObject dataObject;

        String message = jsonObject.optString("error", null);
        boolean success = message == null;
        if (success) {
            message = jsonObject.optString("success", "");
        }

        ActionType action = (ActionType) actionType;

        switch (action) {
            case GetApartments:
            case GetRelatedApartment:
            case searchProperties:
                ArrayList<Property> properties = new ArrayList<>();
                dataArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject propertyItem = dataArray.getJSONObject(i);
                    Property property = new Property(propertyItem);
                    properties.add(property);
                }

                Collections.sort(properties, new Comparator<Property>() {
                    @Override
                    public int compare(Property property1, Property property2) {
                        int code1 = Integer.parseInt(property1.getCode());
                        int code2 = Integer.parseInt(property2.getCode());
                        return code1 - code2;
                    }
                });

                onPostExecuteGetProperties(action, success, message, properties);
                break;
            case GetHotels:
            case searchHotels:
            case GetRelatedHotels:
                ArrayList<Hotel> hotels = new ArrayList<>();
                dataArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject hotelItem = dataArray.getJSONObject(i);
                    Hotel hotel = new Hotel(hotelItem);
                    hotels.add(hotel);
                }
//                Collections.sort(hotels, new Comparator<Hotel>() {
//                    @Override
//                    public int compare(Hotel hotel1, Hotel hotel2) {
//
//                        int code1 = Integer.parseInt(hotel1.getCode());
//                        int code2 = Integer.parseInt(hotel2.getCode());
//                        return code1 - code2;
//                    }
//                });

                onPostExecuteGetHotels(action, success, message, hotels);
                break;
            case GetPropertyDetails:
                dataObject = jsonObject.optJSONObject("data");
                Property property = new Property(dataObject);

                onPostExecuteGetPropertyDetails(action, success, message, property);
                break;
            case GetHotelDetails:
                dataObject = jsonObject.optJSONObject("data");
                Hotel hotel = new Hotel(dataObject);

                onPostExecuteGetHotelDetails(action, success, message, hotel);
                break;
            case searchCars:
            case GetCars:
                ArrayList<Car> cars = new ArrayList<>();
                dataArray = jsonObject.optJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject carItem = dataArray.getJSONObject(i);
                    Car car = new Car(carItem);
                    cars.add(car);
                }
                onPostExecuteGetCars(action, success, message, cars);
                break;
            case GetCarDetails:
                dataObject = jsonObject.optJSONObject("data");

                Car car = new Car(dataObject);
                onPostExecuteGetCarDetails(action, success, message, car);
                break;
            case GetServices:
                ArrayList<Service> services = new ArrayList<>();
                dataArray = jsonObject.optJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject serviceItem = dataArray.optJSONObject(i);
                    Service service = new Service(serviceItem);
                    services.add(service);
                }
                onPostExecuteGetServices(action, success, message, services);
                break;
            case GetServiceDetails:
                dataObject = jsonObject.optJSONObject("data");

                Service service = new Service(dataObject);
                onPostExecuteGetServiceDetails(action, success, message, service);
                break;
            case getContacts:
                dataObject = jsonObject.getJSONObject("data");

                Contact contact = new Contact(dataObject);
                onPostExecuteGetContacts(action, success, message, contact);
                break;
            case about:
                dataObject = jsonObject.getJSONObject("data");
                String about = dataObject.getString("about");

                onPostExecuteAbout(action, success, message, about);
                break;
            case getCities:
                dataArray = jsonObject.getJSONArray("data");
                ArrayList<BaseModel> cities = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    BaseModel city = new BaseModel(dataArray.getJSONObject(i));
                    cities.add(city);
                }
                onPostExecuteGetCities(action, success, message, cities);
                break;
            case getGovs:
                dataArray = jsonObject.getJSONArray("data");
                ArrayList<BaseModel> govs = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    BaseModel gov = new BaseModel(dataArray.getJSONObject(i));
                    govs.add(gov);
                }
                onPostExecuteGetGovs(action, success, message, govs);
                break;
            case getHotelOffers:
                dataArray = jsonObject.getJSONArray("hotelOffers");
                ArrayList<HotelOffer> hotelOffers = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    HotelOffer hotelOffer = new HotelOffer(dataArray.getJSONObject(i));
                    hotelOffers.add(hotelOffer);
                }
                onPostExecuteHotelOffers(action, success, message, hotelOffers);
                break;
            case getPropertyOffers:
                dataArray = jsonObject.getJSONArray("apartmentOffers");
                ArrayList<PropertyOffer> propertyOffers = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    PropertyOffer propertyOffer = new PropertyOffer(dataArray.getJSONObject(i));
                    propertyOffers.add(propertyOffer);
                }
                onPostExecutePropertyOffers(action, success, message, propertyOffers);
                break;
            case addRegistration:
            case bookCar:
            case bookHotel:
            case bookApartment:
                onPostExecute(action, success, message);
                break;
        }
    }

    @Override
    protected void onPostExecuteError(boolean success, String message, BaseVolley.ActionType actionType) {

        ActionType action = (ActionType) actionType;

        switch (action) {
            case GetApartments:
            case GetRelatedApartment:
            case searchProperties:
                onPostExecuteGetProperties(action, false, message, null);
                break;
            case GetHotels:
            case GetRelatedHotels:
            case searchHotels:
                onPostExecuteGetHotels(action, false, message, null);
                break;
            case GetPropertyDetails:
                onPostExecuteGetHotelDetails(action, false, message, null);
                break;
            case GetHotelDetails:
                onPostExecuteGetPropertyDetails(action, false, message, null);
                break;
            case GetCars:
            case searchCars:
                onPostExecuteGetCars(action, false, message, null);
                break;
            case GetCarDetails:
                onPostExecuteGetCarDetails(action, false, message, null);
                break;
            case GetServices:
                onPostExecuteGetServices(action, false, message, null);
                break;
            case GetServiceDetails:
                onPostExecuteGetServiceDetails(action, false, message, null);
                break;
            case getContacts:
                onPostExecuteGetContacts(action, false, message, null);
                break;
            case about:
                onPostExecuteAbout(action, success, message, null);
                break;
            case getGovs:
                onPostExecuteGetGovs(action, success, message, null);
                break;
            case getCities:
                onPostExecuteGetCities(action, success, message, null);
                break;
            case getHotelOffers:
                onPostExecuteHotelOffers(action, success, message, null);
                break;
            case getPropertyOffers:
                onPostExecutePropertyOffers(action, success, message, null);
                break;
            case addRegistration:
            case bookCar:
            case bookHotel:
            case bookApartment:
                onPostExecute(action, success, message);
                break;
        }
    }

    protected void onPostExecute(ActionType actionType, boolean success, String message) {
    }

    protected void onPostExecuteGetPropertyDetails(ActionType actionType, boolean success, String message, Property property) {
    }

    protected void onPostExecuteGetHotelDetails(ActionType actionType, boolean success, String message, Hotel hotel) {
    }

    protected void onPostExecuteGetCarDetails(ActionType actionType, boolean success, String message, Car car) {
    }

    protected void onPostExecuteGetServiceDetails(ActionType actionType, boolean success, String message, Service service) {
    }

    protected void onPostExecuteGetContacts(ActionType actionType, boolean success, String message, Contact contact) {
    }

    protected void onPostExecuteAbout(ActionType actionType, boolean success, String message, String about) {
    }

    protected void onPostExecuteGetProperties(ActionType actionType, boolean success, String message, ArrayList<Property> properties) {
    }

    protected void onPostExecuteGetHotels(ActionType actionType, boolean success, String message, ArrayList<Hotel> hotels) {
    }

    protected void onPostExecuteGetCars(ActionType actionType, boolean success, String message, ArrayList<Car> cars) {
    }

    protected void onPostExecuteGetServices(ActionType actionType, boolean success, String message, ArrayList<Service> services) {
    }

    protected void onPostExecuteGetCities(ActionType actionType, boolean success, String message, ArrayList<BaseModel> cities) {
    }

    protected void onPostExecuteGetGovs(ActionType actionType, boolean success, String message, ArrayList<BaseModel> govs) {
    }

    protected void onPostExecutePropertyOffers(ActionType actionType, boolean success, String message, ArrayList<PropertyOffer> propertyOffers) {
    }

    protected void onPostExecuteHotelOffers(ActionType actionType, boolean success, String message, ArrayList<HotelOffer> hotelOffers) {
    }

    public enum ActionType implements BaseVolley.ActionType {
        GetApartments, GetPropertyDetails, GetHotels, GetHotelDetails,
        GetCars, GetCarDetails, GetServices, GetServiceDetails, GetRelatedApartment,
        GetRelatedHotels, searchHotels, searchProperties, getContacts, about, addRegistration,
        searchCars, bookCar, bookHotel, bookApartment, getCities, getGovs, getHotelOffers, getPropertyOffers
    }
}
