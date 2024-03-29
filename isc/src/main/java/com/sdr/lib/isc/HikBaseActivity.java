package com.sdr.lib.isc;

import com.sdr.lib.base.BaseActivity;
import com.sdr.lib.ui.dialog.SDRLoadingDialog;

/**
 * Created by HyFun on 2019/10/22.
 * Email: 775183940@qq.com
 * Description:
 */
class HikBaseActivity extends BaseActivity {

    private SDRLoadingDialog progressDialog;

    public void showLoadingDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new SDRLoadingDialog.Builder(getContext())
                    .blur(true)
                    .cancel(false)
                    .content(msg)
                    .build();
        }
        progressDialog.setContent(msg);
    }

    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
