package pl.szkoleniaandroid.solarsystem

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset

class SolarObjectsRepository(context: Context) {
    val planets: Array<SolarObject> = context.loadArrayFromJson("planets")
    val others: Array<SolarObject> = context.loadArrayFromJson("others")
    val objectsWithMoons: Array<SolarObject>

    init {
        objectsWithMoons = (planets + others)
            .filter { it.hasMoons }
            .toTypedArray()
    }
}


@Parcelize
class SolarObject(
    val name: String,
    val textFilename: String,
    val imageFilename: String,
    val video: String? = null,
    val moons: Array<SolarObject> = emptyArray()
) : Parcelable {
    val imagePath: String
        get() = String.format("file:///android_asset/%s", imageFilename)

    val hasMoons: Boolean
        get() = moons.isNotEmpty()
}

fun Context.loadStringFromAssets(fileName: String): String {
    return try {
        val inputStream = assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (e: IOException) {
        ""
    }
}

fun Context.loadArrayFromJson(type: String): Array<SolarObject> {
    return try {
        val json = loadStringFromAssets("solar.json")
        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject.getJSONArray(type)
        jsonArray.getSolarObjectFromJsonArray()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyArray()
    }
}


fun JSONObject.toSolarObject(): SolarObject {
    val name = getString("name")
    val image = String.format("images/%s.jpg", name.toLowerCase())
    val text = String.format("texts/%s.txt", name.toLowerCase())
    val video = optString("video")
    val moonsJson = optJSONArray("moons")
    val moons = moonsJson.getSolarObjectFromJsonArray()
    return SolarObject(
        name = name,
        textFilename = text,
        imageFilename = image,
        video = video,
        moons = moons
    )
}

@Throws(JSONException::class)
private fun JSONArray?.getSolarObjectFromJsonArray() =
    if (this == null)
        emptyArray()
    else
        Array(length()) {
            getJSONObject(it).toSolarObject()
        }
