package com.example.chap10

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import com.example.chap10.databinding.ActivityMainBinding
import com.example.chap10.databinding.DialogInputBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var manager : NotificationManager
    private lateinit var builder : NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My Channel One Description"
            channel.setShowBadge(true)

            val uri:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)

            channel.enableLights(true)
            channel.lightColor = Color.RED

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, channelId)
        }else{
            builder = NotificationCompat.Builder(this)
        }


        binding.button1.setOnClickListener {
            val eventHandler = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    if(p1 == DialogInterface.BUTTON_POSITIVE){
                        Toast.makeText(applicationContext, "BUTTON_POSITIVE", Toast.LENGTH_SHORT).show()
                    }else if(p1 == DialogInterface.BUTTON_NEGATIVE){
                        Toast.makeText(applicationContext, "BUTTON_NEGATIVE", Toast.LENGTH_SHORT).show()
                    }else if(p1 == DialogInterface.BUTTON_NEUTRAL){
                        Toast.makeText(applicationContext, "BUTTON_NEUTRAL", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            AlertDialog.Builder(this).run{
                setTitle("test dialog")
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("정말 종료하시겠습니까?")
                setPositiveButton("OK", eventHandler)
                setNegativeButton("Cancel", eventHandler)
                setNeutralButton("More", eventHandler)
                show()
            }
        }

        binding.button2.setOnClickListener {
            val items = arrayOf<String>("사과", "복숭아", "수박", "딸기")
            AlertDialog.Builder(this).run{
                setTitle("items test")
                setIcon(android.R.drawable.ic_dialog_info)
                setItems(items, object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Toast.makeText(applicationContext, "select ${items[p1]}", Toast.LENGTH_SHORT).show()
                    }
                })
                setPositiveButton("닫기", null)
                show()
            }
        }

        binding.button3.setOnClickListener {
            val items = arrayOf<String>("사과", "복숭아", "수박", "딸기")
            AlertDialog.Builder(this).run{
                setTitle("items test")
                setIcon(android.R.drawable.ic_dialog_info)
                setMultiChoiceItems(items, booleanArrayOf(true, false, true, false),
                    object:DialogInterface.OnMultiChoiceClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {
                            Toast.makeText(applicationContext, "select ${items[p1]} ${if(p2) "선택되었습니다" else "선택 해제되었습니다"}", Toast.LENGTH_SHORT).show()
                        }
                    })
                setPositiveButton("닫기", null)
                show()
            }
        }

        binding.button4.setOnClickListener {
            val items = arrayOf<String>("사과", "복숭아", "수박", "딸기")
            AlertDialog.Builder(this).run{
                setTitle("items test")
                setIcon(android.R.drawable.ic_dialog_info)
                setSingleChoiceItems(items, 1, object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Toast.makeText(applicationContext, "select ${items[p1]}", Toast.LENGTH_SHORT).show()
                    }
                })
                setPositiveButton("닫기", null)
                setCancelable(false)
                show()
            }.setCanceledOnTouchOutside(false)
        }

        binding.button5.setOnClickListener {

            val dialogBinding = DialogInputBinding.inflate(layoutInflater)
            AlertDialog.Builder(this).run{
                setTitle("Input")
                setView(dialogBinding.root)

                setPositiveButton("닫기", object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        TODO("Not yet implemented")
                    }

                })
                show()
            }
        }

        binding.button6.setOnClickListener {

            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            ringtone.play()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
//                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))

                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(500, 2000, 500, 500),
                                                    intArrayOf(0, 20, 0, 200), -1))
            }else{
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(500)
                vibrator.vibrate(longArrayOf(500, 1000, 500, 2000), -1)
            }
        }

        binding.button7.setOnClickListener {

            val player: MediaPlayer = MediaPlayer.create(this, R.raw.ring3)
            player.start()
        }

        binding.button8.setOnClickListener {
            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
            builder.setWhen(System.currentTimeMillis())
            builder.setContentTitle("Content Title")
            builder.setContentText("Content Message")
            builder.setAutoCancel(false)
            builder.setOngoing(true)

            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)

            val actionIntent = Intent(this, DetailActivity::class.java)
            val actionPendingIntent  = PendingIntent.getActivity(this, 11, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.stat_notify_more,
                    "Action",
                    actionPendingIntent
                ).build()
            )

            val KEY_TEXT_REPLY = "key_text_reply"
            var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run{
                setLabel("답장")
                build()
            }

            val replyIntent = Intent(this, ReplyReceiver::class.java)
            val replyPendingIntent  = PendingIntent.getBroadcast(this, 30, replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            builder.addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.stat_notify_more,
                    "Reply",
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )

            builder.setProgress(100, 0, false)

            val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.big)
            val bigStyle = NotificationCompat.BigPictureStyle()
            bigStyle.bigPicture(bigPicture)
            builder.setStyle(bigStyle)

            val bigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.bigText(resources.getString(R.string.long_text))
            builder.setStyle(bigTextStyle)

            val style = NotificationCompat.InboxStyle()
            style.addLine("1코스 - 수락.불암산코스")
            style.addLine("2코스 - 용마.아차산코스")
            style.addLine("3코스 - 고덕.일자산코스")
            style.addLine("4코스 - 대모.우면산코스")
            style.addLine("5코스 - 수리.천일산코스")
            builder.setStyle(style)

            val sender1: Person = Person.Builder()
                .setName("kkang")
                .setIcon(IconCompat.createWithResource(this, R.drawable.small))
                .build()

            val sender2: Person = Person.Builder()
                .setName("wily")
                .setIcon(IconCompat.createWithResource(this, R.drawable.send))
                .build()

            val message1 = NotificationCompat.MessagingStyle.Message(
                "hello",
                System.currentTimeMillis(),
                sender1
            )

            val message2 = NotificationCompat.MessagingStyle.Message(
                "world",
                System.currentTimeMillis(),
                sender2
            )

            val messageStyle = NotificationCompat.MessagingStyle(sender2)
                .addMessage(message1)
                .addMessage(message2)
            builder.setStyle(messageStyle)

            manager.notify(11, builder.build())

            thread {
                for(i in 1..100){
                    builder.setProgress(100, i, false)
                    manager.notify(11, builder.build())
                    SystemClock.sleep(100)
                }
            }
        }

        binding.button9.setOnClickListener {
            manager.cancel(11)
        }
    }
}