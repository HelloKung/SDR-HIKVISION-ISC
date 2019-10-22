package com.sdr.lib.isc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.sdr.lib.http.HttpClient;
import com.sdr.lib.rx.RxUtils;
import com.sdr.lib.ui.tree.TreeNode;
import com.sdr.lib.ui.tree.TreeNodeRecyclerAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by HyFun on 2019/10/21.
 * Email: 775183940@qq.com
 * Description:
 */
class Util {

    public static class RxUtils extends com.sdr.lib.rx.RxUtils {

        /**
         * 验证base data
         *
         * @param <T>
         * @return
         */
        public static <T> ObservableTransformer<Entity.BaseData<T>, T> baseData() {
            return new ObservableTransformer<Entity.BaseData<T>, T>() {
                @Override
                public ObservableSource<T> apply(Observable<Entity.BaseData<T>> upstream) {
                    return upstream.flatMap(new Function<Entity.BaseData<T>, ObservableSource<T>>() {
                        @Override
                        public ObservableSource<T> apply(Entity.BaseData<T> baseData) throws Exception {
                            if (baseData.getCode() == Entity.BaseData.SUCCESS) {
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


        /**
         * 转换成HIKBasedata的转换
         *
         * @param <T>
         */
        public static class RxTransformer<T> implements ObservableTransformer<ResponseBody, T> {

            @Override
            public ObservableSource<T> apply(Observable<ResponseBody> upstream) {
                return upstream.flatMap(new Function<ResponseBody, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(ResponseBody responseBody) throws Exception {
                        String json = responseBody.string();
                        if (TextUtils.isEmpty(json)) {
                            return Observable.error(new Exception("获取数据异常"));
                        } else {
                            T t = HttpClient.gson.fromJson(json, new TypeToken<T>() {
                            }.getType());
                            return RxUtils.createData(t);
                        }
                    }
                });
            }
        }


        /**
         * 验证base data
         *
         * @param <T>
         * @return
         */
        public static <T> ObservableTransformer<Entity.HIKBaseData<T>, T> baseHKData() {
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

        /**
         * 验证base data
         *
         * @param <T>
         * @return
         */
        public static <T> ObservableTransformer<Entity.HIKBaseData<T>, Boolean> baseHKBoolean() {
            return new ObservableTransformer<Entity.HIKBaseData<T>, Boolean>() {
                @Override
                public ObservableSource<Boolean> apply(Observable<Entity.HIKBaseData<T>> upstream) {
                    return upstream.flatMap(new Function<Entity.HIKBaseData<T>, ObservableSource<Boolean>>() {
                        @Override
                        public ObservableSource<Boolean> apply(Entity.HIKBaseData<T> baseData) throws Exception {
                            if (baseData.getCode().equals(Entity.HIKBaseData.SUCCESS)) {
                                return RxUtils.createData(true);
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


    /**
     * 显示设备列表的dialog
     */
    public static class HKVideoPlayListDialog {
        private Context mContext;
        private List<TreeNode> mTreeNodeList;
        private TreeNodeRecyclerAdapter.OnTreeNodeSigleClickListener mOnTreeNodeSigleClickListener;

        public HKVideoPlayListDialog(Context context, List<TreeNode> treeNodeList, TreeNodeRecyclerAdapter.OnTreeNodeSigleClickListener onTreeNodeSigleClickListener) {
            mContext = context;
            mTreeNodeList = treeNodeList;
            mOnTreeNodeSigleClickListener = onTreeNodeSigleClickListener;
        }

        private MaterialDialog dialog;

        public void show() {
            try {
                RecyclerView recyclerView = new RecyclerView(mContext);
                TreeNodeRecyclerAdapter adapter = new TreeNodeRecyclerAdapter(mContext, mTreeNodeList, new TreeNodeRecyclerAdapter.OnTreeNodeSigleClickListener() {
                    @Override
                    public void onSigleClick(TreeNode treeNode, int visablePositon, int realDatasPositon, boolean isLeaf) {
                        if (isLeaf && dialog != null) {
                            dialog.dismiss();
                            if (mOnTreeNodeSigleClickListener != null)
                                mOnTreeNodeSigleClickListener.onSigleClick(treeNode, visablePositon, realDatasPositon, isLeaf);
                        }
                    }
                }, 2);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(adapter);
                dialog = new MaterialDialog.Builder(mContext)
                        .title("请选择监控点")
                        .customView(recyclerView, false)
                        .show();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 抓图路径格式：/storage/emulated/0/Android/data/com.hikvision.open.app/files/Pictures/_20180917151634445.jpg
     */
    public static String getCaptureImagePath(Context context) {
        File file = context.getExternalFilesDir(DIRECTORY_PICTURES);
        String path = file.getAbsolutePath() + File.separator + Util.getFileName() + ".jpg";
        return path;
    }


    /**
     * 录像路径格式：/storage/emulated/0/Android/data/com.hikvision.open.app/files/Movies/_20180917151636872.mp4
     */
    public static String getLocalRecordPath(Context context) {
        File file = context.getExternalFilesDir(DIRECTORY_MOVIES);
        String path = file.getAbsolutePath() + File.separator + Util.getFileName() + ".mp4";
        return path;
    }

    /**
     * 根据时间生成文件名称
     *
     * @return
     */
    public static String getFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }


    /**
     * 通知相册更新了
     */
    public static void notifyPhotoChanged(Context context, String filePath) {
        Uri uri = Uri.fromFile(new File(filePath));
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);
    }

}
