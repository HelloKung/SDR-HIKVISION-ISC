package com.sdr.lib.isc;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HyFun on 2019/10/21.
 * Email: 775183940@qq.com
 * Description:
 */
interface ISCApi {
    /**
     * 获取摄像头列表
     *
     * @return
     */
    @GET("hkIsc/cameras")
    Observable<Entity.BaseData<Entity.Resource>> getResourceList();


    /**
     * ISC转发接口
     *
     * @param url
     * @param json
     * @return
     */
    @GET("hkIscTransform")
    Observable<Entity.HIKBaseData<Entity.Preview>> getCameraUrl(@Query("url") String url, @Query("jsonStr") String json);

}
