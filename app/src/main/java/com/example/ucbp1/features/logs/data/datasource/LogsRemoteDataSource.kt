package com.example.ucbp1.features.logs.data.datasource

import com.develoop.logs.LogApi
import com.develoop.logs.LogServiceGrpcKt
import io.grpc.ManagedChannel
import java.io.Closeable

// Este es el constructor correcto que solo pide el canal
class LogsRemoteDataSource(
    private val channel: ManagedChannel
) : Closeable {

    private val stub: LogServiceGrpcKt.LogServiceCoroutineStub =
        LogServiceGrpcKt.LogServiceCoroutineStub(channel)

    suspend fun send(request: LogApi.LogRequest): LogApi.LogResponse {
        // CORRECCIÓN: El nombre del método generado por gRPC usa CamelCase,
        // por lo que es "SendLog" con 'S' mayúscula, no 'sendLog'.
        return stub.send(request)
    }

    override fun close() {
        channel.shutdownNow()
    }
}
