package com.ruhaan.orangeeditor.domain.model.layer

sealed class Layer(
  open val id: String,
  open val transform: Transform,
  open val zIndex: Int,
  open val visible: Boolean = true
)
