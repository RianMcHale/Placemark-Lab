package org.setu.placemark

import org.setu.placemark.models.PlacemarkMemStore
import org.setu.placemark.models.PlacemarkStore

object AppData {
    val placedMarks: PlacemarkStore = PlacemarkMemStore()
}