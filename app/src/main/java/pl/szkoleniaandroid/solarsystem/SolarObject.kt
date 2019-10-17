package pl.szkoleniaandroid.solarsystem

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.nio.charset.Charset

@Parcelize
data class SolarObject(
    val name: String,
    val text: String,
    val image: String,
    val video: String? = null,
    val moons: List<SolarObject> = emptyList()
) : Parcelable {

    val imagePath: String
        get() = String.format("file:///android_asset/%s", image)

    val hasMoons: Boolean
        get() = moons.isNotEmpty()


    companion object {

        fun loadArrayFromJson(context: Context, type: String): List<SolarObject> {
            try {
                val json = loadStringFromAssets(context, "solar.json")
                val jsonObject = JSONObject(json)
                val jsonArray = jsonObject.getJSONArray(type)
                return jsonArray.getSolarObjectFromJsonArray()

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return emptyList()
        }

        @Throws(IOException::class)
        fun loadStringFromAssets(context: Context, fileName: String): String {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()

            return String(buffer, Charset.forName("UTF-8"))

        }
    }
}

fun JSONObject.toSolarObject(): SolarObject {
    val name = getString("name")
    val image = String.format("images/%s.jpg", name.toLowerCase())
    val text = String.format("texts/%s.txt", name.toLowerCase())
    val video = optString("video")
    val moonsJson = optJSONArray("moons")
    val moons = moonsJson?.let { it.getSolarObjectFromJsonArray() } ?: emptyList()
    return SolarObject(
        name = name,
        text = text,
        image = image,
        video = video,
        moons = moons
    )
}

@Throws(JSONException::class)
private fun JSONArray.getSolarObjectFromJsonArray(): List<SolarObject> {
    val solarObjects = mutableListOf<SolarObject>()
    for (i in 0 until length()) {
        solarObjects.add(getJSONObject(i).toSolarObject())

    }
    return solarObjects
}

