package com.rizky.submission.last.moviecatalogue.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rizky.submission.last.moviecatalogue.MainActivity
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.helper.FunctionHelper.getCurrentDate
import com.rizky.submission.last.moviecatalogue.ui.movie.MovieItems
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RemainderReceiver : BroadcastReceiver() {

    companion object {
        const val DAILY_REMAINDER = "Daily Remainder"
        const val DAILY_REMAINDER_TIME = "07:00"
        const val RELEASE_REMAINDER = "Release Remainder"
        const val RELEASE_REMAINDER_TIME = "08:00"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        private const val ID_DAILY_REMAINDER = 100
        private const val ID_RELEASE_REMAINDER = 101
        private const val DAILY_NOTIFICATION_ID = 1
        private const val RELEASE_NOTIFICATION_ID = 2
        private const val DAILY_CHANNEL_ID = "channel_01"
        private const val RELEASE_CHANNEL_ID = "channel_02"
        private const val DAILY_CHANNEL_NAME = "daily_remainder"
        private const val RELEASE_CHANNEL_NAME = "release_remainder"
        private const val TIME_FORMAT = "HH:mm"
        private const val API_KEY = "ad17726d6285f718d265aa2de12213bb"
        private const val GROUP_MOVIE_RELEASE = "group_movie_release"
    }

    override fun onReceive(context: Context, intent: Intent) {


        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = if (type.equals(DAILY_REMAINDER, ignoreCase = true)) DAILY_REMAINDER else RELEASE_REMAINDER
        val id = if (type.equals(DAILY_REMAINDER, ignoreCase = true)) ID_DAILY_REMAINDER else ID_RELEASE_REMAINDER
        showToast(context, title, message)

        if (id == ID_DAILY_REMAINDER) {
            showDailyNotification(context)
        } else {
            getMovieReleaseToday(context)
        }
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }

    private fun isDateInvalid(date: String): Boolean {
        return try {
            val df = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    fun setRemainder(context: Context, type: String, message: String) {
        val idRemainder = if (type.equals(DAILY_REMAINDER, ignoreCase = true)) ID_DAILY_REMAINDER else ID_RELEASE_REMAINDER
        val time = if (type.equals(DAILY_REMAINDER, ignoreCase = true)) DAILY_REMAINDER_TIME else RELEASE_REMAINDER_TIME
        if (isDateInvalid(time)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RemainderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, idRemainder, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        val toastMessage: String = if (type.equals(DAILY_REMAINDER, ignoreCase = true)){
            context.resources.getString(R.string.daily_remainder_enabled)
        } else {
            context.resources.getString(R.string.release_remainder_enabled)
        }
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun cancelRemainder(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, RemainderReceiver::class.java)
        val requestCode = if (type.equals(DAILY_REMAINDER, ignoreCase = true)) ID_DAILY_REMAINDER else ID_RELEASE_REMAINDER
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        val message: String = if (type.equals(DAILY_REMAINDER, ignoreCase = true)){
            context.resources.getString(R.string.daily_remainder_disabled)
        } else {
            context.resources.getString(R.string.release_remainder_disabled)
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun isAlarmSet(context: Context, type: String): Boolean {
        val intent = Intent(context, RemainderReceiver::class.java)
        val requestCode = if (type.equals(DAILY_REMAINDER, ignoreCase = true)) ID_DAILY_REMAINDER else ID_RELEASE_REMAINDER

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }

    private fun showDailyNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, DAILY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(context.resources.getString(R.string.daily_remainder))
            .setSubText(context.resources.getString(R.string.daily_remainder))
            .setContentText(context.resources.getString(R.string.daily_remainder_content))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(DAILY_CHANNEL_ID,
                DAILY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(DAILY_CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(DAILY_NOTIFICATION_ID, notification)
    }

    private fun showReleaseNotification(context: Context, listMovie: ArrayList<MovieItems>) {
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_notifications_black_24dp)
        val mBuilder: NotificationCompat.Builder
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        if (listMovie.size < 2 ) {
            mBuilder = NotificationCompat.Builder(context, RELEASE_CHANNEL_ID)
                .setContentTitle("New movie Release")
                .setContentText(listMovie[0].title)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(largeIcon)
                .setGroup(GROUP_MOVIE_RELEASE)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine("Release today: " + listMovie[0].title)
                .addLine("Release today: " + listMovie[1].title)
                .addLine("others...")
                .setBigContentTitle("${listMovie.size} new movie release today")
                .setSummaryText(context.resources.getString(R.string.release_remainder))
            mBuilder = NotificationCompat.Builder(context, RELEASE_CHANNEL_ID)
                .setContentTitle("${listMovie.size} new movie release")
                .setContentText(context.resources.getString(R.string.release_remainder))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setGroup(GROUP_MOVIE_RELEASE)
                .setGroupSummary(true)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                RELEASE_CHANNEL_ID,
                RELEASE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            mBuilder.setChannelId(RELEASE_CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        notificationManagerCompat.notify(RELEASE_NOTIFICATION_ID, notification)
    }

    private fun getMovieReleaseToday(context: Context){
        val listMovie = ArrayList<MovieItems>()
        val currentDate = getCurrentDate()
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&primary_release_date.gte=$currentDate&primary_release_date.lte=$currentDate"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = MovieItems()

                        movieItems.id = movie.getInt("id")
                        movieItems.title = movie.getString("title")
                        movieItems.overview = movie.getString("overview")
                        movieItems.posterPath = movie.getString("poster_path")
                        movieItems.backdropPath = movie.getString("backdrop_path")
                        movieItems.releaseDate = movie.getString("release_date")
                        movieItems.popularity = movie.getInt("popularity")
                        movieItems.voteCount = movie.getInt("vote_count")
                        movieItems.voteAverage = movie.getInt("vote_average")
                        listMovie.add(movieItems)
                    }
                    if (listMovie.size > 0){
                        showReleaseNotification(context, listMovie)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })

    }
}
