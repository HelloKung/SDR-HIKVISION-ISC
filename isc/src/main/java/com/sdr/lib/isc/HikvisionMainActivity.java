package com.sdr.lib.isc;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.util.AlertUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HikvisionMainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private ImageView viewHistory;
    private ImageView viewZoomOut;

    private int currentViewNum = 2;  //当前的视图数
    private HikvisionRecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hikvision_main);
        setDisplayHomeAsUpEnabled();
        setTitle("实时监控");

        recyclerView = findViewById(R.id.hk_video_main_rv);
        radioGroup = findViewById(R.id.hk_video_main_rg_switch);
        viewHistory = findViewById(R.id.hk_video_main_iv_history);
        viewZoomOut = findViewById(R.id.hk_video_main_iv_zoom_out);

        initData();
        initListener();
    }

    private void initData() {
        changeRecyclerGrid(currentViewNum);
    }

    private void initListener() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            final int switchCount = i + 1;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentViewNum == switchCount) return;
                    changeRecyclerGrid(switchCount);
                }
            });
        }

        viewZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerAdapter == null) {
                    return;
                }
                int position = recyclerAdapter.getLastPosition();
                if (position == -1) {
                    AlertUtil.showNegativeToastTop("请选择一个正在播放的窗口");
                }
            }
        });
    }


    /**
     * 切换视图1*1 2*2 3*3 4*4
     */
    private void changeRecyclerGrid(int num) {
        if (recyclerAdapter == null) {
            List<HKItemControl> itemList = new ArrayList<>();
            recyclerAdapter = new HikvisionRecyclerAdapter(R.layout.hk_isc_layout_hkvideo_main_recycler_item, itemList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), num);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerAdapter.bindToRecyclerView(recyclerView);
            recyclerView.setAdapter(recyclerAdapter);
            for (int i = 0; i < num * num; i++) {
                itemList.add(new HKItemControl(i, recyclerAdapter));
            }
            recyclerAdapter.notifyDataSetChanged();
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanCount(num);
            if (num >= currentViewNum) {
                // 少 变  多
                int addCount = (num * num) - (currentViewNum * currentViewNum);
                for (int i = 0; i < addCount; i++) {
                    recyclerAdapter.addData(new HKItemControl(i + (currentViewNum * currentViewNum), recyclerAdapter));
                }
            } else {
                // 多 变 少
//                // 找出后边所有正在播放的窗口
//                List<HKItemControl> hkItemControlList = new ArrayList<>();
//                for (int i = (num * num); i < mainRecyclerAdapter.getData().size(); i++) {
//                    HKItemControl hkItemControl = mainRecyclerAdapter.getData().get(i);
//                    hkItemControlList.add(hkItemControl);
//                }
//                // 关闭播放
//                HKVideoUtil.closeAllPlayingVideo(hkItemControlList)
//                        .subscribe(new Consumer<Boolean>() {
//                            @Override
//                            public void accept(Boolean aBoolean) throws Exception {
//                                List<HKItemControl> hkItemControls = mainRecyclerAdapter.getData();
//                                Iterator<HKItemControl> iterator = hkItemControls.iterator();
//                                while (iterator.hasNext()) {
//                                    HKItemControl item = iterator.next();
//                                    if (item.getPosition() >= (num * num)) {
//                                        iterator.remove();
//                                        mainRecyclerAdapter.notifyItemRemoved(item.getPosition());
//                                    }
//                                }
//                            }
//                        });

                List<HKItemControl> hkItemControls = recyclerAdapter.getData();
                Iterator<HKItemControl> iterator = hkItemControls.iterator();
                while (iterator.hasNext()) {
                    HKItemControl item = iterator.next();
                    if (item.getPosition() >= (num * num)) {
                        iterator.remove();
                        recyclerAdapter.notifyItemRemoved(item.getPosition());
                    }
                }

            }

        }
        currentViewNum = num;
    }


}
