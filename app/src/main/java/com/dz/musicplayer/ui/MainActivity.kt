package com.dz.musicplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dz.musicplayer.R
import com.dz.musicplayer.databinding.ActivityMainBinding
import com.dz.musicplayer.databinding.ActivityPlayerBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding  get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Songs"

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(navGraph = navController.graph)
        setupActionBarWithNavController(navController,appBarConfiguration)

        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO)
            .withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                }

                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }


            }).check()


        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.songs -> {
                    navController.navigate(R.id.songsFragment)
                    binding.toolbar.title = "Songs"
                }
                R.id.library -> {
                    navController.navigate(R.id.libraryFragment)
                    binding.toolbar.title = "Library"
                }
                R.id.folder -> {
                    navController.navigate(R.id.foldersFragment)
                    binding.toolbar.title = "Audio Folders"
                }
            }

            true
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}