package in.ureport.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import in.ureport.R;
import in.ureport.models.User;

/**
 * Created by johncordeiro on 7/15/15.
 */
public class CoauthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> users;
    private List<User> selectedCoauthors;

    private CoauthorSelectionListener coauthorSelectionListener;

    public CoauthorAdapter(List<User> users, List<User> selectedCoauthors) {
        this.users = users;
        this.selectedCoauthors = selectedCoauthors;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_coauthor, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bindView(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setCoauthorSelectionListener(CoauthorSelectionListener coauthorSelectionListener) {
        this.coauthorSelectionListener = coauthorSelectionListener;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            check = (CheckBox) itemView.findViewById(R.id.check);
            check.setOnClickListener(onCheckClickListener);
        }

        private void bindView(User user) {
            check.setChecked(selectedCoauthors != null && selectedCoauthors.contains(user));
            name.setText(user.getUsername());
        }

        private View.OnClickListener onCheckClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coauthorSelectionListener != null) {
                    User user = users.get(getLayoutPosition());
                    if (check.isChecked()) {
                        coauthorSelectionListener.onCoauthorSelected(user);
                    } else {
                        coauthorSelectionListener.onCoauthorDeselected(user);
                    }
                }
            }
        };
    }

    public interface CoauthorSelectionListener {
        void onCoauthorSelected(User user);
        void onCoauthorDeselected(User user);
    }
}