package com.sdr.lib.isc;

/**
 * Created by HyFun on 2019/10/21.
 * Email: 775183940@qq.com
 * Description:
 */
interface Interface {
    /**
     * 播放视频的监听
     */
    public interface HikIscPlayCallback {
        void onLoading(int position);

        void onHideLoading(int position);

        void onPlaySuccess(int position, String message);

        void onPlayFailed(int position, String message);
    }


    /**
     * 播放状态
     */
    public enum PlayerStatus {
        IDLE,//闲置状态
        LOADING,//加载中状态
        SUCCESS,//播放成功
        STOPPING,//暂时停止播放
        FAILED,//播放失败
        EXCEPTION,//播放过程中出现异常
        FINISH//回放结束
    }


}
