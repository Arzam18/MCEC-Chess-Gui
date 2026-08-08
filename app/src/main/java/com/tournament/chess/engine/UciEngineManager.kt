package com.tournament.chess.engine

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.concurrent.atomic.AtomicBoolean

class UciEngineManager(
    private val onOutputReceived: (String) -> Unit,
    private val onBestMoveReceived: (String) -> Unit
) {
    private var process: Process? = null
    private var writer: BufferedWriter? = null
    private var reader: BufferedReader? = null
    private val isRunning = AtomicBoolean(false)
    private val engineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startEngine(binaryPath: String): Boolean {
        try {
            val processBuilder = ProcessBuilder(binaryPath)
            processBuilder.redirectErrorStream(true)
            process = processBuilder.start()

            writer = BufferedWriter(OutputStreamWriter(process!!.outputStream))
            reader = BufferedReader(InputStreamReader(process!!.inputStream))
            isRunning.set(true)

            startListening()
            sendCommand("uci")
            return true
        } catch (e: Exception) {
            Log.e("UciEngineManager", "Failed to start engine: ${e.message}")
            return false
        }
    }

    private fun startListening() {
        engineScope.launch {
            try {
                var line: String?
                while (isRunning.get() && reader != null) {
                    line = reader?.readLine()
                    if (line != null) {
                        withContext(Dispatchers.Main) {
                            onOutputReceived(line)
                        }
                        if (line.startsWith("bestmove")) {
                            val parts = line.split(" ")
                            if (parts.size >= 2) {
                                val bestMove = parts[1]
                                withContext(Dispatchers.Main) {
                                    onBestMoveReceived(bestMove)
                                }
                            }
                        }
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                Log.e("UciEngineManager", "Error reading output: ${e.message}")
            }
        }
    }

    fun sendCommand(command: String) {
        engineScope.launch {
            try {
                writer?.let {
                    it.write("$command\n")
                    it.flush()
                }
            } catch (e: Exception) {
                Log.e("UciEngineManager", "Failed to send command: ${e.message}")
            }
        }
    }

    fun stopEngine() {
        isRunning.set(false)
        try {
            sendCommand("quit")
            writer?.close()
            reader?.close()
            process?.destroy()
        } catch (e: Exception) {
            Log.e("UciEngineManager", "Error stopping engine: ${e.message}")
        } finally {
            process = null
            writer = null
            reader = null
        }
    }
}
