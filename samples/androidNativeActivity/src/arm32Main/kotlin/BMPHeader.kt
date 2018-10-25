package sample.androidnative

import kotlinx.cinterop.*
import sample.androidnative.bmpformat.BMPHeader

val BMPHeader.data
    get() = interpretCPointer<ByteVar>(rawPtr + sizeOf<BMPHeader>()) as CArrayPointer<ByteVar>
