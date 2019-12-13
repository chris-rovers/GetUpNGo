package com.example.getupngo

import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import android.os.Bundle
import android.widget.*
import java.util.ArrayList
import kotlinx.android.synthetic.main.content_progress.*

class ProgressActivity : AppCompatActivity() {

    private var db: SQLiteDatabase? = null
    private val DB_NAME = "tasksDB"

    var userID = 0
    var userName = ""
    var catNames = ArrayList<String>()

    var cat1_items = ArrayList<String>()
    var cat1_statuses = ArrayList<String>()
    var cat1_ids = ArrayList<Int>()
    var cat2_items = ArrayList<String>()
    var cat2_statuses = ArrayList<String>()
    var cat2_ids = ArrayList<Int>()
    var cat3_items = ArrayList<String>()
    var cat3_statuses = ArrayList<String>()
    var cat3_ids = ArrayList<Int>()
    var cat4_items = ArrayList<String>()
    var cat4_statuses = ArrayList<String>()
    var cat4_ids = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val extras = intent.extras
        if (extras != null) {
            userID = extras.getInt("userID")!!
            userName = extras.getString("userName")!!
        }

        user_details_button.setOnClickListener {
            val editUserPage = Intent(this@ProgressActivity, EditUserActivity::class.java)
            var editUserBundle = Bundle()
            editUserBundle.putInt("userID", userID)
            editUserBundle.putString("userName", userName)
            editUserPage.putExtras(editUserBundle)
            startActivity(editUserPage)
        }

        home_button.setOnClickListener {
            val mainPage = Intent(this@ProgressActivity, MainActivity::class.java)
            startActivity(mainPage)
        }

