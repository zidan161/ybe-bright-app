package com.example.ybebrightapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import java.io.File

class ConverterHelper {

    @RequiresApi(Build.VERSION_CODES.N)
    fun jsonToExcel() {
        val jsonTree = ObjectMapper().readTree("{sjjnsnsnj}")

        val csvSchemaBuilder = CsvSchema.builder()
        val firstObject = jsonTree.elements().next()
        firstObject.fieldNames().forEachRemaining { fieldName: String? ->
            csvSchemaBuilder.addColumn(
                fieldName
            )
        }
        val csvSchema: CsvSchema = csvSchemaBuilder.build()

        val csvMapper = CsvMapper()
        csvMapper.writerFor(JsonNode::class.java)
            .with(csvSchema)
            .writeValue(File("src/main/resources/orderLines.csv"), jsonTree)
    }
}

