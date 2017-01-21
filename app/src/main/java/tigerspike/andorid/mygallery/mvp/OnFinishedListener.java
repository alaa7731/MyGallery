package tigerspike.andorid.mygallery.mvp;

import android.support.annotation.Nullable;

/**
 * Created by alaa.salah on 1/21/2017.
 */
public interface OnFinishedListener<T> {
    void onSuccess(@Nullable T data);

    void onFailure(String errorMessage);


}
