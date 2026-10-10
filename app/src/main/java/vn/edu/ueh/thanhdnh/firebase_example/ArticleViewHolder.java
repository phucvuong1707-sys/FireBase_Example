
package vn.edu.ueh.thanhdnh.firebase_example;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    TextView txtId;
    TextView txtTitle;
    TextView txtContent;
    TextView txtImageUrl;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);

        txtId = itemView.findViewById(R.id.txtId);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtContent = itemView.findViewById(R.id.txtContent);
        txtImageUrl = itemView.findViewById(R.id.txtImageUrl);
    }
}