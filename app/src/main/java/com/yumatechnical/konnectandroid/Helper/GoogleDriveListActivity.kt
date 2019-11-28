package com.yumatechnical.konnectandroid.Helper
/*
import android.content.Intent
import android.os.Bundle
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.yumatechnical.konnectandroid.R
import io.reactivex.internal.subscriptions.SubscriptionHelper.cancel

@ExperimentalCoroutinesApi
class GoogleDriveListActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    companion object {
        private const val REQUEST_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.googledrivelist)

        requestSignIn()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult=$requestCode")
        when(requestCode) {
            REQUEST_SIGN_IN -> {
                if (resultCode == RESULT_OK && data != null) {
                    handleSignInResult(data)
                }
                else {
                    Timber.d("Signin request failed")
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun buildGoogleSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestScopes(Drive.SCOPE_FILE)
                // .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .requestScopes(RestrictTo.Scope(DriveScopes.DRIVE))
                .build()
        return GoogleSignIn.getClient(this, signInOptions)
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener { googleAccount ->
                    Timber.d("Signin successful")

                    // Use the authenticated account to sign in to the Drive service.
                    val credential = GoogleAccountCredential.usingOAuth2(
                            this, listOf(DriveScopes.DRIVE_FILE)
                    )
                    credential.selectedAccount = googleAccount.account
                    val googleDriveService = Drive.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            JacksonFactory.getDefaultInstance(),
                            credential)
                            .setApplicationName(getString(R.string.app_name))
                            .build()

                    // https://developers.google.com/drive/api/v3/search-files
                    // https://developers.google.com/drive/api/v3/search-parameters
                    // https://developers.google.com/drive/api/v3/mime-types

                    launch(Dispatchers.Default) {
                        var pageToken: String? = null
                        do {
                            val result = googleDriveService.files().list().apply {
                                q = "mimeType='application/vnd.google-apps.spreadsheet'"
                                spaces = "drive"
                                fields = "nextPageToken, files(id, name)"
                                this.pageToken = pageToken
                            }.execute()
                            for (file in result.files) {
                                Timber.d("name=${file.name}, id=${file.id}")
                            }
                        } while (pageToken != null)
                    }
                }
                .addOnFailureListener { e ->
                    Timber.e(e, "Signin error")
                }
    }

    private fun requestSignIn() {
        val client = buildGoogleSignInClient()
        startActivityForResult(client.signInIntent, REQUEST_SIGN_IN)
    }
}
c*/