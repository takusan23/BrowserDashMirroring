package io.github.takusan23.browserdashmirroring

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun main(args: Array<String>) {

    // 映像の保存先
    // プロジェクトのフォルダに作る
    // Node.js の process.cwd() みたいな
    val projectFolder = System.getProperty("user.dir")
    val segmentSaveFolder = File(projectFolder, "static").apply {
        listFiles()?.forEach { it.delete() }
        mkdir()
    }

    // セグメントのインデックス
    var index = 0

    println("http://localhost:8080")

    embeddedServer(Netty, port = 8080) {
        routing {
            // プロジェクトの resources フォルダから取得
            // index.html を返す
            resource("/", "index.html")
            // マニフェストを返す
            resource("/manifest.mpd", "manifest.mpd")

            // フロント側からWebMの細切れが送られてくるので保存していく
            post("/api/upload") {
                // Multipart-FormDataを受け取る
                call.receiveMultipart().forEachPart { partData ->
                    if (partData is PartData.FileItem) {
                        // ファイルを作って保存
                        File(segmentSaveFolder, "segment${index++}.webm").apply {
                            createNewFile()
                            writeBytes(partData.streamProvider().readAllBytes())
                        }
                    }
                }
                call.respond(HttpStatusCode.OK, "保存できました")
            }

            // 静的ファイル公開するように。動画を配信する
            static {
                staticRootFolder = segmentSaveFolder
                files(segmentSaveFolder)
            }
        }
    }.start(true)
}