
package vn.edu.ueh.thanhdnh.firebase_example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter
        extends RecyclerView.Adapter<ArticleViewHolder> {

    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ArticleViewHolder holder, int position) {

        Article article = articles.get(position);

        holder.txtId.setText("ID: " + article.getId());
        holder.txtTitle.setText(article.getTitle());
        holder.txtContent.setText(article.getContent());

        String imageUrl = article.getImage_url();

        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.txtImageUrl.setText("Chưa có URL hình ảnh");
        } else {
            holder.txtImageUrl.setText("Image URL: " + imageUrl);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}