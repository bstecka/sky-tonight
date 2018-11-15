package com.example.barbara.skytonight;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("JobScheduler", "onStartJob");
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return false;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText( getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT )
                    .show();
            jobFinished((JobParameters) msg.obj, false );
            return true;
        }
    } );

    private void scheduleRefresh() {
        JobScheduler mJobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder mJobBuilder = new JobInfo.Builder( 1, new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
        Log.e("JobSchedulerService", "refresh");
        /* For Android N and Upper Versions */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.e("Schedule refresh", "version N");
            JobInfo.Builder builder = new JobInfo.Builder( 2, new ComponentName(getPackageName(), JobSchedulerService.class.getName()))
                    .setMinimumLatency(3000);
            builder.build();
        }
        else {
            Log.e("Schedule refresh", "diff version");
        }
    }
}