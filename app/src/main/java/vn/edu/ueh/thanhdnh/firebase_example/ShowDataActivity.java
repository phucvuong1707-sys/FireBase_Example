
package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TextView txtEmpty;

    private ArticleAdapter adapter;
    private List<Article> articles = new ArrayList<>();
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        recyclerView = findViewById(R.id.recyclerView);
        txtEmpty = findViewById(R.id.txtEmpty);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new ArticleAdapter(articles);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listenForArticles();
    }

    private void listenForArticles() {
        listenerRegistration = db.collection("articles")
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(
                                    @Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException error) {

                                if (error != null) {
                                    txtEmpty.setVisibility(View.VISIBLE);
                                    txtEmpty.setText(
                                            "Lỗi đọc dữ liệu: " + error.getMessage()
                                    );
                                    return;
                                }

                                if (snapshots == null) {
                                    txtEmpty.setVisibility(View.VISIBLE);
                                    txtEmpty.setText(
                                            "Không nhận được dữ liệu."
                                    );
                                    return;
                                }

                                articles.clear();

                                for (QueryDocumentSnapshot document : snapshots) {
                                    Article article = document.toObject(Article.class);

                                    // Lấy document ID nếu trường id không có dữ liệu
                                    if (article.getId() == null
                                            || article.getId().isEmpty()) {
                                        article.setId(document.getId());
                                    }

                                    articles.add(article);
                                }

                                adapter.notifyDataSetChanged();

                                if (articles.isEmpty()) {
                                    txtEmpty.setVisibility(View.VISIBLE);
                                    txtEmpty.setText(
                                            "Chưa có bài viết nào."
                                    );
                                } else {
                                    txtEmpty.setVisibility(View.GONE);
                                }
                            }
                        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }
}