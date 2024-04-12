import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nobleaches.R;
import com.example.nobleaches.UserData;
import com.example.nobleaches.UserHistory;
import com.example.nobleaches.UserListSingleton;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<UserData> userDataList;

    public HistoryAdapter(Context context) {
        this.context = context;
        this.userDataList = UserListSingleton.getInstance().getUserList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserData userData = userDataList.get(position);
        holder.bind(userData);
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView;
        private LinearLayout historyLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            historyLayout = itemView.findViewById(R.id.historyLayout);
        }

        public void bind(UserData userData) {
            userNameTextView.setText(userData.getUserName());
            historyLayout.removeAllViews(); // Clear previous views

            List<UserHistory> userHistoryList = userData.getUserHistoryList();
            for (UserHistory userHistory : userHistoryList) {
                View historyView = LayoutInflater.from(context).inflate(R.layout.item_machine_history, null);
                TextView machineNameTextView = historyView.findViewById(R.id.machineNameTextView);
                TextView dateTextView = historyView.findViewById(R.id.dateTextView);
                TextView timeTextView = historyView.findViewById(R.id.timeTextView);

                machineNameTextView.setText("Machine: " + userHistory.getMachineName());
                dateTextView.setText("Date: " + userHistory.getDate());
                timeTextView.setText("Time: " + userHistory.getTime());

                historyLayout.addView(historyView); // Add view to LinearLayout
            }
        }
    }
}
