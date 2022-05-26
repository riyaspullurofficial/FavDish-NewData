package com.riyaspullur.favdish

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.riyaspullur.favdish.databinding.ActivityAddUpdateDishBinding
import com.riyaspullur.favdish.databinding.DialodCustomImageSelectionBinding
import kotlinx.android.synthetic.main.activity_add_update_dish.*
import java.lang.Exception

class AddUpdateDishActivity : AppCompatActivity() , View.OnClickListener{
    companion object{
        var CAMERA_CODE=100
    }
    private lateinit var mbind:ActivityAddUpdateDishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbind=ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mbind.root)

        setupActionBAr()

        mbind.camIconFrameId.setOnClickListener (this)
    }
    private fun setupActionBAr(){
        setSupportActionBar(mbind.toolBarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mbind.toolBarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
       if (v!=null){
           when(v.id){
               R.id.camIconFrameId->{
                   Toast.makeText(this,"You clicked the image icon",Toast.LENGTH_LONG).show()
                   customImageSelectionDialog()
                   return
               }
           }
       }
    }

    private fun customImageSelectionDialog(){
        val dialog= Dialog(this)
        val binding:DialodCustomImageSelectionBinding=DialodCustomImageSelectionBinding
                                                    .inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()


        binding.cameraSelId.setOnClickListener {
            Toast.makeText(this,"You clicked camera select",Toast.LENGTH_LONG).show()

            //using permission Dexter ApI 1
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object :MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let {
                        if (it.areAllPermissionsGranted()){
                            Toast.makeText(this@AddUpdateDishActivity,"Permission granted",Toast.LENGTH_LONG).show()
                            val inte=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(inte, CAMERA_CODE)
                        }else{
                            // show alert
                            showRationalDialogForPermissions()
                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                   // show alert
                    showRationalDialogForPermissions()

                }

            }
            ).onSameThread().check()
            dialog.dismiss()
        }
        binding.gallerySelId.setOnClickListener {
            Toast.makeText(this,"You clicked gallery select",Toast.LENGTH_LONG).show()



            //using permission Dexter ApI 1
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object :PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        Toast.makeText(this@AddUpdateDishActivity,
                            "Permission granted",Toast.LENGTH_LONG).show()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateDishActivity,
                            "Permission granted",Toast.LENGTH_LONG).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                        // show alert
                        showRationalDialogForPermissions()
                }

            }
            ).onSameThread().check()
            dialog.dismiss()

        }
    }
    //permission data call 2
    private fun showRationalDialogForPermissions(){
         AlertDialog.Builder(this).setMessage("It look like you have turned off Pemissionsrequire this feature...")
            .setPositiveButton("Go To settings")
            {_,_->
                try{
                    //call intent to the settings of that app
                    val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri=Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()

                }
            }
            .setNegativeButton("cancel"){dialog,_->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (requestCode== CAMERA_CODE){
              data?.extras?.let {
                  val thumbnail:Bitmap=data.extras!!.get("data") as Bitmap
                  ivDishImageID.setImageBitmap(thumbnail)
                  camIconFrameId.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_edit_24))
              }
            }


        }
    }

}