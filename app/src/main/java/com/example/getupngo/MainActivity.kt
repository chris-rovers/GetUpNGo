package com.example.getupngo

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var db: SQLiteDatabase? = null
    private val DB_NAME = "tasksDB"

    var userIDs = ArrayList<Int>()
    var userNames = ArrayList<String>()
    var archivedUserIDs = ArrayList<Int>()
    var archivedUserNames = ArrayList<String>()
    var catIDs = ArrayList<Int>()
    var catNames = ArrayList<String>()
    var itemNames = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        show_archived.setOnClickListener {
            listUsers()
        }

        background.setOnClickListener {
            background.hideKeyboard()
        }

        dbSetup()
        listUsers()
    } // onCreate()

    fun dbSetup() {
        // create db
        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        // create User table
        db!!.execSQL("CREATE TABLE IF NOT EXISTS tbl_User (userID INTEGER PRIMARY KEY AUTOINCREMENT, userName VARCHAR, age INT, grade VARCHAR, teacher VARCHAR, userStatus VARCHAR);")

        // create Category table
        db!!.execSQL("CREATE TABLE IF NOT EXISTS tbl_Cat (catID INTEGER PRIMARY KEY AUTOINCREMENT, catName VARCHAR);")

        // create Item table
        db!!.execSQL("CREATE TABLE IF NOT EXISTS tbl_Item (itemID INTEGER PRIMARY KEY AUTOINCREMENT, itemName VARCHAR, catID INTEGER);")

        // create Progress table
        db!!.execSQL("CREATE TABLE IF NOT EXISTS tbl_Progress (progressID INTEGER PRIMARY KEY AUTOINCREMENT, catName VARCHAR, itemName VARCHAR, itemStatus VARCHAR, userID INTEGER);")

        val c = db!!.rawQuery("SELECT * FROM tbl_Cat", null)

        if (c != null)
        {
            if (c!!.getCount() <= 0)
            {
                // insert Categories
                db!!.execSQL("INSERT INTO tbl_Cat VALUES(?1, 'Nutrition');")
                db!!.execSQL("INSERT INTO tbl_Cat VALUES(?1, 'Hygiene');")
                db!!.execSQL("INSERT INTO tbl_Cat VALUES(?1, 'Academic');")
                db!!.execSQL("INSERT INTO tbl_Cat VALUES(?1, 'Logistic');")

                // insert Items
                // add items to Nutrition Category table
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Eat breakfast', 1);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Hydrate', 1);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Take vitamins', 1);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Pack lunch', 1);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Pack water bottle', 1);")

                // add items to Hygiene Category table
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Take a shower', 2);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Get dressed', 2);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Brush hair', 2);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Brush teeth', 2);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Put on deodorant', 2);")

                // add items to Academic Category table
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Homework done', 3);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Study for test', 3);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Pack homework', 3);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Pack text books', 3);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Pack school supplies', 3);")

                // add items to Logistic Category table
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Check weather', 4);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Feed pets', 4);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Pack appropriate attire', 4);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Leave in time for the bus', 4);")
                db!!.execSQL("INSERT INTO tbl_Item VALUES(?1, 'Get to class on time', 4);")
            }
        }
        db!!.close()
    } // dbSetup()

    fun addUser(v: View) {
        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        val name = add_user_edit_text.getText().toString()
        var curUserID = 0

        catIDs.clear()
        catNames.clear()
        if(name == "") // no username entered
        {
            val toast = Toast.makeText(applicationContext, "Please enter a name.", Toast.LENGTH_SHORT)
            toast.show()
        }
        else
        {
            if(name !in userNames)
            {
                // insert new User record
                db!!.execSQL("INSERT INTO tbl_User VALUES(?1, '$name', 0, '', '', 'active');")

                //////////////
                // USER
                var c: Cursor? = db!!.rawQuery("SELECT * FROM tbl_User WHERE userName = '$name'", null)

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            curUserID = c.getInt(0)
                        } while (c.moveToNext())
                    }
                }

                //////////////
                // CATEGORIES
                c = db!!.rawQuery("SELECT * FROM tbl_Cat", null)
                var catID: Int?
                var cat: String

                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            catID = c.getInt(0)
                            catIDs.add(catID)
                            cat = c.getString(1)
                            catNames.add(cat)
                        } while (c.moveToNext())
                    }
                }

                //////////////
                // ITEMS
                for (i in catNames.indices) {
                    val curCatID = catIDs.get(i)
                    val curCatName = catNames.get(i)

                    c = db!!.rawQuery("SELECT * FROM tbl_Item WHERE catID = '$curCatID'", null)

                    var curItem: String
                    if (c != null) {
                        if (c.moveToFirst()) {
                            do {
                                curItem = c.getString(1)
                                itemNames.add(curItem)
                            } while (c.moveToNext())
                            // add to Progress table
                            for (p in itemNames.indices) {
                                db!!.execSQL("INSERT INTO tbl_Progress VALUES(?1, '" + curCatName + "', '" + itemNames.get(p) + "', 'active', " + curUserID + ");")
                            }
                        }
                    }
                    itemNames.clear()
                }
                db!!.close()
                add_user_edit_text.setText("")
            }
            else // username already exists
            {
                val toast = Toast.makeText(applicationContext, "User already exists", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        listUsers()
    } // addUser()

    fun listUsers() {
        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)
        var c = db!!.rawQuery("SELECT * FROM tbl_User WHERE userStatus = 'active'", null)
        var curID: Int
        var curUsername: String

        userIDs.clear()
        userNames.clear()
        archivedUserIDs.clear()
        archivedUserNames.clear()

        active_title.setText("")
        archived_title.setText("")

        if (c != null) {
            if (c!!.moveToFirst()) {
                do {
                    curID = c!!.getInt(0)
                    userIDs.add(curID)
                    curUsername = c!!.getString(1)
                    if(curUsername !in userNames)
                        userNames.add(curUsername)
                } while (c!!.moveToNext())
            }
        }
        if(show_archived.isChecked) // view archived users
        {
            active_title.setText("Active")
            archived_title.setText("Archived")
            c = db!!.rawQuery("SELECT * FROM tbl_User WHERE userStatus = 'inactive'", null)
            if (c != null) {
                if (c!!.moveToFirst()) {
                    do {
                        curID = c!!.getInt(0)
                        archivedUserIDs.add(curID)
                        curUsername = c!!.getString(1)
                        if(curUsername !in archivedUserNames)
                            archivedUserNames.add(curUsername)
                    } while (c!!.moveToNext())
                }
            }
        }
        db!!.close()

        val usersAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNames)
        user_list.setAdapter(usersAdapter)

        user_list.setOnItemClickListener(userClicked)

        val archivedUsersAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, archivedUserNames)
        archived_user_list.setAdapter(archivedUsersAdapter)

        archived_user_list.setOnItemClickListener(archivedUserClicked)
    } // listUsers()

    private val userClicked = AdapterView.OnItemClickListener { parent, v, position, id ->
        val id = userIDs.get(position)
        val name = userNames.get(position)

        val progressPage = Intent(this@MainActivity, ProgressActivity::class.java)
        var userBundle = Bundle()
        userBundle.putInt("userID", id)
        userBundle.putString("userName", name)
        progressPage.putExtras(userBundle)
        startActivity(progressPage)
    } // userClicked()

    private val archivedUserClicked = AdapterView.OnItemClickListener { parent, v, position, id ->
        val id = archivedUserIDs.get(position)
        val name = archivedUserNames.get(position)

        val progressPage = Intent(this@MainActivity, ProgressActivity::class.java)
        var archivedUserBundle = Bundle()
        archivedUserBundle.putInt("userID", id)
        archivedUserBundle.putString("userName", name)
        progressPage.putExtras(archivedUserBundle)
        startActivity(progressPage)
    } // archivedUserClicked

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    } // hideKeyboard()

} // class