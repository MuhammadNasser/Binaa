package com.binaa.android.binaa.server;

import android.content.Context;

import com.android.volley.Request;
import com.binaa.android.binaa.models.Car;
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

    @Override
    protected void onPreExecute(BaseVolley.ActionType actionType) {
        ActionType action = (ActionType) actionType;
        onPreExecute(action);
    }

    protected abstract void onPreExecute(ActionType actionType);

    @Override
    protected void getResponseParameters(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray dataArray = null;
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
        }
    }

    @Override
    protected void onPostExecuteError(boolean success, String message, BaseVolley.ActionType actionType) {

        ActionType action = (ActionType) actionType;

        switch (action) {
            case GetApartments:
            case GetHotels:
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
        }
    }

    protected void onPostExecuteGetPropertyDetails(ActionType actionType, boolean success, String message, Property property) {
    }

    protected void onPostExecuteGetCarDetails(ActionType actionType, boolean success, String message, Car car) {
    }

    protected void onPostExecuteGetServiceDetails(ActionType actionType, boolean success, String message, Service service) {
    }

    protected void onPostExecuteGetProperties(ActionType actionType, boolean success, String message, ArrayList<Property> properties) {
    }

    protected void onPostExecuteGetCars(ActionType actionType, boolean success, String message, ArrayList<Car> cars) {
    }

    protected void onPostExecuteGetServices(ActionType actionType, boolean success, String message, ArrayList<Service> services) {
    }

    public enum ActionType implements BaseVolley.ActionType {
        GetApartments, GetPropertyDetails, GetHotels, GetHotelDetails, GetCars, GetCarDetails, GetServices, GetServiceDetails
    }
}
