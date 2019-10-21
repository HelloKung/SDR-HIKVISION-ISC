package com.sdr.lib.isc;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdr.lib.ui.tree.TreeNode;

import java.util.List;

/**
 * Created by HyFun on 2019/10/21.
 * Email: 775183940@qq.com
 * Description:
 */
class HikvisionRecyclerAdapter extends BaseQuickAdapter<HKItemControl, BaseViewHolder> implements Interface.HikIscPlayCallback {

    public HikvisionRecyclerAdapter(int layoutResId, @Nullable List<HKItemControl> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HKItemControl item) {

    }

    // ————————————————————————————————————GET和SET——————————————————————————————————————————————————
    private List<TreeNode> treeNodeList;  // 摄像头树形列表

    public void setTreeNodeList(List<TreeNode> treeNodeList) {
        this.treeNodeList = treeNodeList;
    }

    private int lastPosition = -1;  // 最后一次点击的点

    public int getLastPosition() {
        return lastPosition;
    }



    // ————————————————————————————————————播放回调——————————————————————————————————————————————————
    @Override
    public void onLoading(int position) {

    }

    @Override
    public void onHideLoading(int position) {

    }

    @Override
    public void onPlaySuccess(int position, String message) {

    }

    @Override
    public void onPlayFailed(int position, String message) {

    }
}
