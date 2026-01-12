package com.ruhaan.orangeeditor.domain.model.layer

import android.net.Uri

sealed class ExportResult {
    object Idle : ExportResult()
    data class Success(val uri: Uri?) : ExportResult()
    data class Error(val message: String) : ExportResult()
}