        getProgressItems()
    } // onCreate()

    fun getProgressItems() {
        var curCat: String

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        var c: Cursor? = db!!.rawQuery("SELECT * FROM tbl_Progress WHERE userID = " + userID!!, null)

        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    curCat = c.getString(1)
                    if(catNames.contains(curCat)) {
                        // do nothing
                    } else {
                        catNames.add(curCat)
                    }
                } while (c.moveToNext())
            }
        }
        db!!.close()

        user_name_text.setText(userName)

        // Setup checklist item views
        // CATEGORY 1 ///////////////////
        cat1_items = getProgressItemsByCat(catNames[0])
        var cat_1_TextView = TextView(this)
        cat_1_TextView.setText(catNames[0])
        cat1_statuses = getItemStatusesByCat(catNames[0])
        cat1_ids = getItemIDsByCat(catNames[0])
        progress_layout.addView(cat_1_TextView)
        for(i in cat1_items.indices)
        {
            var cat_1_item = CheckBox(this)
            cat_1_item.setText(cat1_items[i])
            if(cat1_statuses[i] == "inactive")
            {
                cat_1_item.isChecked = true
                cat_1_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            else
            {
                cat_1_item.isChecked = false
                cat_1_item.setPaintFlags(0)
            }
            cat_1_item.setOnClickListener {
                if(cat_1_item.isChecked)
                {
                    cat_1_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                else
                {
                    cat_1_item.setPaintFlags(0)
                }
                markItemStatus(cat1_ids[i], cat_1_item.isChecked)
            }
            progress_layout.addView(cat_1_item)
        }

        // CATEGORY 2 ///////////////////
        cat2_items = getProgressItemsByCat(catNames[1])
        var cat_2_TextView = TextView(this)
        cat_2_TextView.setText(catNames[1])
        cat2_statuses = getItemStatusesByCat(catNames[1])
        cat2_ids = getItemIDsByCat(catNames[1])
        progress_layout.addView(cat_2_TextView)
        for(i in cat2_items.indices)
        {
            var cat_2_item = CheckBox(this)
            cat_2_item.setText(cat2_items[i])
            if(cat2_statuses[i] == "inactive")
            {
                cat_2_item.isChecked = true
                cat_2_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            }
            else
            {
                cat_2_item.setPaintFlags(0)
            }
            cat_2_item.setOnClickListener {
                if(cat_2_item.isChecked)
                {
                    cat_2_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                else
                {
                    cat_2_item.setPaintFlags(0)
                }
                markItemStatus(cat2_ids[i], cat_2_item.isChecked)
            }
            progress_layout.addView(cat_2_item)
        }

        // CATEGORY 3 ///////////////////
        cat3_items = getProgressItemsByCat(catNames[2])
        var cat_3_TextView = TextView(this)
        cat_3_TextView.setText(catNames[2])
        cat3_statuses = getItemStatusesByCat(catNames[2])
        cat3_ids = getItemIDsByCat(catNames[2])
        progress_layout.addView(cat_3_TextView)
        for(i in cat3_items.indices)
        {
            var cat_3_item = CheckBox(this)
            cat_3_item.setText(cat3_items[i])
            if(cat3_statuses[i] == "inactive")
            {
                cat_3_item.isChecked = true
                cat_3_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            }
            else
            {
                cat_3_item.setPaintFlags(0)
            }
            cat_3_item.setOnClickListener {
                if(cat_3_item.isChecked)
                {
                    cat_3_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                else
                {
                    cat_3_item.setPaintFlags(0)
                }
                markItemStatus(cat3_ids[i], cat_3_item.isChecked)
            }
            progress_layout.addView(cat_3_item)
        }

        // CATEGORY 4 ///////////////////
        cat4_items = getProgressItemsByCat(catNames[3])
        var cat_4_TextView = TextView(this)
        cat_4_TextView.setText(catNames[3])
        cat4_statuses = getItemStatusesByCat(catNames[3])
        cat4_ids = getItemIDsByCat(catNames[3])
        progress_layout.addView(cat_4_TextView)
        for(i in cat4_items.indices)
        {
            var cat_4_item = CheckBox(this)
            cat_4_item.setText(cat4_items[i])
            if(cat4_statuses[i] == "inactive")
            {
                cat_4_item.isChecked = true
                cat_4_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            }
            else
            {
                cat_4_item.setPaintFlags(0)
            }
            cat_4_item.setOnClickListener {
                if(cat_4_item.isChecked)
                {
                    cat_4_item.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                else
                {
                    cat_4_item.setPaintFlags(0)
                }
                markItemStatus(cat4_ids[i], cat_4_item.isChecked)
            }
            progress_layout.addView(cat_4_item)
        }
    } // getProgressItems()

    fun getProgressItemsByCat(cat_name: String): ArrayList<String> {
        val tempItems = ArrayList<String>()

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        val c = db!!.rawQuery("SELECT * FROM tbl_Progress WHERE catName = '$cat_name' AND userID = $userID", null)

        var curItem: String

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    curItem = c.getString(2)
                    tempItems.add(curItem)
                } while (c.moveToNext())
            }
        }
        db!!.close()
        return tempItems
    } // getProgressItemsByCat()

    fun getItemStatusesByCat(cat_name: String): ArrayList<String> {
        var tempStatuses = ArrayList<String>()

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        val c = db!!.rawQuery("SELECT * FROM tbl_Progress WHERE catName = '$cat_name' AND userID = $userID", null)

        var curStatus: String

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    curStatus = c.getString(3)
                    tempStatuses.add(curStatus)
                } while (c.moveToNext())
            }
        }
        db!!.close()
        return tempStatuses
    } // getItemStatusesByCat()

    fun getItemIDsByCat(cat_name: String): ArrayList<Int> {
        var tempIDs = ArrayList<Int>()

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        val c = db!!.rawQuery("SELECT * FROM tbl_Progress WHERE catName = '$cat_name' AND userID = $userID", null)

        var curID: Int

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    curID = c.getInt(0)
                    tempIDs.add(curID)
                } while (c.moveToNext())
            }
        }
        db!!.close()
        return tempIDs
    } // getItemIDsByCat()

    fun markItemStatus(progID: Int, checkedStatus: Boolean) {
        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        if(checkedStatus == true)
        {
            db!!.execSQL("UPDATE tbl_Progress SET itemStatus = 'inactive' WHERE progressID = $progID")
        }
        else
        {
            db!!.execSQL("UPDATE tbl_Progress SET itemStatus = 'active' WHERE progressID = $progID")
        }
    } // markItemStatus()

} // class
