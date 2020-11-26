package com.google.fhirengine.resource

import android.os.Build
import com.google.common.truth.Truth.assertThat
import com.google.fhir.r4.core.Id
import com.google.fhir.r4.core.Patient
import com.google.fhir.r4.core.ResourceTypeCode
import com.google.fhir.shaded.protobuf.Message
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ResourcesTest {
    @Test
    fun type() {
        assertThat(
            (Patient.getDefaultInstance() as Message).type
        ).isEqualTo(ResourceTypeCode.Value.PATIENT)
    }

    @Test
    fun id() {
        assertThat(
            (Patient.newBuilder().setId(Id.newBuilder().setValue("new-patient"))
                .build() as Message).id
        ).isEqualTo("new-patient")
    }
}