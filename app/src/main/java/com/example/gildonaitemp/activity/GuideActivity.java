package com.example.gildonaitemp.activity;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.adapter.TipAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private TipAdapter adapter;
    private List<String> tipList;
    private List<String> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        recyclerView = findViewById(R.id.driveTips);
        searchView = findViewById(R.id.searchView);

        searchView.setQueryHint("검색어를 입력하세요");

        tipList = new ArrayList<>();
        filteredList = new ArrayList<>();

        tipList.add("접촉 사고 발생 시 대처 요령");
        tipList.add("주행 중 시동이 꺼졌을 때 대처 요령");
        tipList.add("급발진 대처 요령");
        tipList.add("교차로 우회전 방법");
        tipList.add("비보호 좌회전 방법");
        tipList.add("올바른 유턴 방법");
        tipList.add("원형 교차로(회전교차로)에서의 통행 방법");

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
        for (String item : tipList) {
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }
}