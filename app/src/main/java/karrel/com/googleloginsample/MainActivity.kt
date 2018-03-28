package karrel.com.googleloginsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*

// TODO LIST

// - 페이스북과 트위터의 로그인에 대한 테스트 및 적용 진행
// - 계정삭제 추가
class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123

    // Choose authentication providers
    val providers: List<AuthUI.IdpConfig> = Arrays.asList(
            AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
            AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
            AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 버튼 이벤트에 대한 세팅을 진행
        setupButtonEvents()

        // 로그인 했던 계정이 있는지 가져온다
        getLoginInfo()

    }

    // 로그인 정보를 가져온다
    private fun getLoginInfo() {
        print("I say Hello!!")

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
            printLoginInfo(user!!)

        } else {
            // No user is signed in
            Log.e("HELLO", "No user is signed in!!")
        }
    }

    // 로그인정보를 출력
    private fun printLoginInfo(user: FirebaseUser) {
        name.text = "name : ${user.displayName}"
        email.text = "email : ${user.email}"
        emailVerified.text = "emailVerifed : ${user.isEmailVerified}"
        uid.text = "uid : ${user.uid}"

        Glide.with(this).load(user.photoUrl).into(profileImage)

    }

    private fun clearLoginInfo() {
        name.text = ""
        email.text = ""
        emailVerified.text = ""
        uid.text = ""

        profileImage.setImageResource(R.drawable.ic_launcher_foreground)
    }

    // 버튼 이벤트
    private fun setupButtonEvents() {
        // 로그인 버튼 이벤트
        login.setOnClickListener {
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.img_login)
                            .build(),
                    RC_SIGN_IN)
        }

        // 로그아웃 버튼 이벤트
        logout.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            Log.e("HELLO", "logout onComplete")
                            toast("logout onComplete")

                            // 로그인 정보 제거
                            clearLoginInfo()
                        }
                    })

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode === RC_SIGN_IN) {
            if (resultCode === RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                printLoginInfo(user!!)

            } else {
                toast("Sign in failed")
            }
        }
    }
}
