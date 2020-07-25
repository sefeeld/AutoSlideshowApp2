package jp.techacademy.yoshiyuki.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

    // カーソル宣言
    var cursor = null
  //      val cursor = resolver.query(
  //      MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
  //      null, // 項目(null = 全項目)
  //      null, // フィルタ条件(null = フィルタなし)
  //      null, // フィルタ用パラメータ
  //      null // ソート (null ソートなし)
  //  )

    var mTimer: Timer? = null

    // タイマー用の時間のための変数
    var mTimerSec = 0.0

    var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
            getContentsInfo()
        }



        start_button.setOnClickListener {
            getContentsInfo_next()
        }

        back_button.setOnClickListener {
            getContentsInfo_back()
        }


        saisei_button.setOnClickListener {
            getContentsInfo_saisei()
        }

 //       saisei_button.setOnClickListener(this)
 //       teishi_button.setOnClickListener(this)


    }

 /*   再生停止の制御（案）
      override fun onClick(v: View) {
        if (v.id == R.id.saisei_button) {
            getContentsInfosaisei()
        } else if (v.id == R.id.teishi_button) {
            getContentsInfosaiteishi()
        }
    }  */

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start_button.setOnClickListener {
                        getContentsInfo()
                    }
                }
        }
    }

    private fun getContentsInfo() {

        // 画像の情報を取得する
        val resolver = contentResolver
        this.cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        ) as Nothing?

            // 最初の画像を表示する
            this.cursor.moveToFirst()
            val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = this.cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)

    }

    private fun getContentsInfo_next() {


            if(this.cursor.moveToNext()) {
                // 次に進む
                this.cursor.moveToNext()
                val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = this.cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }else{
                //最初に戻る
                this.cursor.moveToFirst()
                val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = this.cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

            }

    }

    private fun getContentsInfo_back() {

        if (this.cursor.moveToPrevious()) {
            // ひとつ前に戻る
            this.cursor.moveToPrevious()
            val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = this.cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }else{
            //最後に戻る
            this.cursor.moveToLast()
            val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = this.cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }
    }

    private fun getContentsInfo_saisei() {
        // タイマーの作成
        mTimer = Timer()

        // タイマーの始動
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mTimerSec += 0.1
                mHandler.post {
                    if (this.cursor!!.moveToFirst()) {
                        do {
                            // indexからIDを取得し、そのIDから画像のURIを取得する
                            val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = this.cursor.getLong(fieldIndex)
                            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                            imageView.setImageURI(imageUri)
                        } while (this.cursor.moveToNext())
                    }
                }
            }
        }, 100, 2000) // 最初に始動させるまで 2000ミリ秒、ループの間隔を 2000ミリ秒 に設定

        saisei_button.text="停止"
        saisei_button.id = "@+id/teishi_button"
        start_button.isClickable = false
        back_button.isClickable = false
    }

    private fun getContentsInfo_teishi() {

        mTimer!!.cancel()

        saisei_button.text="再生"
        saisei_button.id = "@+id/saisei_button"
        start_button.isClickable = true
        back_button.isClickable = true
    }


}