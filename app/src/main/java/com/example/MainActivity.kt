package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          SmartSanApp()
        }
      }
    }
  }
}

@Composable
fun SmartSanApp() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "splash") {
    composable("splash") { SplashScreen(navController) }
    composable("login") { LoginScreen(navController) }
    composable("dashboard") { DashboardScreen(navController) }
    composable("task_details/{taskId}") { backStackEntry ->
      val taskId = backStackEntry.arguments?.getString("taskId") ?: "1"
      TaskDetailsScreen(navController, taskId)
    }
  }
}

@Composable
fun SplashScreen(navController: NavController) {
  var visible by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    visible = true
    delay(2000)
    navController.navigate("login") {
      popUpTo("splash") { inclusive = true }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BgLight),
    contentAlignment = Alignment.Center
  ) {
    AnimatedVisibility(
      visible = visible,
      enter = fadeIn(tween(1000)) + scaleIn(tween(1000)),
      exit = fadeOut()
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = Icons.Default.WaterDrop,
          contentDescription = "Logo",
          modifier = Modifier.size(100.dp),
          tint = PrimaryBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "SmartSan",
          style = MaterialTheme.typography.headlineLarge,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryLight
        )
        Text(
          text = "AI Powered Cleanliness",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondaryLight
        )
      }
    }
  }
}

