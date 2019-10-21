package com.sdr.lib.isc;

import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.view.TextureView;

import com.hikvision.open.hikvideoplayer.HikVideoPlayer;
import com.hikvision.open.hikvideoplayer.HikVideoPlayerCallback;
import com.hikvision.open.hikvideoplayer.HikVideoPlayerFactory;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxUtils;

import java.text.MessageFormat;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by HyFun on 2019/10/21.
 * Email: 775183940@qq.com
 * Description:
 */
class HKItemControl implements TextureView.SurfaceTextureListener, HikVideoPlayerCallback {

    private int position;   // 播放的位置
    private Interface.HikIscPlayCallback hikIscPlayCallback;

    public HKItemControl(int position, Interface.HikIscPlayCallback hikIscPlayCallback) {
        this.position = position;
        this.hikIscPlayCallback = hikIscPlayCallback;
        mPlayer = HikVideoPlayerFactory.provideHikVideoPlayer();
    }

    private Interface.PlayerStatus mPlayerStatus = Interface.PlayerStatus.IDLE;//默认闲置
    private HikVideoPlayer mPlayer;  // 播放器


    // ———————————————————————————————————————公共方法—————————————————————————————————————

    /**
     * 开始实时预览
     *
     * @param textureView
     */
    public void startPreview(String cameraIndex, final TextureView textureView) {
        if (mPlayerStatus == Interface.PlayerStatus.SUCCESS) {
            return;
        }
        mPlayerStatus = Interface.PlayerStatus.LOADING;
        this.textureView = textureView;
        this.textureView.setSurfaceTextureListener(this);
        // 开始获取播放url
        EntityRequest.PreviewRequest previewRequest = new EntityRequest.PreviewRequest();
        previewRequest.setCameraIndexCode(cameraIndex);

        HttpService.getService().getCameraUrl("/api/video/v1/cameras/previewURLs", HttpClient.gson.toJson(previewRequest))
                .compose(Util.<Entity.Preview>baseData())
                .flatMap(new Function<Entity.Preview, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Entity.Preview preview) throws Exception {
                        mPlayer.setSurfaceTexture(textureView.getSurfaceTexture());
                        return RxUtils.createData(mPlayer.startRealPlay(preview.getUrl(), HKItemControl.this));
                    }
                })
                .compose(RxUtils.<Boolean>io_main())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            // 说明播放失败
                            onPlayerStatus(Status.FAILED, mPlayer.getLastError());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hikIscPlayCallback.onPlayFailed(position, throwable.getMessage());
                    }
                });
    }


    /**
     * 正常关闭
     */
    public void stopPreview() {
        if (mPlayerStatus == Interface.PlayerStatus.SUCCESS) {
            mPlayerStatus = Interface.PlayerStatus.IDLE;//释放这个窗口
            mPlayer.stopPlay();
        }
    }

    // —————————————————————————————————————get 和 set———————————————————————————————————————————

    private TextureView textureView;  // 播放的界面


    /**
     * 获取当前所在的位置
     *
     * @return
     */
    public int getPosition() {
        return position;
    }

    // —————————————————————————————————————TextureView的声明周期———————————————————————————————————————————

    /**
     * 播放结果回调
     *
     * @param status    共四种状态：SUCCESS（播放成功）、FAILED（播放失败）、EXCEPTION（取流异常）、FINISH（回放结束）
     * @param errorCode 错误码，只有 FAILED 和 EXCEPTION 才有值
     */
    @Override
    public void onPlayerStatus(@NonNull final Status status, final int errorCode) {
        //TODO 注意: 由于 HikVideoPlayerCallback 是在子线程中进行回调的，所以一定要切换到主线程处理UI
        Observable.just(0)
                .compose(RxUtils.<Integer>io_main())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        switch (status) {
                            case SUCCESS:
                                //播放成功
                                mPlayerStatus = Interface.PlayerStatus.SUCCESS;
                                hikIscPlayCallback.onPlaySuccess(position, "播放成功");
                                textureView.setKeepScreenOn(true);//保持亮屏
                                break;
                            case FAILED:
                                //播放失败
                                mPlayerStatus = Interface.PlayerStatus.FAILED;
                                hikIscPlayCallback.onPlayFailed(position, MessageFormat.format("预览失败，错误码：{0}", Integer.toHexString(errorCode)));
                                break;
                            case EXCEPTION:
                                //取流异常 停止播放
                                mPlayerStatus = Interface.PlayerStatus.EXCEPTION;
                                mPlayer.stopPlay();//TODO 注意:异常时关闭取流
                                hikIscPlayCallback.onPlayFailed(position, MessageFormat.format("取流发生异常，错误码：{0}", Integer.toHexString(errorCode)));
                                break;
                        }
                    }
                });
    }


    // —————————————————————————————————————TextureView的声明周期———————————————————————————————————————————

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
