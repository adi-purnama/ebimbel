package com.tisoft_id.elearningbimbel.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tisoft_id.elearningbimbel.R
import com.tisoft_id.elearningbimbel.core.Preferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var xjenisuser:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences=Preferences(this)
        xjenisuser=preferences.getInt("jenisuser")
        btnlogout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("Yakin akan Logout?")
            alertDialog.setPositiveButton("Ya") { _, _ ->
                preferences.delete("login")
                preferences.delete("jenisuser")
                val inten=Intent(this@MainActivity,LoginActivity::class.java)
                    .apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(inten)
                finish()
            }
            alertDialog.setNegativeButton("Tidak", null)
            alertDialog.show()
        }
        /*if (xjenisuser!=1){
            bottomNavigationView.menu.removeItem(R.id.tugas_tab)
        }*/
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val fragment: Fragment
            val menuItemSelectedID = menuItem.itemId

            fragment = when (menuItemSelectedID) {
                R.id.kandang_tab -> PelajaranFragment()
                R.id.graf_tab -> LatihanFragment()
                R.id.tugas_tab -> TugasFragment()
                else -> DiskusiFragment()
            }

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.content, fragment)
            fragmentTransaction.commit()

            menuItem.isChecked = true
            false
        }
        bottomNavigationView.selectedItemId = R.id.kandang_tab
    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
