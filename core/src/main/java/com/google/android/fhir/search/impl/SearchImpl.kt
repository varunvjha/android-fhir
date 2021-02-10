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

package com.google.android.fhir.search.impl

import com.google.android.fhir.db.Database
import com.google.android.fhir.resource.getResourceType
import com.google.android.fhir.search.Search
import com.google.android.fhir.search.filter.FilterCriterion
import com.google.android.fhir.search.sort.SortCriterion
import org.hl7.fhir.r4.model.Resource

/** Implementation of the [Search] interface. */
class SearchImpl constructor(val database: Database) : Search {
    override fun <R : Resource> of(clazz: Class<R>) = SearchSpecificationImpl(clazz)

    /** Implementation of the [Search.SearchSpecifications] interface. */
    inner class SearchSpecificationImpl<R : Resource>(
        val clazz: Class<R>
    ) : Search.SearchSpecifications {
        private var filterCriterion: FilterCriterion? = null
        private var sortCriterion: SortCriterion? = null
        private var limit: Int? = null
        private var skip: Int? = null

        override fun filter(filterCriterion: FilterCriterion): Search.SearchSpecifications =
            apply { this.filterCriterion = filterCriterion }

        override fun sort(sortCriterion: SortCriterion): Search.SearchSpecifications =
            apply { this.sortCriterion = sortCriterion }

        override fun limit(limit: Int): Search.SearchSpecifications = apply { this.limit = limit }

        override fun skip(skip: Int): Search.SearchSpecifications = apply { this.skip = skip }

        override fun <R : Resource> run(): List<R> = database.search(
            SerializedResourceQuery(getResourceType(clazz),
                filterCriterion?.query(clazz), sortCriterion, limit, skip))
    }
}
