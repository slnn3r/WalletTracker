package com.example.slnn3r.wallettrackermvp.JobService

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class theJobService: JobService() {

    private var jobCancelled:Boolean = false

    override fun onStartJob(params: JobParameters?): Boolean {

        Log.e("","JobStarted")

        doBackgroundWork(params!!)

        return true
    }

    private fun doBackgroundWork( params:JobParameters){

        // Push to Firebase data here

        Thread(Runnable {

            for(i in 0..9){

                if(jobCancelled){
                    return@Runnable
                }

                Log.e("","run:"+i)
                Thread.sleep(1000)
            }

            Log.e("","DONE")
            jobFinished(params, false)
        }).start()





    }


    override fun onStopJob(params: JobParameters?): Boolean {

        Log.e("","Job Cancel")
        jobCancelled=true
        return true

    }



}