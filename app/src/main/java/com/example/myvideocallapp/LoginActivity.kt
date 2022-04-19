package com.example.myvideocallapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import pub.devrel.easypermissions.EasyPermissions

class LoginActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val RC_CALL = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val vm: LoginViewModel by viewModels()

        vm.authToken.observe(this) { authToken ->
            launchVideoRoomActivity(authToken)
        }

        vm.error.observe(this) { error -> showError(error) }

        findViewById<Button>(R.id.authenticateButton).setOnClickListener {
            val meetingLink = findViewById<TextInputEditText>(R.id.urlInputEditText).text.toString()

            vm.authenticate(meetingLink)
        }
    }

    private fun launchVideoRoomActivity(authToken : String?) {
        if(authToken != null) {
            // Launch the video room
            startActivity(Intent(this, VideoCallActivity::class.java).apply {
                putExtra(BUNDLE_AUTH_TOKEN, authToken)
                val name = findViewById<TextInputEditText>(R.id.nameInputEditText).text.toString()
                putExtra(BUNDLE_NAME, name.ifBlank { "Android User" })
            })
        }
    }

    private fun showError(error: String?) {
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onResume() {
        super.onResume()
        permissionCheck()
    }

    private fun permissionCheck() {
        if (EasyPermissions.hasPermissions(this, *permissions)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "Camera and Record Audio are needed for the app to work.",
                RC_CALL, *permissions
            )
        }
    }
}