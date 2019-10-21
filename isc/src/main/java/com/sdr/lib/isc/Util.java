package com.sdr.lib.isc;

import android.text.TextUtils;

import com.sdr.lib.rx.RxUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * Created by HyFun on 2019/10/21.
 * Email: 775183940@qq.com
 * Description:
 */
class Util {
    /**
     * 验证base data
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Entity.HIKBaseData<T>, T> baseData() {
        return new ObservableTransformer<Entity.HIKBaseData<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Entity.HIKBaseData<T>> upstream) {
                return upstream.flatMap(new Function<Entity.HIKBaseData<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Entity.HIKBaseData<T> baseData) throws Exception {
                        if (baseData.getCode().equals(Entity.HIKBaseData.SUCCESS)) {
                            return RxUtils.createData(baseData.getData());
                        } else {
                            String error = baseData.getMsg();
                            error = TextUtils.isEmpty(error) ? "请求错误" : error;
                            return Observable.error(new Exception("错误：" + baseData.getCode() + "," + error));
                        }
                    }
                });
            }
        };
    }
}
