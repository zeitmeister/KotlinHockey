
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

fun main(args: Array<String>) {
    getTeams()
}


fun getTeams () {
    val url = "https://statsapi.web.nhl.com/api/v1/teams"

    val request = okhttp3.Request.Builder().url(url).build()
    val client = okhttp3.OkHttpClient()
    client.newCall(request).enqueue(object: Callback {
        override fun onResponse(p0: Call?, p1: Response?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            val body = p1?.body()?.string()
            println(body)
            val gson = GsonBuilder().create()

            val league = gson.fromJson(body, League::class.java)
            getPlayersInTeam(league.teams[12].id)
        }
        override fun onFailure(p0: Call?, p1: IOException?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            println("Failed to execute request")
        }
    })
}

fun getPlayersInTeam(id: Int) {

    val playerUrl = "https://statsapi.web.nhl.com/api/v1/teams/$id/roster"
    val request = okhttp3.Request.Builder().url(playerUrl).build()
    val client = okhttp3.OkHttpClient()
    client.newCall(request).enqueue(object: Callback {
        override fun onResponse(p0: Call?, p1: Response?) {
            val body = p1?.body()?.string()

            val gson = GsonBuilder().create()
            var players: List<Player> = mutableListOf()
            val roster = gson.fromJson(body, Skater::class.java)
            val rosterJersy = gson.fromJson(body, SkaterJersy::class.java)
            val rosterPosition = gson.fromJson(body, SkaterPosition::class.java)
            for (i in roster.roster.indices) {
                println(roster.roster[i].person.fullName + " " + rosterJersy.roster[i].jerseyNumber + " " + rosterPosition.roster[i].position.name)
                players += mutableListOf(Player(roster.roster[i].person.fullName, rosterJersy.roster[i].jerseyNumber, rosterPosition.roster[i].position.name))
            }
            val tampaBay = NHLTeam("Vegas Golden Knights", 1, players)
            println(tampaBay)
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onFailure(p0: Call?, p1: IOException?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })
}
