package in.ureport.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.ureport.R;
import in.ureport.helpers.ValueEventListenerAdapter;
import in.ureport.models.KeywordsResult;
import in.ureport.models.MultipleResult;
import in.ureport.models.Poll;
import in.ureport.models.PollResult;
import in.ureport.network.PollServices;
import in.ureport.views.adapters.PollResultsAdapter;

/**
 * Created by johncordeiro on 18/07/15.
 */
public class PollAllResultsFragment extends Fragment {

    private static final String TAG = "PollAllResults";

    private static final String EXTRA_POLL = "poll";

    private Poll poll;
    private PollResultsAdapter.PollResultsListener pollResultsListener;

    private RecyclerView resultsList;
    private ProgressBar progressBar;

    private PollServices pollServices;

    public static PollAllResultsFragment newInstance(Poll poll) {
        PollAllResultsFragment pollAllResultsFragment = new PollAllResultsFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_POLL, poll);
        pollAllResultsFragment.setArguments(args);

        return pollAllResultsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(EXTRA_POLL)) {
            poll = getArguments().getParcelable(EXTRA_POLL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_poll_all_results, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.label_poll_results);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupObjects();
        setupView(view);
        loadData();
    }

    private void setupObjects() {
        pollServices = new PollServices();
    }

    private void setupView(View view) {
        resultsList = (RecyclerView) view.findViewById(R.id.resultsList);
        resultsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void loadData() {
        pollServices.getPollResults(poll, onPollServicesLoadedListener);
    }

    private void setupPollResults(List<PollResult> pollResults) {
        PollResultsAdapter pollResultsAdapter = new PollResultsAdapter(pollResults
                , getResources().getStringArray(R.array.poll_colors));
        pollResultsAdapter.setPollResultsListener(pollResultsListener);
        resultsList.setAdapter(pollResultsAdapter);
    }

    private void addPollResultIfPossible(List<PollResult> pollResults, DataSnapshot snapshot) {
        try {
            Map<String, String> value = (Map<String, String>) snapshot.getValue();
            if (PollResult.Type.valueOf(value.get("type")) == PollResult.Type.Choices) {
                PollResult pollResult = snapshot.getValue(MultipleResult.class);
                pollResults.add(pollResult);
            } else {
                PollResult pollResult = snapshot.getValue(KeywordsResult.class);
                pollResults.add(pollResult);
            }
        } catch (Exception exception) {
            Log.e(TAG, "onDataChange ", exception);
        }
    }

    public void setPollResultsListener(PollResultsAdapter.PollResultsListener pollResultsListener) {
        this.pollResultsListener = pollResultsListener;
    }

    private ValueEventListenerAdapter onPollServicesLoadedListener = new ValueEventListenerAdapter() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            super.onDataChange(dataSnapshot);
            updateViewFotData();

            List<PollResult> pollResults = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                addPollResultIfPossible(pollResults, snapshot);
            }

            setupPollResults(pollResults);
        }
    };

    private void updateViewFotData() {
        progressBar.setVisibility(View.GONE);
    }
}
