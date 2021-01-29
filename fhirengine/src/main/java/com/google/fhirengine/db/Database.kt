/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.fhirengine.db

import com.google.fhirengine.db.impl.entities.LocalChange
import com.google.fhirengine.db.impl.entities.SyncedResourceEntity
import com.google.fhirengine.search.impl.Query
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

/** The interface for the FHIR resource database.  */
interface Database {
    /**
     * Inserts the `resource` into the FHIR resource database. If the resource already
     * exists, it will be overwritten
     *
     * @param <R> The resource type
     */
    fun <R : Resource> insert(resource: R)

    /**
     * Inserts a list of `resource`s into the FHIR resource database. If any of the resources
     * already exists, it will be overwritten
     *
     * @param <R> The resource type
     */
    fun <R : Resource> insertAll(resources: List<R>)

    /**
     * Updates the `resource` in the FHIR resource database. If the resource does not already
     * exist, a new record will be created.
     *
     * @param <R> The resource type
     */
    fun <R : Resource> update(resource: R)

    /**
     * Selects the FHIR resource of type `clazz` with `id`.
     *
     * @param <R> The resource type
     * @throws ResourceNotFoundInDbException if the resource is not found in the database
     */
    @Throws(ResourceNotFoundInDbException::class)
    fun <R : Resource> select(clazz: Class<R>, id: String): R

    /**
     * Return the last update data of a resource based on the resource type.
     * If no resource of [resourceType] is inserted, return `null`.
     * @param resourceType The resource type
     */
    suspend fun lastUpdate(resourceType: ResourceType): String?

    /**
     * Insert a resource that was syncronised.
     *
     * @param syncedResourceEntity The synced resource
     */
    suspend fun insertSyncedResources(
        syncedResourceEntity: SyncedResourceEntity,
        resources: List<Resource>
    )

    /**
     * Deletes the FHIR resource of type `clazz` with `id`.
     *
     * @param <R> The resource type
     */
    fun <R : Resource> delete(clazz: Class<R>, id: String)

    /**
     * Returns a [List] of [Resource]s that are of type `clazz` and have `reference` with `value`.
     *
     * For example, a search for [org.hl7.fhir.r4.model.Observation]s with `reference`
     * 'subject' and `value` 'Patient/1' will return all observations associated with the
     * particular patient.
     */
    fun <R : Resource> searchByReference(
        clazz: Class<R>,
        reference: String,
        value: String
    ): List<R>

    /**
     * Returns a [List] of [Resource]s that are of type `clazz` and have `string` with `value`.
     *
     * For example, a search for [org.hl7.fhir.r4.model.Patient]s with `string` 'given'
     * and `value` 'Tom' will return all patients with a given name Tom.
     */
    fun <R : Resource> searchByString(
        clazz: Class<R>,
        string: String,
        value: String
    ): List<R>

    /**
     * Returns a [List] of [Resource]s that are of type `clazz` and have `code` with `system` and
     * `value`.
     *
     * For example, a search for [org.hl7.fhir.r4.model.Observation]s with `code` 'code', `system`
     * 'http://openmrs.org/concepts' and `value` '1427AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' will return
     * all observations with the given code.
     */
    fun <R : Resource> searchByCode(
        clazz: Class<R>,
        code: String,
        system: String,
        value: String
    ): List<R>

    /**
     * Returns a [List] of [Resource]s that are of type `clazz` and have `reference` with `value`
     * and `code` with `system` and `value`.
     *
     * For example, a search for [org.hl7.fhir.r4.model.Observation]s with `reference`
     * 'subject' and `value` 'Patient/1' as well as with `code` 'code', `system`
     * 'http://openmrs.org/concepts' and `value` '1427AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' will return
     * all observations associated with the particular patient by reference and with the given code.
     */
    fun <R : Resource> searchByReferenceAndCode(
        clazz: Class<R>,
        reference: String,
        referenceValue: String,
        code: String,
        codeSystem: String,
        codeValue: String
    ): List<R>

    fun <R : Resource> search(query: Query): List<R>

    /**
     * Retrieves all [LocalChange]s for all [Resource]s. The [LocalChange]s are in a form which
     * can be synced. Multiple changes for a single [Resource] are squashed into a single
     * [LocalChange].
     */
    fun getAllLocalChanges(): List<LocalChange>

    /**
     * Remove the [LocalChange]s with given ids. Call this after a successful sync.
     */
    fun <R : Resource> deleteUpdates(clazz: Class<R>, id: String)
}
