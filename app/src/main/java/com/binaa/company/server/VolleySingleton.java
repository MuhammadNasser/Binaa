package com.binaa.company.server;

/*
 * Created by Muhammad on 07/29/2017
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context context;
    private static final String TAG = VolleySingleton.class.getSimpleName();

    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * @param context context to create the instance of requestQue an imageLoader
     * @return returns a synchronized instance of this class to be a singleton class instance
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    /**
     * @return an instance of the request que to add any new volley requests to it
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    /**
     * @param req adds tis request to the request que with a default tag or the given tag if it's not empty
     * @param tag tag to add the request under
     * @param <T> generic type of request to be added
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * @param req adds tis request to the request que with a default tag
     * @param <T> generic type of request to be added
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(TAG));
        getRequestQueue().add(req);
    }

    /**
     * @param tag tag to cancel all pending requests under this tag
     */
    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
