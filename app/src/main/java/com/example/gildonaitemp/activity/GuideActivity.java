package com.example.gildonaitemp.activity;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.adapter.TipAdapter;
import com.example.gildonaitemp.adapter.TipItem;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private TipAdapter adapter;
    private List<TipItem> tipList;
    private List<TipItem> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        recyclerView = findViewById(R.id.driveTips);
        searchView = findViewById(R.id.searchView);

        searchView.setQueryHint("검색어를 입력하세요");

        tipList = new ArrayList<>();
        filteredList = new ArrayList<>();

        tipList.add(new TipItem("수신호 동작", getString(R.string.hand_signal_desc), R.drawable.driver_hand_signal, true));
        tipList.add(new TipItem("교통정리가 없는 교차로: 기본 진입 방법", getString(R.string.intersection_yield_entered_vehicle), R.drawable.intersection_yield_entered_vehicle, true));
        tipList.add(new TipItem("교통정리가 없는 교차로: 도로 폭에 따른 진입 방법", getString(R.string.intersection_yield_wider_road), R.drawable.intersection_yield_wider_road, true));
        tipList.add(new TipItem("교통정리가 없는 교차로: 우측도로 차량 우선", getString(R.string.intersection_yield_right_road), R.drawable.intersection_yield_right_road, true));
        tipList.add(new TipItem("교통정리가 없는 교차로: 좌회전 방법", getString(R.string.intersection_yield_turning_left), R.drawable.intersection_yield_turning_left, true));

        tipList.add(new TipItem("교통사고 발생시 대처법", getString(R.string.traffic_accident_action_desc), 0, false));
        tipList.add(new TipItem("교통사고의 신고", getString(R.string.traffic_accident_report_desc), 0, false));

        tipList.add(new TipItem("교차로 통행 방법 : 올바른 우회전 방법", getString(R.string.intersection_right_turn_desc), 0, false));
        tipList.add(new TipItem("교차로 통행 방법 : 올바른 좌회전 방법", getString(R.string.intersection_left_turn_desc), 0, false));

        tipList.add(new TipItem("서행 및 일시정지", getString(R.string.slow_stop_desc), 0, false));
        tipList.add(new TipItem("과태료의 부과", getString(R.string.traffic_law_fine_desc), 0, false));

        filteredList.addAll(tipList);

        adapter = new TipAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 서치뷰 필터링 기능
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        filteredList.clear();
        for (TipItem item : tipList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }
}