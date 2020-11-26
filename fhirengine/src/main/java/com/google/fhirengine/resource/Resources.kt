package com.google.fhirengine.resource

import com.google.fhir.r4.core.Id
import com.google.fhir.r4.core.ResourceTypeCode
import com.google.fhir.shaded.protobuf.Message

val Message.type: ResourceTypeCode.Value
    get() = ResourceTypeCode.Value.valueOf(this.javaClass.simpleName.toUpperCase())

val Message.id: String
    get() = (this.javaClass.getMethod("getId").invoke(this) as Id).value