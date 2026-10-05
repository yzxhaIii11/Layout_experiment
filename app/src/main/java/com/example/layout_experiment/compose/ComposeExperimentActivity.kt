package com.example.layout_experiment.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. 定义数据类，用于存储任务的状态
data class TaskItemData(
    val id: Int,
    val name: String,
    var isCompleted: Boolean = false
)

class ComposeExperimentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 开启沉浸式状态栏
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 调用主界面 Composable 函数
                    TaskListScreen(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(modifier: Modifier = Modifier) {
    // 2. 状态管理
    var inputText by remember { mutableStateOf("") }
    var nextId by remember { mutableIntStateOf(4) }

    // 初始化时提供3个任务，其中1个为已完成状态
    val taskList = remember {
        mutableStateListOf(
            TaskItemData(1, "学习 Column 和 Row", true),
            TaskItemData(2, "学习状态管理", false),
            TaskItemData(3, "完成 Compose 实验", false)
        )
    }

    // 动态计算已完成数量和总数量
    val completedCount = taskList.count { it.isCompleted }
    val totalCount = taskList.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "课程学习任务",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC62828), // 接近指导书的深红色
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 输入框与添加按钮 Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("请输入学习任务", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFC62828)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        taskList.add(TaskItemData(nextId++, inputText))
                        inputText = "" // 添加后清空输入框
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                shape = MaterialTheme.shapes.small
            ) {
                Text("添加", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 任务进度文本
        Text(
            text = "已完成：$completedCount / $totalCount",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3. LazyColumn 列表
        LazyColumn {
            items(taskList, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onCheckedChange = { isChecked ->
                        // 找到对应项并更新其状态
                        val index = taskList.indexOfFirst { it.id == task.id }
                        if (index != -1) {
                            taskList[index] = taskList[index].copy(isCompleted = isChecked)
                        }
                    },
                    onDeleteClick = {
                        taskList.removeAll { it.id == task.id }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: TaskItemData,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFC62828),
                    checkmarkColor = Color.White
                )
            )

            // 文本状态：如果已完成，添加删除线并变灰
            Text(
                text = task.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                color = if (task.isCompleted) Color.Gray else Color.Black,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            TextButton(onClick = onDeleteClick) {
                Text("删除", color = Color(0xFFC62828))
            }
        }
    }
}