@Composable
fun LoginScreen(navController: NavController) {
  var id by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }

  LaunchedEffect(isLoading) {
    if (isLoading) {
      delay(1500)
      navController.navigate("dashboard") {
        popUpTo("login") { inclusive = true }
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.Shield,
      contentDescription = "Logo",
      modifier = Modifier.size(80.dp),
      tint = PrimaryBlue
    )
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = "Cleaner Portal",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      color = TextPrimaryLight
    )
    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
      value = id,
      onValueChange = { id = it },
      label = { Text("Cleaner ID") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PrimaryBlue,
        focusedLabelColor = PrimaryBlue,
        unfocusedBorderColor = BorderLight,
        unfocusedContainerColor = SurfaceLight,
        focusedContainerColor = SurfaceLight
      ),
      shape = RoundedCornerShape(12.dp),
      leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondaryLight) }
    )
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Password") },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PrimaryBlue,
        focusedLabelColor = PrimaryBlue,
        unfocusedBorderColor = BorderLight,
        unfocusedContainerColor = SurfaceLight,
        focusedContainerColor = SurfaceLight
      ),
      shape = RoundedCornerShape(12.dp),
      leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondaryLight) }
    )
    Spacer(modifier = Modifier.height(32.dp))

    Button(
      onClick = { isLoading = true },
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
      shape = RoundedCornerShape(12.dp)
    ) {
      if (isLoading) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
      } else {
        Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

data class Task(val id: String, val name: String, val location: String, val distance: String, val priority: String, val score: Int)

val dummyTasks = listOf(
  Task("T-104", "Central Park Restroom A", "Ward 4, North Gate", "0.4 km", "Critical", 22),
  Task("T-082", "Subway Station L1", "Ward 2, Main Concourse", "1.2 km", "High", 45),
  Task("T-211", "City Hall Public", "Ward 1, East Wing", "2.5 km", "Medium", 68)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PrimaryBlue),
              contentAlignment = Alignment.Center
            ) {
              Text("JS", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text("J. Sharma", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
              Text("SmartSan Cleaner", fontSize = 10.sp, color = TextSecondaryLight, fontWeight = FontWeight.Bold)
            }
          }
        },
        actions = {
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = TextPrimaryLight)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight.copy(alpha = 0.8f))
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = PrimaryBlue,
        contentColor = Color.White
      ) {
        Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan")
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          StatCard("Pending", "3", WarningYellow, Modifier.weight(1f))
          Spacer(modifier = Modifier.width(12.dp))
          StatCard("Critical", "1", CriticalRed, Modifier.weight(1f))
          Spacer(modifier = Modifier.width(12.dp))
          StatCard("Completed", "5", SuccessGreen, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
          text = "Assigned Tasks",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = TextPrimaryLight
        )
        Spacer(modifier = Modifier.height(16.dp))
      }

      items(dummyTasks) { task ->
        TaskCard(task) {
          navController.navigate("task_details/${task.id}")
        }
        Spacer(modifier = Modifier.height(12.dp))
      }
    }
  }
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(SurfaceLight)
      .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
      .padding(12.dp)
  ) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
      Text(title, fontSize = 12.sp, color = TextSecondaryLight)
      Spacer(modifier = Modifier.height(4.dp))
      Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
    }
  }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
  val isCritical = task.priority == "Critical"
  val priorityColor = when (task.priority) {
    "Critical" -> CriticalRed
    "High" -> WarningOrange
    else -> PrimaryBlue
  }

  val bgColor = if (isCritical) CardDarkBg else SurfaceLight
  val textColor = if (isCritical) Color.White else TextPrimaryLight
  val subTextColor = if (isCritical) Color(0xFF94A3B8) else TextSecondaryLight
  val borderColor = if (isCritical) Color.Transparent else BorderLight

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(32.dp))
      .background(bgColor)
      .clickable(onClick = onClick)
      .border(1.dp, borderColor, RoundedCornerShape(32.dp))
      .padding(24.dp)
  ) {
    Column {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(priorityColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
          Text(task.priority.uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Text(task.id, color = subTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
      Spacer(modifier = Modifier.height(16.dp))
      Text(task.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
      Text(task.location, fontSize = 14.sp, color = subTextColor)
      Spacer(modifier = Modifier.height(24.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = subTextColor, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(task.distance, fontSize = 14.sp, color = subTextColor)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Analytics, contentDescription = null, tint = subTextColor, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Score: ${task.score}/100", fontSize = 14.sp, color = priorityColor, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(navController: NavController, taskId: String) {
  val task = dummyTasks.find { it.id == taskId } ?: dummyTasks.first()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Task Details", color = TextPrimaryLight) },
        navigationIcon = {
          IconButton(onClick = { navController.navigateUp() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimaryLight)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceLight.copy(alpha = 0.8f))
      )
    },
    bottomBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Button(
          onClick = { navController.navigateUp() },
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Start Navigation", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(task.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Text(task.location, fontSize = 16.sp, color = TextSecondaryLight)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Sensor Data", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          SensorCard(Icons.Default.WaterDrop, "Water", "18%", CriticalRed, Modifier.weight(1f))
          Spacer(modifier = Modifier.width(12.dp))
          SensorCard(Icons.Default.Delete, "Dustbin", "91%", CriticalRed, Modifier.weight(1f))
          Spacer(modifier = Modifier.width(12.dp))
          SensorCard(Icons.Default.Air, "Odour", "High", WarningOrange, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("AI Recommendation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardDarkBg)
            .border(1.dp, PrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.SmartToy, contentDescription = "AI", tint = PrimaryBlue, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
              Text("Immediate Cleaning Required", fontWeight = FontWeight.Bold, color = CriticalRed)
              Text("Focus on refilling water and emptying the dustbin. High occupancy detected.", color = Color(0xFF94A3B8), fontSize = 14.sp)
            }
          }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
          onClick = { /*TODO*/ },
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryLight)
        ) {
          Icon(Icons.Default.CameraAlt, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Upload Before Photo")
        }
        
        Spacer(modifier = Modifier.height(100.dp)) // Bottom bar padding
      }
    }
  }
}

@Composable
fun SensorCard(icon: ImageVector, title: String, value: String, color: Color, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(SurfaceLight)
      .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
      .padding(12.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.height(8.dp))
      Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
      Text(title, fontSize = 12.sp, color = TextSecondaryLight)
    }
  }
}
