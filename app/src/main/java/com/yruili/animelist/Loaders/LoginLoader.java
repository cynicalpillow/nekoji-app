package com.yruili.animelist.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.yruili.animelist.Network.LoginUtil;

/**
 * Created by rui on 19/07/17.
 */

public class LoginLoader extends AsyncTaskLoader<Boolean> {
    private Boolean mData;
    private String code;
    public LoginLoader(Context context, String code) {
        super(context);
        this.code = code;
    }
    @Override
    public Boolean loadInBackground() {
        boolean result = LoginUtil.login(code);
        return result;
    }
    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/
    @Override
    public void deliverResult(Boolean data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            return;
        }
        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        mData = data;
        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }
    }
    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        // Begin monitoring the underlying data source.
        /*if (mObserver == null) {
            mObserver = new SampleObserver();
            // TODO: register the observer
        }*/

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }
    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }
    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            mData = null;
        }
        // The Loader is being reset, so we should stop monitoring for changes.
        /*if (mObserver != null) {
            // TODO: unregister the observer
            mObserver = null;
        }*/
    }
    @Override
    public void onCanceled(Boolean data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
    }
}
