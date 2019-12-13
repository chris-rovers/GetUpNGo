package com.example.getupngo

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_edit_user.*

class EditUserActivity : AppCompatActivity() {

    private var db: SQLiteDatabase? = null
    private val DB_NAME = "tasksDB"

    var userID = 0
    var name = ""
    var age = 0
    var grade = ""
    var teacher = ""
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val extras = intent.extras
        if (extras != null) {
            userID = extras.getInt("userID")!!
            name = extras.getString("userName")!!
        }

        done_button.setOnClickListener {
            val progressPage = Intent(this@EditUserActivity, ProgressActivity::class.java)
            var userBundle = Bundle()
            userBundle.putInt("userID", userID)
            userBundle.putString("userName", name)
            progressPage.putExtras(userBundle)
            startActivity(progressPage)
        }

        getUserDetails()
        showUserDetails()
    } // onCreate()

    fun getUserDetails() {
        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        var c: Cursor? = db!!.rawQuery("SELECT * FROM tbl_User WHERE userName = '$name'", null)

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    userID = c.getInt(0)
                    name = c.getString(1)
                    age = c.getInt(2)
                    grade = c.getString(3)
                    teacher = c.getString(4)
                    status = c.getString(5)
                } while (c.moveToNext())
            }
        }

        db!!.close()
    } // getUserDetails()

    fun showUserDetails() {
        edit_name_text.setText(name)
        age_text.setText(age.toString())
        grade_text.setText(grade)
        teacher_text.setText(teacher)
        if(status == "inactive")
        {
            user_status.isChecked = true
        }
    } // showUserDetails()

    fun updateUser(v: View) {
        name = edit_name_text.getText().toString()
        age = age_text.getText().toString().toInt()
        grade = grade_text.getText().toString()
        teacher = teacher_text.getText().toString()

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        db!!.execSQL("UPDATE tbl_User SET userName = '$name', age = $age, grade = '$grade', teacher = '$teacher' WHERE userID = $userID")

        if(user_status.isChecked)
        {
            db!!.execSQL("UPDATE tbl_User SET userStatus = 'inactive' WHERE userID = $userID")
        }
        else
        {
            db!!.execSQL("UPDATE tbl_User SET userStatus = 'active' WHERE userID = $userID")
        }

        db!!.close()

        save_button.hideKeyboard()

        val toast = Toast.makeText(applicationContext, "User details updated.", Toast.LENGTH_SHORT)
        toast.show()

        getUserDetails()
        showUserDetails()
    } // updateUser()

    fun deleteUser(v: View) {
        val alertDialog = AlertDialog.Builder(this)
            //set icon
            .setIcon(android.R.drawable.ic_dialog_alert)
            //set prompt
            .setTitle("Are you sure you wish to delete this user?")
            //set warning
            .setMessage("The user will be permanently deleted. This cannot be undone.")
            // if "Yes"
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

                db!!.execSQL("DELETE FROM tbl_User WHERE userID = $userID")
                db!!.execSQL("DELETE FROM tbl_Progress WHERE userID = $userID")


                db!!.close()
                finish()
                val mainPage = Intent(this@EditUserActivity, MainActivity::class.java)
                startActivity(mainPage)
            })
            // if "No"
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(applicationContext, "Action canceled", Toast.LENGTH_LONG).show()
            })
            .show()
    } // deleteUser()

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    } // hideKeyboard()

} // class
