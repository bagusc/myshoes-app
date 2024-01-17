package com.bagusc.myshoes;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText inputEmail, inputPass;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);
        btnLogin = findViewById(R.id.loginBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPass.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Masuk berhasil
                            // Tambahkan tindakan yang sesuai di sini
                            Toast.makeText(MainActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                            // Arahkan ke kelas Dashboard
                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish(); // Jika Anda ingin menutup MainActivity setelah berhasil login

                        } else {
                            // Gagal masuk, tampilkan pesan kesalahan
                            Toast.makeText(MainActivity.this, "Gagal masuk, coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
