package jp.techacademy.yoshiyuki.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

    // カーソル宣言
        val cursor = null
  //      val cursor = resolver.query(
  //      MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
  //      null, // 項目(null = 全項目)
  //        null, // フィルタ条件(null = フィルタなし)
  //      null, // フィルタ用パラメータ
  //      null // ソート (null ソートなし)
  //  )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        }



        start_button.setOnClickListener {
            getContentsInfo()
        }

        back_button.setOnClickListener {
            getContentsInfoback()
        }
 //       reset_button.text="test"
    }

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

        val resolver = contentResolver

        if (this.cursor.moveToNext()) {
            // 次に進む
            if(this.cursor.moveToNext()) {
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
        this.cursor.close()
    }

    private fun getContentsInfoback() {

        val resolver = contentResolver

        if (this.cursor!!.moveToPrevious()) {
            // ひとつ前に戻る
            this.cursor.moveToPrevious()
            val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = thsi.cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }else{
            //最後に戻る
            this.cursor.moveToLast()
            val fieldIndex = this.cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = this.cursor.getLong(fieldIndex)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        }
        this.cursor.close()
    }
}