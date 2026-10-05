package com.example.layout_experiment;

import com.example.layout_experiment.compose.ComposeExperimentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 强制状态栏文字/图标为深色，因为这个界面的背景是浅色 (#F5F5F5)
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);

        View mainView = findViewById(R.id.main);
        final int originalPaddingLeft = mainView.getPaddingLeft();
        final int originalPaddingTop = mainView.getPaddingTop();
        final int originalPaddingRight = mainView.getPaddingRight();
        final int originalPaddingBottom = mainView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            // 叠加系统边距和原始 XML 中的 padding，避免遮挡并保留原始间距
            v.setPadding(originalPaddingLeft + systemBars.left, originalPaddingTop + systemBars.top,
                    originalPaddingRight + systemBars.right, originalPaddingBottom + systemBars.bottom);
            return insets;
        });

        // 实验一：线性布局
        Button btnLinear = findViewById(R.id.btn_linear);
        btnLinear.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LinearLayoutActivity.class));
        });

        // 实验二：表格布局
        Button btnTable = findViewById(R.id.btn_table);
        btnTable.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TableLayoutActivity.class));
        });

        // 实验三：约束布局 1
        Button btnConstraint1 = findViewById(R.id.btn_constraint1);
        btnConstraint1.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ConstraintLayoutActivity1.class));
        });

        // 实验四：约束布局 2
        Button btnConstraint2 = findViewById(R.id.btn_constraint2);
        btnConstraint2.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ConstraintLayoutActivity2.class));
        });

        // 实验五：Compose 实验
        Button btnCompose = findViewById(R.id.btn_compose);
        btnCompose.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ComposeExperimentActivity.class));
        });
    }
}