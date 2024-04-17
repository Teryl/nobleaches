package com.example.nobleaches;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<UserData> userDataList;
    private List<HistoryRecord> historyRecordList;

    public HistoryAdapter(Context context, List<HistoryRecord> historyRecordList) {
        this.context = context;
        this.historyRecordList = historyRecordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryRecord historyRecord = historyRecordList.get(position);
        holder.bind(historyRecord);
    }

    @Override
    public int getItemCount() {
        return historyRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView machineNameTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView processedTextView;
        private TextView rTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            machineNameTextView = itemView.findViewById(R.id.machineNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            processedTextView = itemView.findViewById(R.id.processedTextView);
            rTimeTextView = itemView.findViewById(R.id.rTimeTextView);
        }

        public void bind(HistoryRecord historyRecord) {
            // Set request date
            Date recordDate = historyRecord.getRequestTime();
            if (recordDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(recordDate);
                dateTextView.setText(String.format("Date: %s", formattedDate));
            } else {
                dateTextView.setText("Date: N/A");
            }

            // Set request time
            if (recordDate != null) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedTime = timeFormat.format(recordDate);
                timeTextView.setText(String.format("Time: %s", formattedTime));
            } else {
                timeTextView.setText("Time: N/A");
            }

            // Set processed status
            Date processedDate = historyRecord.getProcessedOn();
            if (processedDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                String formattedProcessedOn = dateFormat.format(processedDate);
                processedTextView.setText(String.format("Processed on: %s", formattedProcessedOn));
            } else {
                processedTextView.setText("Processed on: N/A");
            }

            // Set processed status
            if (processedDate != null) {
                Date reserveDate = Date.from(processedDate.toInstant().plusMillis(3600000));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                String formattedProcessedOn = dateFormat.format(reserveDate);
                rTimeTextView.setText(String.format("Reserved for: %s", formattedProcessedOn));
            } else {
                rTimeTextView.setText("Reserved for: N/A");
            }
            machineNameTextView.setText(String.format("%s-%s", historyRecord.getMachine().getName(), historyRecord.getMachine().getBlock()));
        }
    }
}
