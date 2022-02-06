package com.example.ch15_service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ch15_outer.MyAIDLInterface
import com.example.ch15_service.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var connectionMode = "none"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateMessengerService()
        onCreateAIDLService()
        onCreateJobScheduler()
    }

    private fun changeViewEnable() = with(binding){
        when(connectionMode) {
            "messenger" -> {
                messengerPlay.isEnabled = false
                aidlPlay.isEnabled = false
                messengerStop.isEnabled = true
                aidlStop.isEnabled = false
            }
            "aidl" -> {
                messengerPlay.isEnabled = false
                aidlPlay.isEnabled = false
                messengerStop.isEnabled = false
                aidlStop.isEnabled = true
            }
            else -> {
                messengerPlay.isEnabled = true
                aidlPlay.isEnabled = true
                messengerStop.isEnabled = false
                aidlStop.isEnabled = false

                messengerProgress.progress = 0
                aidlProgress.progress = 0
            }
        }
    }

    //messenger connection ....................
    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger
    var messengerJob: Job? = null

    private fun onCreateMessengerService(){
        replyMessenger = Messenger(HandlerReplyMsg())
        binding.messengerPlay.setOnClickListener {
            val intent = Intent("ACTION_SERVICE_Messenger")
            intent.setPackage("com.example.ch15_outer")
            bindService(intent, messengerConnection, Context.BIND_AUTO_CREATE)
        }
        binding.messengerStop.setOnClickListener {
            val msg = Message()
            msg.what = 20
            messenger.send(msg)

            unbindService(messengerConnection)
            messengerJob?.cancel()

            connectionMode = "none"
            changeViewEnable()

            binding.messengerProgress.max = 0
        }
    }

    val messengerConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            Log.d("wily3", "onServiceConnected...")
            messenger = Messenger(service)

            val msg = Message()
            msg.replyTo = replyMessenger
            msg.what = 10
            messenger.send(msg)

            connectionMode = "messenger"
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("wily3", "onServiceDisconnected...")
        }
    }

    inner class HandlerReplyMsg : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                10->{
                    val bundle = msg.obj as Bundle
                    bundle.getInt("duration")?.let{
                        when{
                            it > 0 -> {
                                binding.messengerProgress.max = it

                                val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
                                messengerJob = backgroundScope.launch {
                                    while (binding.messengerProgress.progress < binding.messengerProgress.max){
                                        delay(1000)
                                        binding.messengerProgress.incrementSecondaryProgressBy(1000)
                                    }
                                }
                                changeViewEnable()
                            }
                            else -> {
                                connectionMode = "none"
                                unbindService(messengerConnection)
                                changeViewEnable()
                            }
                        }
                    }
                }
            }
        }
    }



    //aidl connection .......................

    var aidlService: MyAIDLInterface? = null
    var aidlJob: Job? = null

    private fun onCreateAIDLService(){
        binding.aidlPlay.setOnClickListener {
            val intent = Intent("ACTION_SERVICE_AIDL")
            intent.setPackage("com.example.ch15_outer")
            bindService(intent, aidlConnection, Context.BIND_AUTO_CREATE)
        }
        binding.aidlStop.setOnClickListener {
            aidlService!!.stop()
            unbindService(aidlConnection)
            aidlJob?.cancel()
            connectionMode="none"
            changeViewEnable()
        }
    }

    private val aidlConnection: ServiceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            aidlService = MyAIDLInterface.Stub.asInterface(service)
            aidlService!!.start()
            binding.aidlProgress.max = aidlService!!.maxDuration
            val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
            aidlJob = backgroundScope.launch {
                while(binding.aidlProgress.progress < binding.aidlProgress.max){
                    delay(1000)
                    binding.aidlProgress.incrementProgressBy(1000)
                }
            }
            connectionMode = "aidl"
            changeViewEnable()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            aidlService = null
        }
    }

    //JobScheduler

    private fun onCreateJobScheduler(){
        var jobScheduler: JobScheduler? = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val builder = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        val jobInfo = builder.build()
        jobScheduler!!.schedule(jobInfo)
    }


    //onStop
    override fun onStop() {
        super.onStop()
        if(connectionMode === "messenger"){
            onStopMessengerService()
        }else if(connectionMode === "aidl"){
            onStopAIDLService()
        }
        connectionMode = "none"
        changeViewEnable()
    }

    private fun onStopMessengerService(){
        val msg = Message()
        msg.what = 20
        messenger.send(msg)

        unbindService(messengerConnection)
    }

    private fun onStopAIDLService(){
        unbindService(aidlConnection)
    }
}