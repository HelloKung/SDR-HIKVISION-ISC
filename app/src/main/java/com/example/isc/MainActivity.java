package com.example.isc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sdr.lib.isc.SDR_HIKVISION_ISC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        SDR_HIKVISION_ISC.getInstance().start(this, "http://58.240.174.254:9059/mcs/", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbXBsb3llZUluZm8iOiJ7XCJlbXBsb3llZUlkXCI6XCJmZWY0MzI4MzZmZjM0OWRmYTUyYWYwYzZjODI3ZDU3NVwiLFwidXNlcklkXCI6XCJkZWY0MzI4MzZmZjM0OWRmYTUyYWYwYzZjODI3ZDU3NVwiLFwidXNlck5hbWVcIjpcImFkbWluXCIsXCJuYW1lXCI6XCLnrqHnkIblkZhcIixcImhlYWRJbWdcIjpcImh0dHA6Ly81OC4yNDAuMTc0LjI1NDo5MDU5L29yZy8vaW1hZ2UvMjAxOTA5MTkvNTk2ZGJkNTI2ODQ1NDE4LmpwZ1wiLFwic2V4XCI6bnVsbCxcInBob25lTnVtYmVyXCI6XCIxMzMzMzMzMzMzM1wiLFwic3RhdGVcIjoyLFwiZGVwYXJ0SWRcIjpcImRjNDU5ZmE2ZGVjNTQ2MTdhNGNmY2NiODUzZDRlZjExXCIsXCJqb2JJZHNcIjpcIicnXCIsXCJvcmdJZFwiOlwiNGNmODU4Mjg0ZDEyNGE3OGJiZmJhYWVmZTBlNTc5NmJcIixcImVtYWlsXCI6XCI2NjY2NkBxcS5jb21cIn0iLCJleHAiOjE1NzE3Nzk5MTR9.9_PSBVuas4IGA_AG6sCGqQPsX4XQnz836nFjoticO6I");
    }
}
