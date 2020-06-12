package com.example.habittrainer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.habittrainer.db.HabitDbTable
import kotlinx.android.synthetic.main.activity_create_habit.tv_error
import kotlinx.android.synthetic.main.single_card_input.*
import java.io.IOException

class CreateHabitActivity : AppCompatActivity() {

    private val TAG = CreateHabitActivity::class.simpleName
    private val CHOOSE_IMAGE_REQUEST = 1
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_habit)
    }

    fun chooseImage(view: View) {
        // Let user choose image to illustrate habit
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val chooser = Intent.createChooser(intent, "Choose image for habit")
        // startActivityForResult allows us to get back the selected image
        startActivityForResult(chooser, CHOOSE_IMAGE_REQUEST)

        Log.d(TAG, "Intent to choose image was sent...")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that the result we are getting is for the choose image request
        if(requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
            Log.d(TAG, "An image was chosen by the user.")

            //iv_img.visibility = View.VISIBLE

            val uri = data.data
            uri?.let {
                val bitmap = tryReadBitmap(uri)
                bitmap?.let {
                    this.imageBitmap = bitmap
                    iv_img.setImageBitmap(bitmap)
                    Log.d(TAG, "Read bitmap and updated the image view.")
                }
            }
        }
    }

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try{
            if(Build.VERSION.SDK_INT < 28){
                MediaStore.Images.Media.getBitmap(contentResolver, data)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, data)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

    fun saveHabit(view: View) {
        // Check that all inputs have been completed
        if(et_title.isBlank() || et_description.isBlank()){
            Log.d(TAG, "No habit stored: title or description missing.")
            displayErrorMessage("Your habit needs an engaging title and description.")
            return
        } else if(imageBitmap == null){
            Log.d(TAG, "No habit stored: image is missing.")
            displayErrorMessage("Add a motivating picture to your habit.")
            return
        }

        // Store the habit to the database
        tv_error.visibility = View.INVISIBLE

        val title = et_title.text.toString()
        val description = et_description.text.toString()
        // Here we can use the unsafe call operator !! because we already checked for nullity before
        // Since we are not calling a method on it we cannot use the safe call operator ? (which would be better)
        val habit = Habit(title, description, imageBitmap!!)
        val id = HabitDbTable(this).store(habit)
        // Insert method returns -1L if something went wrong
        if(id == -1L){
            displayErrorMessage("Habit could not be stored... Let's not make this a habit.")
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayErrorMessage(s: String) {
        tv_error.text = s
        tv_error.visibility = View.VISIBLE
    }

    private fun EditText.isBlank() = this.text.toString().isBlank()
}

