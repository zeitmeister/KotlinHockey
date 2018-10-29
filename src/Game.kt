
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

fun main(args: Array<String>) {
    println("Use the ID to select your team: ")
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
            val gson = GsonBuilder().create()

            val league = gson.fromJson(body, League::class.java)
            for (index in league.teams.indices) {
                println(league.teams[index].name + " | " + league.teams[index].id )
            }
            val userInput: String = readLine().toString()

            selectTeam(league.teams, userInput)
            p1?.body()?.close()
        }
        override fun onFailure(p0: Call?, p1: IOException?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            println("Failed to execute request")
        }
    })


}

fun selectTeam(teams: List<Team>, input: String) {
    for (index in teams.indices) {
        if (teams[index].id == input.toInt()) {
            getPlayersInTeam(input.toInt())
        }
    }
}

fun getPlayersInTeam(id: Int): NHLTeam {

    val playerUrl = "https://statsapi.web.nhl.com/api/v1/teams/$id/roster"
    val request = okhttp3.Request.Builder().url(playerUrl).build()
    val client = okhttp3.OkHttpClient()
    var nhlTeam =  NHLTeam("", 0, mutableListOf())
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
            nhlTeam =  NHLTeam("Vegas Golden Knights", id, players)
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onFailure(p0: Call?, p1: IOException?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })
    return nhlTeam
}
