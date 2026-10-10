package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

  private FirebaseFirestore db;

  private EditText etId;
  private EditText etTitle;
  private EditText etContent;
  private EditText etImageUrl;

  private Button btnSave;
  private Button btnShow;

  private TextView txtStatus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Kết nối Firestore
    db = FirebaseFirestore.getInstance();

    // Ánh xạ các thành phần giao diện
    etId = findViewById(R.id.etId);
    etTitle = findViewById(R.id.etTitle);
    etContent = findViewById(R.id.etContent);
    etImageUrl = findViewById(R.id.etImageUrl);

    btnSave = findViewById(R.id.btnSave);
    btnShow = findViewById(R.id.btnShow);

    txtStatus = findViewById(R.id.txtStatus);

    // Đăng ký sự kiện click
    btnSave.setOnClickListener(this);
    btnShow.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btnSave) {
      saveArticle();
    } else if (view.getId() == R.id.btnShow) {
      Intent intent = new Intent(
              MainActivity.this,
              ShowDataActivity.class
      );

      startActivity(intent);
    }
  }

  // Kiểm tra dữ liệu và lưu bài viết
  private void saveArticle() {
    final String id = etId.getText().toString().trim();
    final String title = etTitle.getText().toString().trim();
    final String content = etContent.getText().toString().trim();
    final String imageUrl = etImageUrl.getText().toString().trim();

    // Kiểm tra ID
    if (id.isEmpty()) {
      etId.setError("Vui lòng nhập ID");
      etId.requestFocus();
      return;
    }

    // Kiểm tra tiêu đề
    if (title.isEmpty()) {
      etTitle.setError("Vui lòng nhập tiêu đề");
      etTitle.requestFocus();
      return;
    }

    // Kiểm tra nội dung
    if (content.isEmpty()) {
      etContent.setError("Vui lòng nhập nội dung");
      etContent.requestFocus();
      return;
    }

    final Article article = new Article(
            id,
            title,
            content,
            imageUrl
    );

    txtStatus.setText("Đang kiểm tra ID...");

    // Kiểm tra ID đã tồn tại trong Firestore chưa
    db.collection("articles")
            .document(id)
            .get()
            .addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(
                              @NonNull Task<DocumentSnapshot> task) {

                        if (!task.isSuccessful()) {
                          Exception error = task.getException();

                          String message = "Không thể kiểm tra ID.";

                          if (error != null) {
                            message = error.getMessage();
                          }

                          txtStatus.setText("Lỗi: " + message);
                          return;
                        }

                        DocumentSnapshot document = task.getResult();

                        // Nếu ID đã tồn tại thì không lưu
                        if (document != null && document.exists()) {
                          etId.setError("ID đã tồn tại");

                          txtStatus.setText(
                                  "Không thể lưu: ID đã tồn tại."
                          );

                          return;
                        }

                        // Nếu ID chưa tồn tại thì lưu bài viết
                        db.collection("articles")
                                .document(id)
                                .set(article)
                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void unused) {
                                            txtStatus.setText(
                                                    "Lưu bài viết thành công!"
                                            );

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "Đã lưu bài viết",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            clearForm();
                                          }
                                        }
                                )
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                          @Override
                                          public void onFailure(
                                                  @NonNull Exception e) {

                                            txtStatus.setText(
                                                    "Lỗi ghi dữ liệu: "
                                                            + e.getMessage()
                                            );

                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "Không thể lưu bài viết",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                          }
                                        }
                                );
                      }
                    }
            );
  }

  // Xóa dữ liệu trên form sau khi lưu thành công
  private void clearForm() {
    etId.setText("");
    etTitle.setText("");
    etContent.setText("");
    etImageUrl.setText("");

    etId.requestFocus();
  }
}