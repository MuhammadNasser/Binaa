package com.binaa.company.server;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Muhammad on 07/29/2017
 */
public abstract class BaseVolley implements Response.ErrorListener, Response.Listener<String> {

    /**
     * action type performed by this instance
     */
    protected ActionType actionType;
    /**
     * parameters to be sent with post requests
     */
    protected HashMap<String, String> params;
    /**
     * tag to add all requests by this instance under
     */
    private String TAG;
    /**
     * instance of the volley singleton class to manage requests and requests ques
     */
    private VolleySingleton volleySingleton;
    /**
     * url of las api called by this instance of the class
     */
    private String methodUrl;

    /**
     * @param TAG             tag to add all requests by this instance under
     * @param volleySingleton instance of the volley singleton class to manage requests and requests ques
     */
    public BaseVolley(String TAG, VolleySingleton volleySingleton) {
        this.TAG = TAG;
        this.volleySingleton = volleySingleton;
    }

    /**
     * @param requestMethod type of request POST, GET, PUT, UPDATE, DELETE
     * @param methodUrl     url of the required api
     * @param shouldCache   indicated whether the result of this request should be cached or not
     *                      <p/>
     *                      method used to add formed requests to volley request queue this should
     *                      be called by subclass methods forming those requests one method for each api
     */
    protected void requestAction(int requestMethod, String methodUrl, boolean shouldCache) {

        onPreExecute(actionType);
        CustomStringRequest stringRequest = new CustomStringRequest(requestMethod, methodUrl, this, this) {
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                if (params != null && params.get("token") != null) {
                    headers.put("Authorization", "Bearer " + params.get("token"));
                }
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(shouldCache);
        if (shouldCache) {
            stringRequest.setCacheKeyParams(methodUrl + getParamsString(params));
        }
        this.methodUrl = methodUrl;
        volleySingleton.addToRequestQueue(stringRequest, TAG);
    }

    /**
     * @param volleyError error resulted from the request
     *                    <p/>
     *                    method check cache entry for this request if available it's returned
     *                    if not it calls error handling method
     */
    @Override
    public void onErrorResponse(VolleyError volleyError) {
        logResponse(volleyError + "");
        volleyError.printStackTrace();
        String data = getCache(methodUrl, params);
        if (data != null) {
            // handle data, like converting it to xml, json, bitmap, etc.
            onResponse(data);
        } else {
            String message;
            if (volleyError.networkResponse == null && volleyError instanceof NoConnectionError) {
                message = "No active internet connection";
            } else if (volleyError.networkResponse != null) {
                try {
                    message = volleyError.getMessage() + " " + volleyError.networkResponse.statusCode + " " + new String(volleyError.networkResponse.data, "UTF-8");
                } catch (Exception e) {
                    message = volleyError.toString();
                }
            } else {
                message = volleyError.toString();
            }

            onPostExecuteError(false, message, actionType);
        }
    }

    /**
     * @param response the string response returned by the server as reply to apis call
     *                 <p/>
     *                 method logs the response for developer and calls getResponseParameters which
     *                 is implemented by subclass to parse data from server response
     */
    @Override
    public void onResponse(String response) {
        logResponse(response);
        try {
            getResponseParameters(response);
        } catch (JSONException e) {
            e.printStackTrace();
            onPostExecuteError(false, e + "", actionType);
        }
    }

    private String getParamsString(HashMap<String, String> params) {
        String s = "";
        if (params != null) {
            Object[] keys = params.keySet().toArray();
            for (Object key : keys) {
                s += params.get(key + "");
            }
        }
        return s;
    }

    protected String getCache(String methodUrl, HashMap<String, String> params) {
        String data = null;
        Cache cache = volleySingleton.getRequestQueue().getCache();
        Cache.Entry entry = cache.get(methodUrl + getParamsString(params));
        if (entry != null) {
            try {
                data = new String(entry.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    private void logResponse(String jsonResponse) {
        jsonResponse = jsonResponse.replaceAll(",", ",\n\t");
        String TAG = "ServerLog" + " " + ((Enum) actionType).name();
        String response = ((Enum) actionType).name() + " ";
        if (params != null) {
            Object[] keys = params.keySet().toArray();
            for (Object key : keys) {
                response += " " + key + ":" + params.get(key + "");
            }
        }
        response += "\n" + jsonResponse;
        Log.d(TAG, response + "\n__________________________" +
                "___________________________________________" +
                "_____________________\n");
    }

    // overridden by inheriting class to take action in case response was recived
    protected abstract void onPreExecute(ActionType actionType);

    protected abstract void onPostExecuteError(boolean success, String message, ActionType actionType);

    protected abstract void getResponseParameters(String responseString) throws JSONException;

    /**
     * intreface implemented by subclasses to add all possible actions to be performed by that subclass
     */
    protected interface ActionType {
    }

    protected class CustomStringRequest extends StringRequest {

        private String cacheKeyParams = "";

        public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public CustomStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(url, listener, errorListener);
        }

        public void setCacheKeyParams(String cacheKeyParams) {
            this.cacheKeyParams = cacheKeyParams;
        }

        @Override
        public String getCacheKey() {
            return cacheKeyParams;
        }
    }
}
