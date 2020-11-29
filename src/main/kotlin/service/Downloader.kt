package com.xobotun.elite_trade_loop_finder.service

import khttp.get
import khttp.head
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class Downloader {

    val webPath = props.getProperty("eddb.files.url")
    val filePath = props.getProperty("eddb.files.location")

    fun renew() {
        listOf("commodities.json", "listings.csv", "systems_populated.json", "stations.json").stream()
            .filter { shouldDownload(it, getLocalDate(it)) }
            .forEach { download(it) }
    }

    private fun getLocalDate(resource: String) = if (File(filePath + resource).exists()) LocalDateTime.ofEpochSecond(File(filePath + resource).lastModified() / 1000, 0, ZoneOffset.UTC) else LocalDateTime.MIN

    private fun shouldDownload(resource: String, localTimestamp: LocalDateTime) = parseLastModified(getWebLastModified(resource)) > localTimestamp

    private fun getWebLastModified(resource: String) = head(webPath + resource, mapOf("Accept-Encoding" to "")).headers["Last-Modified"]

    private fun parseLastModified(header: String?) = if (header == null) LocalDateTime.MIN else LocalDateTime.parse(header, DateTimeFormatter.RFC_1123_DATE_TIME)

    private fun download(resource: String) {
        val downloaded = get(webPath + resource)
        val path = Path.of(filePath + resource)

        Files.write(path, downloaded.raw.readAllBytes())
        Files.setLastModifiedTime(path, FileTime.from(parseLastModified(downloaded.headers["Last-Modified"]).toInstant(ZoneOffset.UTC)))
    }

}
