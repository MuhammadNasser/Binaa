package com.binaa.android.binaa.server;

import android.content.Context;

import com.android.volley.Request;
import com.binaa.android.binaa.models.Car;
import com.binaa.android.binaa.models.Contact;
import com.binaa.android.binaa.models.Property;
import com.binaa.android.binaa.models.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Muhammad on 07/29/2017
 */
public abstract class ContentVolley extends BaseVolley {

    private final String url = Constants.getApiUrl();
    private Context context;

    public ContentVolley(String TAG, Context context) {
        super(TAG, VolleySingleton.getInstance(context));
        this.context = context;
    }

    public void getProperties() {
        actionType = ActionType.GetApartments;
        requestAction(Request.Method.POST, url + "apartments", false);
    }

    public void getHotels() {
        actionType = ActionType.GetHotels;
        requestAction(Request.Method.POST, url + "hotels", false);
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

    public void getPropertyDetails(String id) {
        params = new HashMap<>();
        params.put("id", id);

        actionType = ActionType.GetPropertyDetails;
        requestAction(Request.Method.POST, url + "apartment/show", false);
    }

    public void getCarDetails(String id) {
        params = new HashMap<>();
        params.put("id", id);

        actionType = ActionType.GetCarDetails;
        requestAction(Request.Method.POST, url + "car/show", false);
    }

    public void getHotelDetails(String id) {
        params = new HashMap<>();
        params.put("id", id);

        actionType = ActionType.GetHotelDetails;
        requestAction(Request.Method.POST, url + "apartment/show", false);
    }

    public void getServiceDetails(String id) {
        params = new HashMap<>();
        params.put("id", id);

        actionType = ActionType.GetServiceDetails;
        requestAction(Request.Method.POST, url + "service/show", false);
    }

    public void search(String type, String minPrice, String maxPrice, String bedrooms) {
        params = new HashMap<>();
        params.put("type", type);
        params.put("min_price", minPrice);
        params.put("max_price", maxPrice);
        params.put("number_of_bedrooms", bedrooms);

        actionType = ActionType.search;
        requestAction(Request.Method.POST, url + "search", false);
    }

    public void getRelatedApartments(String id) {
        params = new HashMap<>();
        params.put("id", id);

        actionType = ActionType.GetRelatedApartment;
        requestAction(Request.Method.POST, url + "related/apartments", false);
    }

    public void getRelatedHotels(String id) {
        params = new HashMap<>();
        params.put("id", id);

        actionType = ActionType.GetRelatedHotels;
        requestAction(Request.Method.POST, url + "related/hotels", false);
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
            case GetHotels:
            case GetRelatedApartment:
            case GetRelatedHotels:
            case search:
                ArrayList<Property> properties = new ArrayList<>();
                dataArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject propertyItem = dataArray.getJSONObject(i);
                    Property property = new Property(propertyItem);
                    properties.add(property);
                }
                onPostExecuteGetProperties(action, success, message, properties);
                break;
            case GetPropertyDetails:
            case GetHotelDetails:
                dataObject = jsonObject.optJSONObject("data");
                Property property = new Property(dataObject);

                onPostExecuteGetPropertyDetails(action, success, message, property);
                break;
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
        }
    }

    @Override
    protected void onPostExecuteError(boolean success, String message, BaseVolley.ActionType actionType) {

        ActionType action = (ActionType) actionType;

        switch (action) {
            case GetApartments:
            case GetHotels:
            case GetRelatedApartment:
            case GetRelatedHotels:
            case search:
                onPostExecuteGetProperties(action, false, message, null);
                break;
            case GetPropertyDetails:
            case GetHotelDetails:
                onPostExecuteGetPropertyDetails(action, false, message, null);
                break;
            case GetCars:
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
        }
    }

    protected void onPostExecuteGetPropertyDetails(ActionType actionType, boolean success, String message, Property property) {
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

    protected void onPostExecuteGetCars(ActionType actionType, boolean success, String message, ArrayList<Car> cars) {
    }

    protected void onPostExecuteGetServices(ActionType actionType, boolean success, String message, ArrayList<Service> services) {
    }

    public enum ActionType implements BaseVolley.ActionType {
        GetApartments, GetPropertyDetails, GetHotels, GetHotelDetails,
        GetCars, GetCarDetails, GetServices, GetServiceDetails, GetRelatedApartment,
        GetRelatedHotels, search, getContacts, about
    }
}
