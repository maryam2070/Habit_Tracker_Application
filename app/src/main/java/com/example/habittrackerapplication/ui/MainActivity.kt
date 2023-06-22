package com.example.habittrackerapplication.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.habittrackerapplication.R
import com.example.habittrackerapplication.databinding.ActivityMainBinding
import com.example.habittrackerapplication.databinding.NavHeaderBinding
import com.example.habittrackerapplication.repositories.FirebaseAuthRepo
import com.example.habittrackerapplication.viewmodels.MainActivityViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding:ActivityMainBinding
    lateinit var navHostFragment:NavHostFragment
    lateinit var navController: NavController
    var imgURI: Uri? =null

    val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this, MainActivityViewModel.MainActivityViewModelFactory(
            FirebaseAuthRepo(FirebaseAuth.getInstance())
        )).get(
            MainActivityViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_nav) as NavHostFragment
        navController = navHostFragment.navController



        binding.bottomNav.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener(this)


        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.homeFragment || nd.id == R.id.habitListFragment
                || nd.id == R.id.calenderHistoryFragment
            ) {

                binding.toolbar.title=""
                setSupportActionBar(binding.toolbar)
                val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.toolbar, R.string.open_nav, R.string.close_nav)
                binding.drawerLayout.addDrawerListener(toggle)
                toggle.isDrawerIndicatorEnabled=true
                toggle.syncState()
                binding.bottomNav.visibility = View.VISIBLE
                binding.toolbar.visibility=View.VISIBLE
                if(FirebaseAuth.getInstance().currentUser !=null)
                  updateNavDrawerData(FirebaseAuth.getInstance().currentUser!!)
            } else {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility=View.GONE
            }
        }

        var header=binding.navView.getHeaderView(0)
        header.findViewById<ImageView>(R.id.profile_iv).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            showImageDialog()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editEmailFragment -> {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.editEmailFragment)
            }

            R.id.editPasswordFragment -> {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.editPasswordFragment)
            }
            R.id.logout->{
                viewModel.logout()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                //navController.clearBackStack(R.id.welcome_navigation)
            // //navController.navigate(R.id.coreNavToLoginFragment)
            }
        }
        return true

    }

    override fun onBackPressed() {

        if (navController.currentDestination!!.id == R.id.homeFragment||navController.currentDestination!!.id == R.id.habitListFragment
            ||navController.currentDestination!!.id == R.id.calenderHistoryFragment) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else
                finish()
        } else {
            super.onBackPressed()
        }
    }


    fun updateNavDrawerData(user:FirebaseUser)
    {
        var header=binding.navView.getHeaderView(0)
        val photo=header.findViewById<ImageView>(R.id.profile_iv)

        Glide.with(this)
            .load(user.photoUrl)
            .placeholder(R.drawable.user_avatar)
            //.centerCrop()
            .circleCrop()
            .into(photo)

        header.findViewById<TextView>(R.id.name_tv).text= user.displayName
        header.findViewById<TextView>(R.id.email_tv).text=user.email

    }


    private fun showImageDialog()
    {

        val dialog: Dialog = Dialog(this)
        dialog.setContentView(R.layout.set_img_dialog)
        dialog.show()
        dialog.findViewById<ImageView>(R.id.gallery_iv).setOnClickListener {
            dialog.dismiss()
            startGallery()

            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                startGallery()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }

                }).onSameThread().check()
        }



        dialog.findViewById<ImageView>(R.id.camera_iv).setOnClickListener {

            dialog.dismiss()
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener {
                    // ensure you implement members of the object which is related to dexter third party library
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                startCamera()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // It is the alert dialog that user will allow permissions
                        showRationalDialogForPermissions()
                    }

                }).onSameThread().check()
        }
        }



    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(
            "It looks that you have turned off " +
                    "permissions required for these features. It can be enabled under " +
                    "applications settings"
        )
            .setPositiveButton("GO TO SETTINGS")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
    private fun startGallery() {
        val intent= Intent(Intent.ACTION_GET_CONTENT)
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 1)
    }

    fun startCamera()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgURI)
        startActivityForResult(cameraIntent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && resultCode== RESULT_OK) {

            val user=FirebaseAuth.getInstance().currentUser
            viewModel.uploadImage(data!!.data!!)
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            val user=FirebaseAuth.getInstance().currentUser
            val photo = data!!.extras!!.get("data") as Bitmap


            val bytes = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(
                this.getContentResolver(),
                photo,
                "Title",
                null
            )
            val uri=Uri.parse(path)
            viewModel.uploadImage(uri)
        }
    }

}