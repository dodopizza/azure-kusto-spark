package com.microsoft.kusto.spark.datasink

import java.util.UUID

import com.microsoft.kusto.spark.common.KustoOptions

import scala.concurrent.duration.FiniteDuration

object KustoSinkOptions extends KustoOptions{
  val KUSTO_TABLE: String = newOption("kustoTable")

  val KUSTO_CUSTOM_DATAFRAME_COLUMN_TYPES: String = newOption("customSchema")

  // If set to 'FailIfNotExist', the operation will fail if the table is not found
  // in the requested cluster and database.
  // If set to 'CreateIfNotExist' and the table is not found in the requested cluster and database,
  // it will be created, with a schema matching the DataFrame that is being written.
  // Default: 'FailIfNotExist'
  val KUSTO_TABLE_CREATE_OPTIONS: String = newOption("tableCreateOptions")
  // When writing to Kusto, allows the driver to complete operation asynchronously.  See KustoSink.md for
  // details and limitations. Default: 'false'
  val KUSTO_WRITE_ENABLE_ASYNC: String = newOption("writeEnableAsync")
  // When writing to Kusto, limits the number of rows read back as BaseRelation. Default: '1'.
  // To read back all rows, set as 'none' (NONE_RESULT_LIMIT)
  val KUSTO_WRITE_RESULT_LIMIT: String = newOption("writeResultLimit")

  // A json representation of the SparkIngestionProperties object used for writing to Kusto
  val KUSTO_SPARK_INGESTION_PROPERTIES_JSON: String = newOption("sparkIngestionPropertiesJson")

  val NONE_RESULT_LIMIT = "none"

  // A limit indicating the size in MB of the aggregated data before ingested to Kusto. Note that this is done for each
  // partition. Kusto's ingestion also aggregates data, default suggested by Kusto is 1GB but here we suggest to cut
  // it at 100MB to adjust it to spark pulling of data.
  val KUSTO_CLIENT_BATCHING_LIMIT: String = newOption("clientBatchingLimit")

  // An integer number corresponding to the period in seconds after which the staging resources used for the writing
  // are cleaned if they weren't cleaned at the end of the run
  val KUSTO_STAGING_RESOURCE_AUTO_CLEANUP_TIMEOUT: String = newOption("stagingResourcesAutoCleanupTimeout")
}

object SinkTableCreationMode extends Enumeration {
  type SinkTableCreationMode = Value
  val CreateIfNotExist, FailIfNotExist = Value
}

case class WriteOptions(tableCreateOptions: SinkTableCreationMode.SinkTableCreationMode = SinkTableCreationMode.FailIfNotExist,
                        isAsync: Boolean = false,
                        writeResultLimit: String = KustoSinkOptions.NONE_RESULT_LIMIT,
                        timeZone: String = "UTC",
                        timeout: FiniteDuration,
                        IngestionProperties: Option[String] = None,
                        batchLimit: Int = 100,
                        requestId: String = UUID.randomUUID().toString,
                        autoCleanupTime: FiniteDuration,
                        maxRetriesOnMoveExtents: Int = 10,
                        minimalExtentsCountForSplitMerge: Int = 400)
