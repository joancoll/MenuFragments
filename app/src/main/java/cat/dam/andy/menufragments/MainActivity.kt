package cat.dam.andy.menufragments

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

    companion object {
        // els companion object són objectes que es poden accedir sense necessitat de crear una instància de la classe
        private const val KEY_SELECTED_ACTIVITY = "selectedActivity"
        private const val KEY_SELECTED_BUTTON_ID = "selectedButtonId"
    }

    // Afegit per mantenir un seguiment de l'activitat seleccionada
    private var selectedActivity: Class<*> = Fragment1::class.java
    private var selectedButtonId: Int = R.id.button1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.frameLayout)

        // Botons superiors
        val buttons = listOf<Button>(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4)
        )

        // Restaurar l'estat si és necessari (per exemple, en cas de rotació)
        if (savedInstanceState != null) {
            val className = savedInstanceState.getString(KEY_SELECTED_ACTIVITY)
            selectedActivity = try {
                Class.forName(className!!) as Class<*>
            } catch (e: ClassNotFoundException) {
                // Si no es pot trobar la classe, utilitza la classe per defecte (Fragment1::class.java)
                Fragment1::class.java
            }
            selectedButtonId = savedInstanceState.getInt(KEY_SELECTED_BUTTON_ID)
        }

        // Configurar els botons
        buttons.forEachIndexed { index, button ->
            val activityClass = getActivityClass(index + 1)
            button.setOnClickListener {
                refreshButtons(buttons, it.id)
                navigateToActivity(activityClass)
            }
        }

        // Mostrar l'activitat seleccionada. Per defecte la primera
        refreshButtons(buttons, selectedButtonId)
        navigateToActivity(selectedActivity)
    }

    private fun refreshButtons(buttons: List<Button>, buttonId: Int) {
        // Restaura el color del fons de tots els botons
        buttons.forEach { button ->
            button.setBackgroundResource(android.R.drawable.btn_default)
        }
        // Canvia el color del fons del botó actual
        findViewById<Button>(buttonId).setBackgroundResource(R.drawable.button_background_selected)
        selectedButtonId = buttonId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guardar l'activitat seleccionada i el botó per a la restauració de l'estat
        outState.putString(KEY_SELECTED_ACTIVITY, selectedActivity.name) // Guardar el nom de la classe
        outState.putInt(KEY_SELECTED_BUTTON_ID, selectedButtonId)
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        // Actualitza l'activitat seleccionada
        selectedActivity = activityClass
        // Infla l'activitat seleccionada
        inflateSelectedActivity()
    }

    private fun inflateSelectedActivity() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        // Crea una instància del fragment corresponent
        val fragment = when (selectedActivity) {
            Fragment1::class.java -> Fragment1()
            Fragment2::class.java -> Fragment2()
            Fragment3::class.java -> Fragment3()
            Fragment4::class.java -> Fragment4()
            else -> Fragment1()  // Fragment per defecte
        }

        // Reemplaça el contingut actual pel fragment
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    private fun getActivityClass(buttonNumber: Int): Class<*> {
        return when (buttonNumber) {
            1 -> Fragment1::class.java
            2 -> Fragment2::class.java
            3 -> Fragment3::class.java
            4 -> Fragment4::class.java
            else -> Fragment1::class.java
        }
    }
}
